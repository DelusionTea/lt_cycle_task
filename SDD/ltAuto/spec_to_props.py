"""spec.yaml -> profile.properties (без profile.spec.yaml).

Использование:
  python3 SDD/ltAuto/spec_to_props.py \
    --spec SDD/specs/<АС>/<Component>/spec.yaml \
    --target_percent 100 \
    --output profile.properties
"""

from __future__ import annotations

import argparse
import os
import sys

import yaml

_REPO_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
_LTAUTO = os.path.join(_REPO_ROOT, "ltAuto")
if _LTAUTO not in sys.path:
    sys.path.insert(0, _LTAUTO)

from profile_to_props import build_properties  # noqa: E402


def load_spec(path: str) -> dict:
    with open(path, encoding="utf-8") as fh:
        return yaml.load(fh, Loader=yaml.FullLoader) or {}


def normalize_list(val):
    if val is None:
        return []
    if isinstance(val, list):
        return val
    return [val]


def build_profile_from_spec(spec: dict) -> dict:
    profile = spec.get("profile") or {}
    return {
        "target_percent": profile.get("target_percent", 100),
        "rampup": profile.get("rampup", 0),
        "request_classes": normalize_list(spec.get("request_classes")),
        "count": profile.get("count") or {},
        "sla_per_label": profile.get("sla_per_label") or {},
        "injection": spec.get("injection") or {},
    }


def build_base_dirs(spec: dict) -> list[str]:
    meta = spec.get("meta") or {}
    service = meta.get("service")
    roots = [
        _REPO_ROOT,
        os.getcwd(),
        os.path.join(_REPO_ROOT, "gatling"),
        os.path.join(_REPO_ROOT, "gatling", "gatlingScripts"),
    ]
    if service:
        roots.extend([
            os.path.join(_REPO_ROOT, "gatling", "src", "test", "java", "cases", service),
            os.path.join(_REPO_ROOT, "gatling", "gatlingScripts", "src", "test", "java", "cases", service),
        ])
    return list(dict.fromkeys(r for r in roots if r))


def main() -> int:
    ap = argparse.ArgumentParser(description="spec.yaml -> profile.properties")
    ap.add_argument("--spec", required=True, help="путь к spec.yaml")
    ap.add_argument("--target_percent", type=float, default=None, help="целевой %% профиля")
    ap.add_argument("--output", default="profile.properties", help="файл properties")
    args = ap.parse_args()

    spec_path = os.path.abspath(args.spec)
    spec = load_spec(spec_path)
    profile = build_profile_from_spec(spec)

    target_percent = args.target_percent
    if target_percent is None:
        target_percent = float(profile.get("target_percent", 100))

    props = build_properties(profile, target_percent, build_base_dirs(spec))
    with open(args.output, "w", encoding="utf-8") as fh:
        fh.write(props)

    print("[spec] properties written to {}".format(args.output))
    return 0


if __name__ == "__main__":
    sys.exit(main())
