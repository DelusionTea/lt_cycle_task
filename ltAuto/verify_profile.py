"""Единый gate-проверки профиля Gatling и рефакторинга весов.

Используется слабыми ИИ-моделями после каждой итерации (one-file-at-a-time):
одна команда → PASS/FAIL с понятным выводом.

Примеры (из каталога gatling/gatlingScripts или корня scaffold с ltAuto/):

  # После codemod одного сценария:
  python3 ltAuto/verify_profile.py --scenario src/test/java/scenarios/.../LicensesScenario.java --compile

  # После правки profile.yaml:
  python3 ltAuto/verify_profile.py --profile profiles/efsFinmonWeb/profile.yaml

  # Round-trip: веса в сценарии (дефолты getWeight) vs profile_to_props:
  python3 ltAuto/verify_profile.py \\
      --scenario resources/LicensesScenario.java \\
      --profile /tmp/pg.yaml --round-trip --scenario-name Licenses

Exit code: 0 = PASS, 1 = FAIL, 2 = ошибка запуска/аргументов.
"""

from __future__ import annotations

import argparse
import glob
import os
import re
import subprocess
import sys
import tempfile

import yaml

_HERE = os.path.dirname(os.path.abspath(__file__))
if _HERE not in sys.path:
    sys.path.insert(0, _HERE)

from case_parser import parse_case_classes  # noqa: E402
from profile_paths import build_profile_base_dirs, as_name_from_profile_path, detect_gatling_root
from profile_to_props import build_properties  # noqa: E402
from sim_to_profile import parse_weights  # noqa: E402

_NUMERIC_WEIGHT = re.compile(r"new\s+Choice\.WithWeight\(\s*(\d+)")
_SCN_CONST = re.compile(r'String\s+SCN\s*=\s*"([^"]+)"')
_GET_WEIGHT = re.compile(
    r'getWeight\(\s*(\w+)\s*,\s*"([^"]+)"\s*,\s*([\d.]+)\s*\)'
)


class Reporter:
    def __init__(self, strict: bool):
        self.strict = strict
        self.ok = 0
        self.fail = 0
        self.warn = 0

    def _print(self, tag, msg):
        print("[verify] {}: {}".format(tag, msg))

    def ok_msg(self, msg):
        self.ok += 1
        self._print("OK", msg)

    def fail_msg(self, msg):
        self.fail += 1
        self._print("FAIL", msg)

    def warn_msg(self, msg):
        self.warn += 1
        self._print("WARN", msg)

    def passed(self):
        extra = self.warn if self.strict else 0
        return self.fail + extra == 0

    def summary(self):
        status = "PASS" if self.passed() else "FAIL"
        print("---")
        print("RESULT: {} ({} ok, {} fail, {} warn)".format(
            status, self.ok, self.fail, self.warn))
        if self.strict and self.warn:
            print("(режим --strict: WARN считаются FAIL)")


def detect_base_dir(explicit: str | None) -> str:
    if explicit:
        return os.path.abspath(explicit)
    cwd = os.getcwd()
    candidates = [
        cwd,
        os.path.join(cwd, "gatling", "gatlingScripts"),
    ]
    for c in candidates:
        if os.path.isfile(os.path.join(c, "pom.xml")) and os.path.isdir(os.path.join(c, "ltAuto")):
            return c
    if os.path.isdir(os.path.join(cwd, "ltAuto")):
        return cwd
    return cwd


def build_base_dirs(base: str, profile_path: str | None) -> list[str]:
    if profile_path:
        return build_profile_base_dirs(profile_path, gatling_root=base)
    return build_profile_base_dirs(os.path.join(base, "profiles"), gatling_root=base)


def resolve_path(base: str, path: str) -> str:
    if os.path.isabs(path):
        return path
    for root in (base, os.getcwd()):
        cand = os.path.join(root, path)
        if os.path.exists(cand):
            return cand
    return os.path.join(base, path)


def strip_comments_line(line: str) -> str:
    if line.lstrip().startswith("//"):
        return ""
    return line


def check_scenario_file(r: Reporter, path: str, expect_scn: str | None, allow_numeric: bool = False):
    if not os.path.isfile(path):
        r.fail_msg("файл сценария не найден: {}".format(path))
        return None
    with open(path, encoding="utf-8", errors="replace") as fh:
        text = fh.read()

    scn_m = _SCN_CONST.search(text)
    scn = scn_m.group(1) if scn_m else None
    if scn:
        r.ok_msg("SCN={} в {}".format(scn, os.path.basename(path)))
    elif _GET_WEIGHT.search(text):
        r.fail_msg("используется getWeight, но нет константы SCN в {}".format(path))
    if expect_scn and scn and scn != expect_scn:
        r.fail_msg("SCN '{}' != ожидаемого '{}'".format(scn, expect_scn))

    numeric_hits = []
    for i, line in enumerate(text.splitlines(), 1):
        if strip_comments_line(line) == "":
            continue
        for m in _NUMERIC_WEIGHT.finditer(line):
            if "ProfileConfig.getWeight" not in line:
                numeric_hits.append((i, m.group(1)))

    if numeric_hits:
        for ln, w in numeric_hits[:5]:
            msg = "числовой WithWeight({}) строка {} в {}".format(w, ln, path)
            if allow_numeric:
                r.warn_msg(msg + " (--allow-numeric-weights)")
            else:
                r.fail_msg(msg)
        if len(numeric_hits) > 5:
            extra = "... ещё {} числовых WithWeight".format(len(numeric_hits) - 5)
            if allow_numeric:
                r.warn_msg(extra)
            else:
                r.fail_msg(extra)
    else:
        if "Choice.WithWeight" in text or "randomSwitch" in text:
            r.ok_msg("нет числовых WithWeight в {}".format(os.path.basename(path)))

    if "ProfileConfig.getWeight" in text:
        if "import config.ProfileConfig" in text:
            r.ok_msg("import ProfileConfig присутствует")
        else:
            r.fail_msg("getWeight без import config.ProfileConfig")

    gw = _GET_WEIGHT.findall(text)
    if gw:
        r.ok_msg("найдено {} вызовов getWeight".format(len(gw)))
    return scn


def check_profile_yaml(r: Reporter, path: str, base_dirs: list[str], expect_scn: str | None, expect_as: str | None = None):
    if not os.path.isfile(path):
        r.fail_msg("profile не найден: {}".format(path))
        return None
    try:
        with open(path, encoding="utf-8") as fh:
            cfg = yaml.load(fh, Loader=yaml.FullLoader) or {}
    except yaml.YAMLError as e:
        r.fail_msg("YAML не читается: {}".format(e))
        return None
    r.ok_msg("YAML загружен: {}".format(os.path.basename(path)))

    as_name = as_name_from_profile_path(path)
    if as_name:
        r.ok_msg("АС (имя каталога profiles/): {}".format(as_name))
        if expect_as and as_name != expect_as:
            r.fail_msg("каталог profiles/{} != APPLICATION/ожидание {}".format(as_name, expect_as))
        cases_rel = "src/test/java/cases/{}".format(as_name)
        root = detect_gatling_root()
        if os.path.isdir(os.path.join(root, "src", "test", "java", "cases", as_name)):
            r.ok_msg("Case-каталог существует: {}".format(cases_rel))
        else:
            r.warn_msg("Case-каталог не найден: {} (scaffold — ок)".format(cases_rel))
    else:
        r.warn_msg("профиль не в profiles/<АС>/profile.yaml — рекомендуется такая структура")

    counts = cfg.get("count") or {}
    top_rc = cfg.get("request_classes") or []
    if top_rc and counts:
        try:
            var2log = parse_case_classes(top_rc, base_dirs=base_dirs)
            for key in counts:
                if key not in var2log and key not in var2log.values():
                    r.fail_msg("count['{}'] не резолвится через request_classes".format(key))
            if not any(k for k in counts if k not in var2log and k not in var2log.values()):
                r.ok_msg("все ключи count резолвятся ({} шт.)".format(len(counts)))
        except FileNotFoundError as e:
            r.fail_msg("request_classes: {}".format(e))

    inj = cfg.get("injection") or {}
    scenarios = inj.get("scenarios") or {}
    for scn_name, scn_cfg in scenarios.items():
        if expect_scn and scn_name != expect_scn:
            continue
        classes = scn_cfg.get("request_classes") or scn_cfg.get("request_class")
        if classes and isinstance(classes, str):
            classes = [classes]
        if not classes:
            r.warn_msg("сценарий '{}' без request_classes — веса не считаются авто".format(scn_name))
            continue
        try:
            props_text = build_properties(cfg, float(cfg.get("target_percent", 100)), base_dirs)
            weights = {}
            prefix = "weight.{}.".format(scn_name)
            for line in props_text.splitlines():
                if line.startswith(prefix):
                    var = line[len(prefix):].split("=", 1)[0]
                    weights[var] = float(line.split("=", 1)[1])
            if not weights:
                r.fail_msg("profile_to_props не выдал веса для '{}'".format(scn_name))
                continue
            total = sum(weights.values())
            if abs(total - 100.0) > 0.05 and total > 0:
                r.fail_msg("сумма весов '{}' = {:.4f} (ожид. ~100)".format(scn_name, total))
            else:
                r.ok_msg("сумма весов '{}' = {:.2f}".format(scn_name, total))
        except Exception as e:
            r.fail_msg("profile_to_props для '{}': {}".format(scn_name, e))

    return cfg


def parse_props_weights(text: str, scn: str) -> dict[str, float]:
    out = {}
    prefix = "weight.{}.".format(scn)
    for line in text.splitlines():
        if line.startswith(prefix):
            var, val = line[len(prefix):].split("=", 1)
            out[var.strip()] = float(val.strip())
    return out


def check_round_trip(r: Reporter, scenario_path: str, profile_path: str, scn: str, base_dirs: list[str]):
    with open(scenario_path, encoding="utf-8") as fh:
        scn_text = fh.read()
    scenario_weights = parse_weights(scn_text)
    if not scenario_weights:
        r.fail_msg("round-trip: в сценарии нет весов WithWeight/getWeight")
        return

    with open(profile_path, encoding="utf-8") as fh:
        cfg = yaml.load(fh, Loader=yaml.FullLoader) or {}
    props = build_properties(cfg, float(cfg.get("target_percent", 100)), base_dirs)
    profile_weights = parse_props_weights(props, scn)

    if not profile_weights:
        r.fail_msg("round-trip: нет weight.{}. в properties".format(scn))
        return

    sw = sum(scenario_weights.values())
    mism = []
    for var, w in scenario_weights.items():
        if var not in profile_weights:
            mism.append("{}: нет в profile".format(var))
            continue
        pw = profile_weights[var]
        # сравниваем целые проценты (как в codemod) или доли count
        exp_pct = round(100.0 * w / sw) if sw else 0
        if abs(pw - exp_pct) > 1.5 and abs(pw - w) > 1.5:
            mism.append("{}: сценарий={} profile={:.2f}".format(var, w, pw))

    if mism:
        for m in mism[:8]:
            r.fail_msg("round-trip: {}".format(m))
    else:
        r.ok_msg("round-trip OK: {} запросов, веса совпадают".format(len(scenario_weights)))


def check_compile(r: Reporter, base_dir: str):
    pom = os.path.join(base_dir, "pom.xml")
    if not os.path.isfile(pom):
        r.warn_msg("pom.xml не найден в {} — compile пропущен".format(base_dir))
        return
    try:
        proc = subprocess.run(
            ["mvn", "-q", "-DskipTests", "test-compile"],
            cwd=base_dir,
            capture_output=True,
            text=True,
            timeout=600,
        )
    except FileNotFoundError:
        r.warn_msg("mvn не найден в PATH — compile пропущен")
        return
    except subprocess.TimeoutExpired:
        r.fail_msg("mvn test-compile: timeout")
        return
    if proc.returncode == 0:
        r.ok_msg("mvn test-compile")
    else:
        r.fail_msg("mvn test-compile exit={}".format(proc.returncode))
        tail = (proc.stderr or proc.stdout or "").strip().splitlines()[-8:]
        for line in tail:
            print("         ", line)


def scan_scenarios_dir(r: Reporter, directory: str):
    pattern = os.path.join(directory, "**", "*.java")
    files = sorted(glob.glob(pattern, recursive=True))
    bad = []
    for f in files:
        with open(f, encoding="utf-8", errors="replace") as fh:
            for i, line in enumerate(fh, 1):
                if strip_comments_line(line) == "":
                    continue
                if _NUMERIC_WEIGHT.search(line) and "ProfileConfig.getWeight" not in line:
                    bad.append("{}:{}".format(f, i))
    if bad:
        for hit in bad[:10]:
            r.fail_msg("числовой WithWeight в {}".format(hit))
        if len(bad) > 10:
            r.fail_msg("... ещё {} файлов/строк с числовыми весами".format(len(bad) - 10))
    else:
        r.ok_msg("в каталоге {} нет числовых WithWeight ({} файлов)".format(directory, len(files)))


def main():
    ap = argparse.ArgumentParser(description="Gate-проверка профиля Gatling / весов сценариев")
    ap.add_argument("--base-dir", default=None, help="корень gatlingScripts (auto-detect)")
    ap.add_argument("--scenario", default=None, help="один файл *Scenario.java")
    ap.add_argument("--scenarios-dir", default=None, help="скан каталога scenarios на числовые веса")
    ap.add_argument("--profile", default=None, help="profile.yaml")
    ap.add_argument("--scenario-name", default=None, help="ожидаемый SCN / ключ injection.scenarios")
    ap.add_argument("--round-trip", action="store_true",
                    help="сравнить веса сценария с profile_to_props (нужны --scenario и --profile)")
    ap.add_argument("--compile", action="store_true", help="запустить mvn -q -DskipTests test-compile")
    ap.add_argument("--application", default=None,
                    help="имя АС — должно совпадать с каталогом profiles/<АС>/")
    ap.add_argument("--allow-numeric-weights", action="store_true",
                    help="не FAIL на числовых WithWeight (round-trip до codemod)")
    ap.add_argument("--strict", action="store_true", help="WARN трактовать как FAIL")
    args = ap.parse_args()

    if not any([args.scenario, args.scenarios_dir, args.profile, args.compile]):
        ap.error("укажите хотя бы --scenario, --scenarios-dir, --profile или --compile")

    base = detect_base_dir(args.base_dir)
    profile_resolved = resolve_path(base, args.profile) if args.profile else None
    base_dirs = build_base_dirs(base, profile_resolved)

    r = Reporter(strict=args.strict)
    scn = args.scenario_name

    if args.scenario:
        path = resolve_path(base, args.scenario)
        found = check_scenario_file(r, path, scn, allow_numeric=args.allow_numeric_weights)
        if found and not scn:
            scn = found

    if args.scenarios_dir:
        scan_scenarios_dir(r, resolve_path(base, args.scenarios_dir))

    if args.profile:
        path = resolve_path(base, args.profile)
        check_profile_yaml(r, path, base_dirs, scn, expect_as=args.application)

    if args.round_trip:
        if not args.scenario or not args.profile:
            print("[verify] FAIL: --round-trip требует --scenario и --profile", file=sys.stderr)
            sys.exit(2)
        if not scn:
            print("[verify] FAIL: для round-trip укажите --scenario-name (SCN)", file=sys.stderr)
            sys.exit(2)
        check_round_trip(
            r,
            resolve_path(base, args.scenario),
            resolve_path(base, args.profile),
            scn,
            base_dirs,
        )

    if args.compile:
        check_compile(r, base)

    r.summary()
    sys.exit(0 if r.passed() else 1)


if __name__ == "__main__":
    main()
