"""Codemod: хардкод injectOpen(atOnceUsers(N)) -> ProfileConfig.getInjectUsers(SCN, N).

Превращает в Simulation.java
    LicensesScenario.scn_ott_debug
        .injectOpen(atOnceUsers(5))
в
    LicensesScenario.scn_ott_debug
        .injectOpen(atOnceUsers(ProfileConfig.getInjectUsers("Licenses", 5)))

Правила:
  - SCN берётся из константы String SCN в *Scenario.java (если найден через
    --scenarios-dir), иначе из имени класса: LicensesScenario -> "Licenses";
    для поля scn_ott_debug_1 у StatisticsScenario -> "Statistics_1".
  - Переопределение: --scn-map LicensesScenario=Licenses или JSON-файл.
  - Старый N сохраняется как дефолт getInjectUsers.
  - добавляется `import config.ProfileConfig;` при отсутствии.

Идемпотентность: уже обёрнутые getInjectUsers пропускаются.
Закомментированные строки не трогаются.

Режим one-file-at-a-time:
  --only-scenario LicensesScenario   только один Scenario-класс
  --only-field scn_ott_debug         только одно поле сценария
  --limit 1                          максимум N замен за запуск

Использование:
    python3 ltAuto/inject_codemod.py <Simulation.java> [--dry-run]
    python3 ltAuto/inject_codemod.py simulations/.../OTT_all_debug.java \\
        --scenarios-dir src/test/java/scenarios --only-scenario LicensesScenario --limit 1
"""

from __future__ import annotations

import argparse
import glob
import json
import os
import re
import sys

IMPORT_LINE = "import config.ProfileConfig;"
GETINJECT_PREFIX = "ProfileConfig.getInjectUsers("

# ScenarioClass.field \n whitespace .injectOpen(atOnceUsers(N))
_INJECT_BLOCK = re.compile(
    r"(?P<class>\w+Scenario)\.(?P<field>\w+)\s*\n"
    r"(?P<indent>\s*)\.injectOpen\(\s*atOnceUsers\(\s*(?P<users>\d+)\s*\)\s*\)",
    re.MULTILINE,
)

# Однострочный вариант (редко)
_INJECT_ONE_LINE = re.compile(
    r"(?P<class>\w+Scenario)\.(?P<field>\w+)\s*"
    r"\.injectOpen\(\s*atOnceUsers\(\s*(?P<users>\d+)\s*\)\s*\)",
)


def find_java_files(paths):
    out = []
    for p in paths:
        if os.path.isdir(p):
            out.extend(sorted(glob.glob(os.path.join(p, "**", "*.java"), recursive=True)))
        elif p.endswith(".java") and os.path.isfile(p):
            out.append(p)
        else:
            out.extend(sorted(glob.glob(p, recursive=True)))
    return list(dict.fromkeys(out))


def load_scn_map(items, json_path):
    mapping = {}
    if json_path:
        with open(json_path, encoding="utf-8") as fh:
            data = json.load(fh)
        if not isinstance(data, dict):
            raise SystemExit("SCN map JSON must be an object")
        mapping.update({str(k): str(v) for k, v in data.items()})
    for item in items or []:
        if "=" not in item:
            raise SystemExit("SCN map entry must be ClassName=SCN, got: {}".format(item))
        k, v = item.split("=", 1)
        mapping[k.strip()] = v.strip()
    return mapping


def find_scenario_file(scenarios_dir, class_name):
    if not scenarios_dir or not os.path.isdir(scenarios_dir):
        return None
    pattern = os.path.join(scenarios_dir, "**", class_name + ".java")
    hits = sorted(glob.glob(pattern, recursive=True))
    return hits[0] if hits else None


def read_scn_constant(scenario_path):
    if not scenario_path:
        return None
    with open(scenario_path, encoding="utf-8") as fh:
        text = fh.read()
    m = re.search(r'String\s+SCN\s*=\s*"([^"]+)"', text)
    return m.group(1) if m else None


def derive_scn(class_name, field_name, scn_map, scenarios_dir):
    if class_name in scn_map:
        return scn_map[class_name]
    from_file = read_scn_constant(find_scenario_file(scenarios_dir, class_name))
    if from_file:
        return from_file
    base = class_name
    if base.endswith("Scenario"):
        base = base[: -len("Scenario")]
    m = re.search(r"_(\d+)$", field_name)
    if m:
        return "{}_{}".format(base, m.group(1))
    return base


def transform_text(
    text,
    scn_map,
    scenarios_dir,
    only_scenario,
    only_field,
    limit,
):
    warns = []
    count = 0
    remaining = limit if limit is not None else None

    def should_process(class_name, field_name):
        if only_scenario and class_name != only_scenario:
            return False
        if only_field and field_name != only_field:
            return False
        if remaining is not None and remaining <= 0:
            return False
        return True

    def replacer(match):
        nonlocal count, remaining
        class_name = match.group("class")
        field_name = match.group("field")
        users = match.group("users")
        if not should_process(class_name, field_name):
            return match.group(0)
        scn = derive_scn(class_name, field_name, scn_map, scenarios_dir)
        count += 1
        if remaining is not None:
            remaining -= 1
        inject_expr = "ProfileConfig.getInjectUsers(\"{}\", {})".format(scn, users)
        if match.re is _INJECT_BLOCK:
            return (
                "{class_name}.{field_name}\n"
                "{indent}.injectOpen(atOnceUsers({inject}))"
            ).format(
                class_name=class_name,
                field_name=field_name,
                indent=match.group("indent"),
                inject=inject_expr,
            )
        return "{class_name}.{field_name}.injectOpen(atOnceUsers({inject}))".format(
            class_name=class_name,
            field_name=field_name,
            inject=inject_expr,
        )

    new_text = _INJECT_BLOCK.sub(replacer, text)
    new_text = _INJECT_ONE_LINE.sub(replacer, new_text)
    return new_text, count, warns


def ensure_import(lines):
    if any(l.strip() == IMPORT_LINE for l in lines):
        return lines
    last_import = -1
    pkg = -1
    for i, l in enumerate(lines):
        if l.strip().startswith("import "):
            last_import = i
        elif l.strip().startswith("package "):
            pkg = i
    insert_at = last_import + 1 if last_import >= 0 else (pkg + 1 if pkg >= 0 else 0)
    return lines[:insert_at] + [IMPORT_LINE + "\n"] + lines[insert_at:]


def process_file(path, args):
    with open(path, encoding="utf-8") as fh:
        text = fh.read()

    scn_map = load_scn_map(args.scn_map, args.scn_map_json)
    new_text, count, warns = transform_text(
        text,
        scn_map,
        args.scenarios_dir,
        args.only_scenario,
        args.only_field,
        args.limit,
    )

    if count == 0:
        return 0, warns

    lines = new_text.splitlines(keepends=True)
    if "ProfileConfig.getInjectUsers" in new_text:
        lines = ensure_import(lines)
        new_text = "".join(lines)

    if not args.dry_run:
        with open(path, "w", encoding="utf-8") as fh:
            fh.write(new_text)
    return count, warns


def main():
    ap = argparse.ArgumentParser(
        description="Codemod injectOpen(atOnceUsers(N)) -> ProfileConfig.getInjectUsers"
    )
    ap.add_argument("paths", nargs="+", help="Simulation.java или каталог")
    ap.add_argument("--scenarios-dir", default="src/test/java/scenarios",
                    help="корень scenarios/ для чтения SCN из *Scenario.java")
    ap.add_argument("--scn-map", action="append", default=[],
                    help='переопределение SCN: LicensesScenario=Licenses')
    ap.add_argument("--scn-map-json", default=None, help='JSON {"LicensesScenario": "Licenses"}')
    ap.add_argument("--only-scenario", default=None,
                    help="только блоки этого класса (LicensesScenario)")
    ap.add_argument("--only-field", default=None,
                    help="только это поле (scn_ott_debug)")
    ap.add_argument("--limit", type=int, default=None,
                    help="макс. число замен за запуск (1 для one-file-at-a-time)")
    ap.add_argument("--dry-run", action="store_true", help="не писать файлы")
    args = ap.parse_args()

    files = find_java_files(args.paths)
    if not files:
        print("Не найдено .java по путям:", args.paths)
        sys.exit(1)

    grand = 0
    for path in files:
        changed, warns = process_file(path, args)
        grand += changed
        if changed or warns:
            tag = "[dry-run] " if args.dry_run else ""
            print("{}{}: заменено {}".format(tag, path, changed))
            for w in warns:
                print("    WARN {}".format(w))
    print("Итого замен: {} в {} файле(ах){}".format(
        grand, len(files), " (dry-run, ничего не записано)" if args.dry_run else ""))
    if grand == 0:
        sys.exit(0)


if __name__ == "__main__":
    main()
