"""Сопоставить label из simulation.log с ключами count в profile.yaml.

Помогает заполнить/исправить count без ручного угадывания имён.
Предпочитает ключи по имени переменной Case (контракт profile.yaml).

Примеры (из gatling/gatlingScripts):

  python3 ltAuto/log_labels_to_profile.py \\
      --simulation_log /path/to/simulation.log \\
      --profile profiles/efsFinmonWeb/profile.yaml \\
      --application efsFinmonWeb

  # только labels из лога (без профиля):
  python3 ltAuto/log_labels_to_profile.py --simulation_log resources/simulation.log
"""

from __future__ import annotations

import argparse
import os
import sys
from collections import Counter

import yaml

_HERE = os.path.dirname(os.path.abspath(__file__))
if _HERE not in sys.path:
    sys.path.insert(0, _HERE)

from case_parser import parse_case_classes  # noqa: E402
from gatling_parser import build_var_to_log, collect_request_classes, read_simulation_log  # noqa: E402
from profile_paths import build_profile_base_dirs  # noqa: E402


def _load_profile(path: str | None) -> dict:
    if not path or not os.path.isfile(path):
        return {}
    with open(path, encoding="utf-8") as fh:
        return yaml.load(fh, Loader=yaml.FullLoader) or {}


def _resolve_count_keys(cfg: dict, profile_path: str | None) -> dict[str, str]:
    """{log_label: count_key_in_yaml}."""
    var_to_log = build_var_to_log(cfg, profile_path or "")
    log_to_var = {log: var for var, log in var_to_log.items()}
    resolved = {}
    for key in (cfg.get("count") or {}):
        label = var_to_log.get(key, key)
        resolved[label] = key
    return resolved, log_to_var, var_to_log


def main():
    ap = argparse.ArgumentParser(description="label из simulation.log ↔ count в profile.yaml")
    ap.add_argument("--simulation_log", required=True, help="путь к simulation.log")
    ap.add_argument("--profile", default="", help="profile.yaml (опционально)")
    ap.add_argument("--application", default="", help="имя АС для проверки каталога profiles/")
    ap.add_argument("--request-classes", nargs="*", default=[], help="override request_classes")
    ap.add_argument("--rampup", type=float, default=None, help="сек отрезать от начала (как в gatling_parser)")
    ap.add_argument("--json", action="store_true", help="вывод в JSON")
    args = ap.parse_args()

    df, run_info = read_simulation_log(args.simulation_log)
    if df.empty:
        print("FAIL: пустой simulation.log или нет REQUEST")
        sys.exit(1)

    if args.rampup and args.rampup > 0:
        min_ts = df["start"].min()
        df = df[df["start"] >= min_ts + args.rampup * 1000]

    ok_counts = Counter(df[df["success"]]["label"].tolist())
    all_labels = sorted(set(df["label"].tolist()))

    cfg = _load_profile(args.profile)
    if args.request_classes:
        cfg = dict(cfg)
        cfg["request_classes"] = args.request_classes

    profile_path = args.profile or None
    count_by_label, log_to_var, var_to_log = _resolve_count_keys(cfg, profile_path)

    missing_in_profile = []
    matched = []
    suggest_add = []

    for label in all_labels:
        count_key = count_by_label.get(label) or log_to_var.get(label, label)
        in_profile = label in count_by_label or count_key in (cfg.get("count") or {})
        entry = {
            "log_label": label,
            "suggested_count_key": log_to_var.get(label, label),
            "ok_requests": ok_counts.get(label, 0),
            "in_profile": in_profile,
        }
        if in_profile:
            matched.append(entry)
        else:
            missing_in_profile.append(entry)
            suggest_add.append(
                "  {}: 0    # TODO: из прогона OK={}".format(
                    log_to_var.get(label, label), ok_counts.get(label, 0)
                )
            )

    orphan_keys = []
    profile_count = cfg.get("count") or {}
    for key in profile_count:
        label = var_to_log.get(key, key)
        if label not in all_labels:
            orphan_keys.append({"count_key": key, "resolves_to_log": label})

    result = {
        "simulation_log": args.simulation_log,
        "profile": args.profile or None,
        "run_info": run_info,
        "labels_in_log": len(all_labels),
        "matched_in_profile": len(matched),
        "missing_in_profile": missing_in_profile,
        "orphan_in_profile": orphan_keys,
        "suggest_yaml_lines": suggest_add,
    }

    if args.application and args.profile:
        from profile_paths import as_name_from_profile_path  # noqa: E402

        as_name = as_name_from_profile_path(args.profile)
        if as_name and as_name != args.application:
            result["warn_application_mismatch"] = {
                "expected": args.application,
                "profile_dir": as_name,
            }

    if args.json:
        import json

        print(json.dumps(result, ensure_ascii=False, indent=2))
    else:
        print("=== log_labels_to_profile ===")
        print("log: {}  labels={}  matched={}  missing={}  orphan={}".format(
            args.simulation_log, len(all_labels), len(matched),
            len(missing_in_profile), len(orphan_keys),
        ))
        if missing_in_profile:
            print("\n--- missing in profile (добавить в count, одна строка за итерацию) ---")
            for e in missing_in_profile:
                print("  {}  key={}  OK={}".format(
                    e["log_label"], e["suggested_count_key"], e["ok_requests"]))
        if orphan_keys:
            print("\n--- orphan in profile (нет в логе) ---")
            for e in orphan_keys:
                print("  {} -> {}".format(e["count_key"], e["resolves_to_log"]))
        if suggest_add:
            print("\n--- suggest (вставлять по одному ключу, затем verify) ---")
            for line in suggest_add[:20]:
                print(line)
            if len(suggest_add) > 20:
                print("  ... ещё {} строк".format(len(suggest_add) - 20))

    has_issues = bool(missing_in_profile or orphan_keys)
    sys.exit(0 if not has_issues else 1)


if __name__ == "__main__":
    main()
