"""Снять метаданные сценария/симуляции перед правками (для слабых ИИ-моделей).

Не полагайся на «прочитал Java глазами» — запускай этот скрипт и правь по JSON.

Примеры (из gatling/gatlingScripts):

  python3 ltAuto/dump_scenario_meta.py \\
      --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \\
      --request-classes src/test/java/cases/pprbSberrating

  python3 ltAuto/dump_scenario_meta.py \\
      --simulation src/test/java/simulations/pprbSberrating/All/OTT_all_debug.java
"""

from __future__ import annotations

import argparse
import json
import os
import re
import sys

_HERE = os.path.dirname(os.path.abspath(__file__))
if _HERE not in sys.path:
    sys.path.insert(0, _HERE)

from case_parser import parse_case_classes  # noqa: E402
from profile_paths import build_profile_base_dirs  # noqa: E402
from sim_to_profile import parse_weights  # noqa: E402

_SCN = re.compile(r'String\s+SCN\s*=\s*"([^"]+)"')
_CLASS = re.compile(r"public\s+class\s+(\w+)")
_INJECT = re.compile(
    r"(\w+Scenario(?:\.\w+)?)\s*\n\s*\.injectOpen\(\s*atOnceUsers\(\s*(\d+)\s*\)",
    re.MULTILINE,
)
_THROTTLE_RPS = re.compile(r"reachRps\(\s*(\d+)")
_THROTTLE_HOLD = re.compile(r"holdFor\(\s*(\d+)")


def _scenario_name(text: str, override: str | None) -> str:
    if override:
        return override
    m = _SCN.search(text)
    if m:
        return m.group(1)
    cm = _CLASS.search(text)
    if not cm:
        return "Unknown"
    return re.sub(r"(Scenarios|Scenario)$", "", cm.group(1))


def dump_scenario(path: str, scenario_name: str | None, request_classes: list[str]) -> dict:
    with open(path, encoding="utf-8") as fh:
        text = fh.read()
    weights = parse_weights(text)
    scn = _scenario_name(text, scenario_name)
    meta = {
        "file": path,
        "type": "scenario",
        "scn": scn,
        "weights": [{"var": k, "default_weight": v} for k, v in weights.items()],
        "weight_sum": sum(weights.values()),
        "has_profile_config": "ProfileConfig.getWeight" in text,
        "numeric_with_weight_left": bool(re.search(r"new\s+Choice\.WithWeight\(\s*\d+", text)),
    }
    if request_classes:
        try:
            var_to_log = parse_case_classes(
                request_classes, base_dirs=build_profile_base_dirs(path)
            )
            meta["case_vars"] = [
                {"var": var, "log_name": log} for var, log in sorted(var_to_log.items())
            ]
        except FileNotFoundError as e:
            meta["case_vars_error"] = str(e)
    return meta


def dump_simulation(path: str) -> dict:
    with open(path, encoding="utf-8") as fh:
        text = fh.read()
    blocks = []
    for m in _INJECT.finditer(text):
        ref = m.group(1)
        users = int(m.group(2))
        scn_guess = ref.split(".")[0].replace("Scenario", "")
        blocks.append({
            "line_ref": ref,
            "scn_guess": scn_guess,
            "inject_users_default": users,
            "suggested": (
                f'.injectOpen(atOnceUsers(ProfileConfig.getInjectUsers("{scn_guess}", {users})))'
            ),
        })
    return {
        "file": path,
        "type": "simulation",
        "inject_blocks": blocks,
        "inject_block_count": len(blocks),
        "throttle_rps": _THROTTLE_RPS.search(text).group(1) if _THROTTLE_RPS.search(text) else None,
        "throttle_hold_sec": _THROTTLE_HOLD.search(text).group(1) if _THROTTLE_HOLD.search(text) else None,
        "has_profile_config_import": "import config.ProfileConfig" in text,
    }


def main():
    ap = argparse.ArgumentParser(description="Метаданные сценария/симуляции -> JSON")
    ap.add_argument("--scenario", help="путь к *Scenario.java")
    ap.add_argument("--simulation", help="путь к Simulation.java")
    ap.add_argument("--scenario-name", help="override SCN")
    ap.add_argument("--request-classes", nargs="*", default=[], help="Case-каталоги для var↔log")
    ap.add_argument("--output", help="файл JSON (иначе stdout)")
    args = ap.parse_args()

    if not args.scenario and not args.simulation:
        ap.error("укажите --scenario и/или --simulation")

    out = {}
    if args.scenario:
        out["scenario"] = dump_scenario(args.scenario, args.scenario_name, args.request_classes)
    if args.simulation:
        out["simulation"] = dump_simulation(args.simulation)

    text = json.dumps(out, ensure_ascii=False, indent=2)
    if args.output:
        with open(args.output, "w", encoding="utf-8") as fh:
            fh.write(text)
        print("JSON -> {}".format(args.output))
    else:
        print(text)


if __name__ == "__main__":
    main()
