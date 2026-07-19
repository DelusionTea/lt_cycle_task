"""Заполнить profile.count нулями по всем запросам спеки.

Использование:
  python3 SDD/ltAuto/spec_fill_count.py --spec SDD/specs/<АС>/<Component>/spec.yaml

Поведение:
- Если profile.count отсутствует или пустой, заполняет ключи нулями.
- Ключи берутся из:
  1) endpoints[].id (если есть)
  2) request_classes (Case-классы, var-имена)
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

from case_parser import parse_case_classes  # noqa: E402


def load_spec(path: str) -> dict:
    with open(path, encoding="utf-8") as fh:
        return yaml.load(fh, Loader=yaml.FullLoader) or {}


def dump_spec(data: dict, path: str):
    with open(path, "w", encoding="utf-8") as fh:
        yaml.safe_dump(data, fh, sort_keys=False, allow_unicode=True)


def resolve_request_ids(spec: dict) -> list[str]:
    endpoints = spec.get("endpoints") or []
    ids = [e.get("id") for e in endpoints if e.get("id")]
    if ids:
        return ids

    req_classes = spec.get("request_classes") or []
    if not req_classes:
        return []

    base_dirs = [_REPO_ROOT, os.getcwd()]
    mapping = parse_case_classes(req_classes, base_dirs=base_dirs)
    return sorted(mapping.keys())


def main() -> int:
    ap = argparse.ArgumentParser(description="Заполнить profile.count нулями")
    ap.add_argument("--spec", required=True, help="путь к spec.yaml")
    args = ap.parse_args()

    spec_path = os.path.abspath(args.spec)
    spec = load_spec(spec_path)

    profile = spec.get("profile") or {}
    count = profile.get("count") or {}
    if count:
        print("[spec] count уже заполнен ({} ключей)".format(len(count)))
        return 0

    ids = resolve_request_ids(spec)
    if not ids:
        print("[spec] FAIL: не удалось определить список запросов", file=sys.stderr)
        return 1

    profile["count"] = {rid: 0 for rid in ids}
    spec["profile"] = profile
    dump_spec(spec, spec_path)
    print("[spec] count заполнен нулями ({} ключей): {}".format(len(ids), spec_path))
    return 0


if __name__ == "__main__":
    sys.exit(main())
