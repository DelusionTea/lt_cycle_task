#!/usr/bin/env python3
"""Жёсткие gate-проверки для ревью Gatling (gatling-reviewer skill).

Запускай из корня репозитория или gatling/gatlingScripts:

  python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py --module pprbSberrating
  python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py --files src/test/java/cases/foo/BarCase.java
  python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py --module efsFinmonWeb --profile profiles/efsFinmonWeb/profile.yaml --strict

Exit code: 0 = PASS, 1 = FAIL.
"""

from __future__ import annotations

import argparse
import glob
import json
import os
import re
import subprocess
import sys

# ltAuto и корень репозитория (скрипт в gigacode/skills/gatling-reviewer/scripts/)
_SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))


def _find_repo_root() -> str:
    d = _SCRIPT_DIR
    for _ in range(8):
        if os.path.isdir(os.path.join(d, "ltAuto")) or os.path.isdir(
            os.path.join(d, "gigacode", "skills")
        ):
            return d
        parent = os.path.dirname(d)
        if parent == d:
            break
        d = parent
    return os.path.abspath(os.path.join(_SCRIPT_DIR, "..", "..", "..", ".."))


_REPO_ROOT = _find_repo_root()


def _find_base_dir(explicit: str | None) -> str:
    if explicit:
        return os.path.abspath(explicit)
    for cand in [
        os.getcwd(),
        os.path.join(os.getcwd(), "gatling", "gatlingScripts"),
        _REPO_ROOT,
    ]:
        if os.path.isfile(os.path.join(cand, "pom.xml")):
            return cand
    return os.getcwd()


def _ltauto_dir(base: str) -> str | None:
    for p in [
        os.path.join(base, "ltAuto"),
        os.path.join(_REPO_ROOT, "ltAuto"),
        os.path.join(_REPO_ROOT, "gatling", "gatlingScripts", "ltAuto"),
    ]:
        p = os.path.normpath(p)
        if os.path.isdir(p):
            return p
    return None


class Reporter:
    def __init__(self, strict: bool):
        self.strict = strict
        self.ok = self.fail = self.warn = 0

    def _out(self, tag: str, msg: str):
        print("[review-gate] {}: {}".format(tag, msg))

    def ok_msg(self, msg: str):
        self.ok += 1
        self._out("OK", msg)

    def fail_msg(self, msg: str):
        self.fail += 1
        self._out("FAIL", msg)

    def warn_msg(self, msg: str):
        self.warn += 1
        self._out("WARN", msg)

    def passed(self) -> bool:
        extra = self.warn if self.strict else 0
        return self.fail + extra == 0

    def summary(self):
        status = "PASS" if self.passed() else "FAIL"
        print("---")
        print("RESULT: {} ({} ok, {} fail, {} warn)".format(
            status, self.ok, self.fail, self.warn))
        if self.strict and self.warn:
            print("(--strict: WARN = FAIL)")


def collect_java_files(base: str, module: str | None, files: list[str]) -> list[str]:
    if files:
        out = []
        for f in files:
            p = f if os.path.isabs(f) else os.path.join(base, f)
            if os.path.isfile(p):
                out.append(os.path.normpath(p))
            else:
                print("[review-gate] WARN: файл не найден: {}".format(p), file=sys.stderr)
        return sorted(set(out))
    if not module:
        return []
    patterns = [
        os.path.join(base, "src/test/java/cases", module, "**", "*.java"),
        os.path.join(base, "src/test/java/scenarios", "**", module, "**", "*.java"),
        os.path.join(base, "src/test/java/scenarios", module, "**", "*.java"),
        os.path.join(base, "src/test/java/simulations", "**", module, "**", "*.java"),
        os.path.join(base, "src/test/java/feeders", module, "**", "*.java"),
    ]
    found = []
    for pat in patterns:
        found.extend(glob.glob(pat, recursive=True))
    return sorted(set(found))


def gate_compile(r: Reporter, base: str, skip: bool):
    if skip:
        r.warn_msg("mvn test-compile пропущен (--skip-compile)")
        return
    if not os.path.isfile(os.path.join(base, "pom.xml")):
        r.warn_msg("pom.xml не найден — compile пропущен")
        return
    try:
        proc = subprocess.run(
            ["mvn", "-q", "-DskipTests", "test-compile"],
            cwd=base,
            capture_output=True,
            text=True,
            timeout=600,
        )
    except FileNotFoundError:
        r.fail_msg("mvn не найден в PATH")
        return
    except subprocess.TimeoutExpired:
        r.fail_msg("mvn test-compile: timeout")
        return
    if proc.returncode == 0:
        r.ok_msg("mvn test-compile")
    else:
        r.fail_msg("mvn test-compile exit={}".format(proc.returncode))
        for line in (proc.stderr or proc.stdout or "").strip().splitlines()[-10:]:
            print("           ", line)


def gate_verify_profile(r: Reporter, ltauto: str, base: str, args, java_files: list[str]):
    verify = os.path.join(ltauto, "verify_profile.py")
    if not os.path.isfile(verify):
        r.warn_msg("verify_profile.py не найден — пропуск NT-gate")
        return

    scenarios = [f for f in java_files if "/scenarios/" in f.replace("\\", "/") and f.endswith(".java")]
    if args.module and not scenarios:
        scn_dir = os.path.join(base, "src/test/java/scenarios", args.module)
        if os.path.isdir(scn_dir):
            subprocess_args = [
                sys.executable, verify,
                "--scenarios-dir", scn_dir,
                "--base-dir", base,
            ]
            if args.strict:
                subprocess_args.append("--strict")
            proc = subprocess.run(subprocess_args, capture_output=True, text=True)
            print(proc.stdout, end="")
            if proc.stderr:
                print(proc.stderr, file=sys.stderr, end="")
            if proc.returncode != 0:
                r.fail_msg("verify_profile --scenarios-dir {}".format(args.module))
            else:
                r.ok_msg("verify_profile --scenarios-dir {}".format(args.module))

    for scn_file in scenarios:
        cmd = [sys.executable, verify, "--scenario", scn_file, "--base-dir", base]
        if args.compile:
            cmd.append("--compile")
        if args.strict:
            cmd.append("--strict")
        proc = subprocess.run(cmd, capture_output=True, text=True)
        print(proc.stdout, end="")
        if proc.returncode != 0:
            r.fail_msg("verify_profile --scenario {}".format(scn_file))
        else:
            r.ok_msg("verify_profile --scenario {}".format(os.path.basename(scn_file)))

    if args.profile:
        prof = args.profile if os.path.isabs(args.profile) else os.path.join(base, args.profile)
        cmd = [sys.executable, verify, "--profile", prof, "--base-dir", base]
        if args.application:
            cmd.extend(["--application", args.application])
        if args.strict:
            cmd.append("--strict")
        proc = subprocess.run(cmd, capture_output=True, text=True)
        print(proc.stdout, end="")
        if proc.returncode != 0:
            r.fail_msg("verify_profile --profile")
        else:
            r.ok_msg("verify_profile --profile")


def _strip_line_comment(line: str) -> str:
    return re.sub(r"//.*", "", line)


def gate_security(r: Reporter, java_files: list[str]):
    secret_patterns = [
        (re.compile(r'(?i)(password|passwd|secret|api[_-]?key|jwt)\s*=\s*"[^"]{3,}"'), "hardcoded secret"),
        (re.compile(r'(?i)"password"\s*:\s*"[^"]+"'), "password in JSON string literal"),
    ]
    for path in java_files:
        norm = path.replace("\\", "/")
        in_cases_scenarios = "/cases/" in norm or "/scenarios/" in norm
        lines = open(path, encoding="utf-8", errors="replace").read().splitlines()
        for i, raw in enumerate(lines, 1):
            line = _strip_line_comment(raw)
            prev = lines[i - 2] if i > 1 else ""
            for pat, kind in secret_patterns:
                if not pat.search(line):
                    continue
                if in_cases_scenarios:
                    r.fail_msg("[{}:{}] {} в Cases/Scenarios запрещено".format(
                        os.path.basename(path), i, kind))
                elif "DEBUG ONLY" not in raw and "DEBUG ONLY" not in prev:
                    r.warn_msg("[{}:{}] {} — добавьте // DEBUG ONLY (feeders)".format(
                        os.path.basename(path), i, kind))
                break


def gate_cases_checks(r: Reporter, java_files: list[str]):
    case_files = [f for f in java_files if "/cases/" in f.replace("\\", "/")]
    http_def = re.compile(r"=\s*http\s*\(")
    check_re = re.compile(r"\.check\s*\(\s*status\s*\(")
    for path in case_files:
        text = open(path, encoding="utf-8", errors="replace").read()
        if "extends Methods" not in text and "class " in text and "Case" in os.path.basename(path):
            r.fail_msg("[{}] Case не extends Methods".format(os.path.basename(path)))
        lines = text.splitlines()
        for i, line in enumerate(lines, 1):
            if line.lstrip().startswith("//"):
                continue
            if http_def.search(line):
                chunk = "\n".join(lines[i - 1 : min(i + 12, len(lines))])
                if not check_re.search(chunk):
                    r.fail_msg("[{}:{}] http(...) без .check(status())".format(
                        os.path.basename(path), i))


def gate_json_resources(r: Reporter, base: str, java_files: list[str]):
    el_body = re.compile(r'ElFileBody\s*\(\s*([^+)]+)\s*\+?\s*"([^"]+\.json)"\s*\)')
    jsons_path = re.compile(r'JSONS_PATH\s*=\s*"([^"]+)"')
    res_root = os.path.join(base, "src/test/resources")
    if not os.path.isdir(res_root):
        r.warn_msg("src/test/resources не найден — JSON gate пропущен")
        return
    for path in java_files:
        if "/cases/" not in path.replace("\\", "/"):
            continue
        text = open(path, encoding="utf-8", errors="replace").read()
        base_json = None
        m = jsons_path.search(text)
        if m:
            base_json = m.group(1)
        for m in el_body.finditer(text):
            prefix = m.group(1).strip()
            fname = m.group(2)
            if prefix == "JSONS_PATH":
                rel = (base_json or "") + fname
            elif "JSONS_PATH" in prefix:
                rel = (base_json or "") + fname
            else:
                rel = fname.strip("/")
            full = os.path.join(res_root, rel.replace("/", os.sep))
            if not os.path.isfile(full):
                r.fail_msg("[{}] JSON не найден: src/test/resources/{}".format(
                    os.path.basename(path), rel))
            else:
                try:
                    with open(full, encoding="utf-8") as jf:
                        json.load(jf)
                    r.ok_msg("JSON valid: {}".format(rel))
                except json.JSONDecodeError as e:
                    r.fail_msg("[{}] невалидный JSON {}: {}".format(os.path.basename(path), rel, e))


def gate_simulation_patterns(r: Reporter, java_files: list[str]):
    sim_files = [f for f in java_files if "/simulations/" in f.replace("\\", "/")]
    for path in sim_files:
        text = open(path, encoding="utf-8", errors="replace").read()
        name = os.path.basename(path)
        if "extends Simulation" not in text:
            r.fail_msg("[{}] не extends Simulation".format(name))
        if "setUp(" not in text:
            r.fail_msg("[{}] нет setUp()".format(name))
        if "injectOpen" not in text and "injectClosed" not in text:
            r.warn_msg("[{}] нет injectOpen/injectClosed".format(name))
        if "protocols(" not in text:
            r.fail_msg("[{}] нет .protocols()".format(name))
        if "atOnceUsers(" in text and "ProfileConfig.getInjectUsers" not in text:
            if re.search(r"atOnceUsers\(\s*\d+\s*\)", text):
                r.warn_msg("[{}] хардкод atOnceUsers(N) — рекомендуется inject_codemod.py".format(name))


def main():
    ap = argparse.ArgumentParser(description="Gate-проверки gatling-reviewer")
    ap.add_argument("--base-dir", default=None, help="корень gatlingScripts")
    ap.add_argument("--module", default=None, help="имя АС, напр. pprbSberrating")
    ap.add_argument("--files", nargs="*", default=[], help="конкретные .java для проверки")
    ap.add_argument("--profile", default=None, help="profiles/<АС>/profile.yaml")
    ap.add_argument("--application", default=None, help="имя АС для verify profile")
    ap.add_argument("--skip-compile", action="store_true", help="не запускать mvn")
    ap.add_argument("--compile", action="store_true", help="verify_profile с --compile")
    ap.add_argument("--strict", action="store_true", help="WARN = FAIL")
    ap.add_argument("--quick", action="store_true", help="только compile + verify (без JSON/security)")
    args = ap.parse_args()

    if not args.module and not args.files:
        ap.error("укажите --module <АС> или --files <путь.java ...>")

    base = _find_base_dir(args.base_dir)
    ltauto = _ltauto_dir(base)
    java_files = collect_java_files(base, args.module, args.files)

    if not java_files and args.module:
        print("[review-gate] WARN: не найдено .java для module={}".format(args.module))

    r = Reporter(strict=args.strict)

    gate_compile(r, base, args.skip_compile)
    if ltauto:
        gate_verify_profile(r, ltauto, base, args, java_files)
    else:
        r.warn_msg("ltAuto/ не найден — verify_profile пропущен")

    if not args.quick:
        if java_files:
            gate_security(r, java_files)
            gate_cases_checks(r, java_files)
            gate_json_resources(r, base, java_files)
            gate_simulation_patterns(r, java_files)
        elif args.module:
            r.warn_msg("--quick не задан, но нет java_files для static gates")

    r.summary()
    sys.exit(0 if r.passed() else 1)


if __name__ == "__main__":
    main()
