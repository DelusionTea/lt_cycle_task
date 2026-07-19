"""Трансляция spec.yaml -> profile.yaml (ручные спеки без Swagger).

Пример:
  python3 SDD/ltAuto/spec_to_profile.py --spec SDD/specs/ZK/ComplianceRequests/spec.yaml \
    --output SDD/profiles/ZK/ComplianceRequests/profile.spec.yaml
"""

from __future__ import annotations

import argparse
import os
import sys

import yaml


_REPO_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))


def load_spec(path: str) -> dict:
    with open(path, encoding="utf-8") as fh:
        return yaml.load(fh, Loader=yaml.FullLoader) or {}


def normalize_list(val):
    if val is None:
        return []
    if isinstance(val, list):
        return val
    return [val]


def build_profile(spec: dict, spec_path: str) -> dict:
    meta = spec.get("meta") or {}
    profile = spec.get("profile") or {}
    thresholds = profile.get("thresholds") or {}

    out = {}
    desc = profile.get("description") or meta.get("description")
    if desc:
        out["description"] = desc
    out["spec_ref"] = spec_path
    out["target_percent"] = profile.get("target_percent", 100)
    out["rampup"] = profile.get("rampup", 0)

    if "pct95" in thresholds:
        out["95pct"] = thresholds.get("pct95")
    if "pct50" in thresholds:
        out["50pct"] = thresholds.get("pct50")
    if "rps" in thresholds:
        out["rps"] = thresholds.get("rps")
    if "error_count" in thresholds:
        out["error_count"] = thresholds.get("error_count")

    out["request_classes"] = normalize_list(spec.get("request_classes"))
    out["count"] = profile.get("count") or {}
    out["sla_per_label"] = profile.get("sla_per_label") or {}
    out["injection"] = spec.get("injection") or {}
    return out


def default_output_path(spec: dict) -> str:
    meta = spec.get("meta") or {}
    service = meta.get("service", "unknown")
    component = meta.get("component", "spec")
    return os.path.join(_REPO_ROOT, "SDD", "profiles", service, component, "profile.spec.yaml")


def dump_yaml(data: dict, path: str):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as fh:
        yaml.safe_dump(data, fh, sort_keys=False, allow_unicode=True)


def main() -> int:
    ap = argparse.ArgumentParser(description="spec.yaml -> profile.yaml")
    ap.add_argument("--spec", required=True, help="путь к spec.yaml")
    ap.add_argument("--output", default=None, help="куда записать профиль")
    args = ap.parse_args()

    spec_path = os.path.abspath(args.spec)
    spec = load_spec(spec_path)
    output = os.path.abspath(args.output) if args.output else default_output_path(spec)

    profile = build_profile(spec, spec_path)
    dump_yaml(profile, output)
    print("[spec] profile written to {}".format(output))
    return 0


if __name__ == "__main__":
    sys.exit(main())
