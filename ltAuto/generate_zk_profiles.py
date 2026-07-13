#!/usr/bin/env python3
"""Сгенерировать profile.yaml для каждого Case/Scenario-класса домена ZK.

Один профиль на контроллер swagger:
  profiles/ZK/<ScnName>/profile.yaml
  request_classes -> src/test/java/cases/ZK/<ScnName>Case.java

Веса и count берутся из randomSwitch в *Scenario.java (sim_to_profile.build_yaml).

Запуск из корня репозитория:
  python3 ltAuto/generate_zk_profiles.py
  python3 ltAuto/generate_zk_profiles.py --rps 10 --duration 3600
"""

from __future__ import annotations

import argparse
import glob
import os
import re
import sys

_HERE = os.path.dirname(os.path.abspath(__file__))
if _HERE not in sys.path:
    sys.path.insert(0, _HERE)

from sim_to_profile import build_yaml, parse_weights  # noqa: E402

_REPO_ROOT = os.path.dirname(_HERE)
_SCENARIOS_DIR = os.path.join(_REPO_ROOT, "gatling", "src", "test", "java", "scenarios", "ZK")
_CASES_REL = "src/test/java/cases/ZK"
_PROFILES_ROOT = os.path.join(_REPO_ROOT, "profiles", "ZK")


def scenario_name(text: str, class_name: str) -> str:
    m = re.search(r'scn\s*=\s*scenario\(\s*"([^"]+)"', text)
    if m:
        return m.group(1)
    return re.sub(r"(Scenarios|Scenario)$", "", class_name)


def case_rel_path(scn: str) -> str:
    return f"{_CASES_REL}/{scn}Case.java"


def generate_profile(
    scenario_path: str,
    *,
    rps: int,
    duration: int,
    rampup: int,
) -> tuple[str, str, int]:
    with open(scenario_path, encoding="utf-8") as fh:
        text = fh.read()

    class_m = re.search(r"public\s+class\s+(\w+)", text)
    if not class_m:
        raise ValueError(f"не найден class в {scenario_path}")
    class_name = class_m.group(1)
    scn = scenario_name(text, class_name)

    weights = parse_weights(text)
    if not weights:
        raise ValueError(f"нет WithWeight в {scenario_path}")

    case_path = case_rel_path(scn)
    yaml_text = build_yaml(
        weights,
        rps=rps,
        duration=duration,
        scn=scn,
        request_classes=[case_path],
        rampup=rampup,
    )
    yaml_text = yaml_text.replace(
        f'description: "Профиль НТ {scn} (auto из весов+RPS)"',
        f'description: "Профиль НТ ZK / {scn}"',
        1,
    )
    yaml_text = yaml_text.replace(
        "# Профиль сгенерирован sim_to_profile.py",
        "# Профиль ZK: один контроллер = один Case/Scenario-класс\n"
        "# Сгенерирован ltAuto/generate_zk_profiles.py",
        1,
    )
    return scn, yaml_text, len(weights)


def main():
    ap = argparse.ArgumentParser(description="Профили profiles/ZK/<Class>/profile.yaml")
    ap.add_argument("--rps", type=int, default=1, help="суммарный RPS на контроллер (заглушка)")
    ap.add_argument("--duration", type=int, default=600, help="injection.duration, сек")
    ap.add_argument("--rampup", type=int, default=60, help="injection.rampup, сек")
    ap.add_argument("--dry-run", action="store_true", help="только список, без записи")
    args = ap.parse_args()

    scenarios = sorted(glob.glob(os.path.join(_SCENARIOS_DIR, "*Scenario.java")))
    if not scenarios:
        print(f"Сценарии не найдены: {_SCENARIOS_DIR}", file=sys.stderr)
        sys.exit(1)

    written = 0
    total_requests = 0
    for path in scenarios:
        scn, yaml_text, n_req = generate_profile(
            path,
            rps=args.rps,
            duration=args.duration,
            rampup=args.rampup,
        )
        out_dir = os.path.join(_PROFILES_ROOT, scn)
        out_path = os.path.join(out_dir, "profile.yaml")
        if args.dry_run:
            print(f"  {scn}: {n_req} запросов -> {out_path}")
            continue
        os.makedirs(out_dir, exist_ok=True)
        with open(out_path, "w", encoding="utf-8") as fh:
            fh.write(yaml_text)
        written += 1
        total_requests += n_req

    if args.dry_run:
        print(f"dry-run: {len(scenarios)} профилей")
    else:
        print(f"Generated {written} profiles in profiles/ZK/ ({total_requests} UC total)")
        print(f"  RPS={args.rps}, duration={args.duration}s, rampup={args.rampup}s")


if __name__ == "__main__":
    main()
