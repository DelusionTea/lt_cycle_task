"""Генерация profile.yaml из весов сценария и целевого RPS (обратная задача).

Инверсия profile_to_props.py: там из count считаются веса; здесь из весов
randomSwitch и суммарного RPS (из throttle) считаются count по каждому запросу.

Формула:
    сумма_весов   = Σ w_i
    RPS_i         = RPS_total * w_i / сумма_весов
    count_i       = round(RPS_i * duration_сек)

Веса читаются из сценария в двух формах (хардкод и уже вынесенные в профиль):
    new Choice.WithWeight(25, exec(LicensesCase.UC01_POST_Licenses_Summary))
    new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01_POST_Licenses_Summary", 25), ...)
Ключ count — ИМЯ ПЕРЕМЕННОЙ (совпадает с ключом веса и с именем в Case-классе).
Закомментированные строки игнорируются.

RPS и длительность: берутся из CLI (--rps/--duration) либо парсятся из throttle
(reachRps(...) / holdFor(...)) в файле симуляции (--simulation). CLI имеет приоритет.

Использование:
    python3 ltAuto/sim_to_profile.py \
        --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
        --rps 200 --duration 600 \
        --scenario-name Licenses \
        --request-classes src/test/java/cases/pprbSberrating \
        --output profiles/pprbSberrating/profile.generated.yaml
"""

import argparse
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from weights_codemod import MARKER, split_choice_args, var_from_target  # noqa: E402


def parse_weights(text):
    """{имя_переменной: вес(int)} по порядку. Поддержаны хардкод и getWeight."""
    weights = {}
    for line in text.splitlines():
        if line.lstrip().startswith("//"):
            continue
        search_from = 0
        while True:
            idx = line.find(MARKER, search_from)
            if idx == -1:
                break
            start = idx + len(MARKER)
            comma, close = split_choice_args(line, start)
            if comma == -1 or close == -1:
                break
            arg1 = line[start:comma].strip()
            arg2 = line[comma + 1 : close].strip()
            search_from = close + 1
            gw = re.search(r'getWeight\(\s*[\w".]+\s*,\s*"([^"]+)"\s*,\s*([\d.]+)', arg1)
            if gw:
                weights[gw.group(1)] = int(float(gw.group(2)))
                continue
            if re.fullmatch(r"\d+", arg1):
                var = var_from_target(arg2)
                if var:
                    weights[var] = int(arg1)
    return weights


def parse_number(pattern, text):
    m = re.search(pattern, text)
    return int(m.group(1)) if m else None


def build_yaml(weights, rps, duration, scn, request_classes, rampup):
    total_w = sum(weights.values())
    lines = []
    lines.append("# Профиль сгенерирован sim_to_profile.py")
    lines.append("# RPS_total={}, duration={}s, сумма весов={}".format(rps, duration, total_w))
    lines.append('description: "Профиль НТ {} (auto из весов+RPS)"'.format(scn))
    lines.append("target_percent: 100")
    lines.append("rampup: 0")
    lines.append("")
    lines.append("# TODO: задайте реальные SLA по времени отклика/ошибкам")
    lines.append("95pct: 1000")
    lines.append("50pct: 500")
    lines.append("rps: {}".format(rps))
    lines.append("error_count: 100")
    lines.append("")
    if request_classes:
        lines.append("request_classes:")
        for rc in request_classes:
            lines.append("  - {}".format(rc))
        lines.append("")
    lines.append("# count_i = round(RPS_total * w_i / Σw * duration)")
    lines.append("count:")
    for var, w in weights.items():
        rps_i = rps * w / total_w
        count_i = round(rps_i * duration)
        lines.append("  {}: {}    # вес {} -> {:.2f} rps".format(var, count_i, w, rps_i))
    lines.append("")
    lines.append("sla_per_label: {}")
    lines.append("")
    lines.append("injection:")
    lines.append("  duration: {}".format(duration))
    lines.append("  rampup: {}".format(rampup))
    lines.append("  scenarios:")
    lines.append("    {}:".format(scn))
    lines.append("      users: {}    # стартовая оценка ~ RPS_total; выверьте под open-модель".format(rps))
    if request_classes:
        lines.append("      request_classes:")
        for rc in request_classes:
            lines.append("        - {}".format(rc))
    return "\n".join(lines) + "\n"


def main():
    ap = argparse.ArgumentParser(description="Веса + RPS из throttle -> profile.yaml (count/injection)")
    ap.add_argument("--scenario", required=True, help="путь к .java сценарию с randomSwitch")
    ap.add_argument("--simulation", default=None, help="путь к .java симуляции (для парсинга throttle)")
    ap.add_argument("--rps", type=int, default=None, help="суммарный целевой RPS (иначе из throttle)")
    ap.add_argument("--duration", type=int, default=None, help="длительность hold, сек (иначе из throttle)")
    ap.add_argument("--rampup", type=int, default=60, help="разгон, сек (для injection)")
    ap.add_argument("--scenario-name", default=None, help="SCN (по умолчанию из имени класса)")
    ap.add_argument("--request-classes", nargs="*", default=[], help="пути к Case-классам/каталогам для профиля")
    ap.add_argument("--output", default="profile.generated.yaml")
    args = ap.parse_args()

    with open(args.scenario, encoding="utf-8") as f:
        scn_text = f.read()
    weights = parse_weights(scn_text)
    if not weights:
        print("Не найдено весов Choice.WithWeight в", args.scenario)
        sys.exit(1)

    scn = args.scenario_name
    if not scn:
        cm = re.search(r"public\s+class\s+(\w+)", scn_text)
        name = cm.group(1) if cm else "Default"
        scn = re.sub(r"(Scenarios|Scenario)$", "", name)

    rps = args.rps
    duration = args.duration
    if (rps is None or duration is None) and args.simulation:
        with open(args.simulation, encoding="utf-8") as f:
            sim_text = f.read()
        if rps is None:
            rps = parse_number(r"reachRps\(\s*(\d+)", sim_text)
        if duration is None:
            duration = parse_number(r"holdFor\(\s*(\d+)", sim_text) or parse_number(r"during\(\s*(\d+)", sim_text)

    if rps is None or duration is None:
        print("Не заданы RPS/duration и не удалось распарсить из throttle. Укажите --rps и --duration.")
        sys.exit(2)

    yaml_text = build_yaml(weights, rps, duration, scn, args.request_classes, args.rampup)
    with open(args.output, "w", encoding="utf-8") as f:
        f.write(yaml_text)

    print("Сценарий: {}  SCN={}  RPS={}  duration={}s".format(args.scenario, scn, rps, duration))
    print("Найдено запросов: {} (сумма весов {})".format(len(weights), sum(weights.values())))
    print("Профиль записан -> {}".format(args.output))
    print("\n--- превью ---")
    print(yaml_text)


if __name__ == "__main__":
    main()
