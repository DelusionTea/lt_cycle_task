"""Codemod: хардкод весов randomSwitch -> ProfileConfig.getWeight(...).

Превращает в Gatling-сценариях
    new Choice.WithWeight(25, exec(LicensesCase.UC01_POST_Licenses_Summary))
    new Choice.WithWeight(96, UC01_POST_Licenses_Summary)
в
    new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01_POST_Licenses_Summary", 25), exec(LicensesCase.UC01_POST_Licenses_Summary))
    new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01_POST_Licenses_Summary", 96), UC01_POST_Licenses_Summary)

Правила (контракт с profile_to_props.py / ProfileConfig.java):
  - ключ choice в getWeight == ИМЯ ПЕРЕМЕННОЙ (после точки в exec(Case.VAR) или сам
    идентификатор для bare-варианта). Это же имя используется в profile.properties.
  - старый числовой вес сохраняется как ДЕФОЛТ (2-й аргумент getWeight).
  - SCN — константа `private static final String SCN = "..."`; если её нет, скрипт
    добавляет её (значение выводится из имени класса: XxxScenario -> "Xxx"),
    либо задайте явно через --scenario-name.
  - добавляется `import config.ProfileConfig;` при отсутствии.

Идемпотентность: строки, где вес уже обёрнут в ProfileConfig.getWeight, пропускаются.
Закомментированные строки (начинаются с //) не трогаются.
Многострочные Choice.WithWeight не поддерживаются (каждый Choice — на своей строке).

Использование:
    python3 ltAuto/weights_codemod.py <файл_или_каталог...> [--scenario-name Licenses] [--dry-run]

Без флага --dry-run изменения пишутся в файлы на месте (git — ваша страховка).
"""

import argparse
import glob
import os
import re
import sys

MARKER = "new Choice.WithWeight("
IMPORT_LINE = "import config.ProfileConfig;"
GETWEIGHT_PREFIX = "ProfileConfig.getWeight("


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


def derive_scn(text, override):
    if override:
        return override
    m = re.search(r'String\s+SCN\s*=\s*"([^"]+)"', text)
    if m:
        return None  # константа уже есть — используем её как SCN
    cm = re.search(r'public\s+class\s+(\w+)', text)
    if cm:
        name = cm.group(1)
        for suf in ("Scenarios", "Scenario"):
            if name.endswith(suf):
                return name[: -len(suf)]
        return name
    return "Default"


def split_choice_args(line, start):
    """От позиции сразу после '(' в WithWeight найти top-level запятую и закрытие."""
    depth = 1
    comma = -1
    i = start
    while i < len(line):
        c = line[i]
        if c == "(":
            depth += 1
        elif c == ")":
            depth -= 1
            if depth == 0:
                return comma, i
        elif c == "," and depth == 1 and comma == -1:
            comma = i
        i += 1
    return comma, -1


def var_from_target(arg2):
    arg2 = arg2.strip()
    m = re.fullmatch(r"exec\(\s*[\w.]*\.(\w+)\s*\)", arg2)
    if m:
        return m.group(1)
    m = re.fullmatch(r"exec\(\s*(\w+)\s*\)", arg2)
    if m:
        return m.group(1)
    m = re.fullmatch(r"(\w+)", arg2)
    if m:
        return m.group(1)
    return None


def transform_line(line, scn_ref):
    """Вернуть (новая_строка, число_замен, предупреждения[])."""
    if line.lstrip().startswith("//"):
        return line, 0, []
    warns = []
    count = 0
    pos = 0
    out = line
    search_from = 0
    while True:
        idx = out.find(MARKER, search_from)
        if idx == -1:
            break
        start = idx + len(MARKER)
        comma, close = split_choice_args(out, start)
        if comma == -1 or close == -1:
            search_from = start
            continue
        arg1 = out[start:comma].strip()
        arg2 = out[comma + 1 : close].strip()
        if arg1.startswith(GETWEIGHT_PREFIX):
            search_from = close + 1
            continue  # уже сделано
        if not re.fullmatch(r"\d+", arg1):
            warns.append("вес не число ('{}') — пропуск".format(arg1))
            search_from = close + 1
            continue
        var = var_from_target(arg2)
        if not var:
            warns.append("не удалось определить имя переменной из '{}' — пропуск".format(arg2))
            search_from = close + 1
            continue
        new_weight = '{}{}, "{}", {})'.format(GETWEIGHT_PREFIX, scn_ref, var, arg1)
        out = out[:start] + new_weight + ", " + arg2 + out[close:]
        count += 1
        search_from = start + len(new_weight) + 2 + len(arg2) + 1
    return out, count, warns


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
    return lines[:insert_at] + [IMPORT_LINE] + lines[insert_at:]


def ensure_scn_constant(lines, scn_value):
    text = "".join(lines)
    if re.search(r'String\s+SCN\s*=\s*"', text):
        return lines
    for i, l in enumerate(lines):
        m = re.search(r"public\s+class\s+\w+[^{]*\{", l)
        if m:
            indent = "    "
            const = '{}private static final String SCN = "{}";\n'.format(indent, scn_value)
            return lines[: i + 1] + ["\n", const] + lines[i + 1 :]
    return lines


def process_file(path, override, dry_run):
    with open(path, encoding="utf-8") as f:
        text = f.read()
    lines = text.splitlines(keepends=True)

    scn_const_needed = not re.search(r'String\s+SCN\s*=\s*"', text)
    derived = derive_scn(text, override)
    scn_value = derived if derived else None  # None => константа уже есть
    scn_ref = "SCN"

    total = 0
    all_warns = []
    new_lines = []
    for l in lines:
        nl, c, warns = transform_line(l, scn_ref)
        new_lines.append(nl)
        total += c
        all_warns.extend(warns)

    if total == 0:
        return 0, all_warns

    if scn_const_needed:
        val = scn_value if scn_value else (override or "Default")
        new_lines = ensure_scn_constant(new_lines, val)
    new_lines = ensure_import(new_lines)

    if not dry_run:
        with open(path, "w", encoding="utf-8") as f:
            f.write("".join(new_lines))
    return total, all_warns


def main():
    ap = argparse.ArgumentParser(description="Codemod весов randomSwitch -> ProfileConfig.getWeight")
    ap.add_argument("paths", nargs="+", help="файлы/каталоги/glob со сценариями .java")
    ap.add_argument("--scenario-name", default=None, help="значение SCN (по умолчанию из имени класса)")
    ap.add_argument("--dry-run", action="store_true", help="не писать файлы, только отчёт")
    args = ap.parse_args()

    files = find_java_files(args.paths)
    if not files:
        print("Не найдено .java по путям:", args.paths)
        sys.exit(1)

    grand = 0
    for path in files:
        changed, warns = process_file(path, args.scenario_name, args.dry_run)
        grand += changed
        if changed or warns:
            tag = "[dry-run] " if args.dry_run else ""
            print("{}{}: заменено {}".format(tag, path, changed))
            for w in warns:
                print("    WARN {}".format(w))
    print("Итого замен: {} в {} файле(ах){}".format(
        grand, len(files), " (dry-run, ничего не записано)" if args.dry_run else ""))


if __name__ == "__main__":
    main()
