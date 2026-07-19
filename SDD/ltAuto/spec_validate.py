"""Проверка соответствия spec.yaml и Gatling Case-классов.

Проверки:
- обязательные поля спеки;
- endpoints.id и endpoints.label соответствуют Case-классам;
- profile.count ключи совпадают с Case-классами.

Пример:
  python3 SDD/ltAuto/spec_validate.py --spec SDD/specs/ZK/ComplianceRequests/spec.yaml
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

from case_parser import parse_case_classes, log_to_var


class Reporter:
    def __init__(self):
        self.ok = 0
        self.fail = 0
        self.warn = 0

    def _print(self, tag, msg):
        print("[spec] {}: {}".format(tag, msg))

    def ok_msg(self, msg):
        self.ok += 1
        self._print("OK", msg)

    def fail_msg(self, msg):
        self.fail += 1
        self._print("FAIL", msg)

    def warn_msg(self, msg):
        self.warn += 1
        self._print("WARN", msg)

    def passed(self):
        return self.fail == 0

    def summary(self):
        status = "PASS" if self.passed() else "FAIL"
        print("---")
        print("RESULT: {} ({} ok, {} fail, {} warn)".format(
            status, self.ok, self.fail, self.warn))


def detect_gatling_root() -> str:
    candidates = [
        _REPO_ROOT,
        os.path.join(_REPO_ROOT, "gatling"),
    ]
    for cand in candidates:
        if os.path.isfile(os.path.join(cand, "pom.xml")) and os.path.isdir(os.path.join(cand, "src")):
            return os.path.abspath(cand)
    return os.path.abspath(_REPO_ROOT)


def load_spec(path: str) -> dict:
    with open(path, encoding="utf-8") as fh:
        return yaml.load(fh, Loader=yaml.FullLoader) or {}


def normalize_list(val):
    if val is None:
        return []
    if isinstance(val, list):
        return val
    return [val]


def required_fields(r: Reporter, spec: dict):
    meta = spec.get("meta") or {}
    for key in ("service", "component", "scenario", "source"):
        if meta.get(key):
            r.ok_msg("meta.{} задан".format(key))
        else:
            r.fail_msg("meta.{} отсутствует".format(key))
    if spec.get("request_classes"):
        r.ok_msg("request_classes задан")
    else:
        r.fail_msg("request_classes отсутствует")
    if spec.get("endpoints"):
        r.ok_msg("endpoints задан")
    else:
        r.fail_msg("endpoints отсутствует")


def validate_labels(r: Reporter, spec: dict, base_dirs: list[str]):
    request_classes = normalize_list(spec.get("request_classes"))
    if not request_classes:
        return
    mapping = parse_case_classes(request_classes, base_dirs=base_dirs)
    log_map = log_to_var(mapping)

    endpoints = spec.get("endpoints") or []
    for endpoint in endpoints:
        eid = endpoint.get("id")
        label = endpoint.get("label")
        if eid and eid in mapping:
            r.ok_msg("endpoint id найден в Case: {}".format(eid))
        else:
            r.fail_msg("endpoint id не найден в Case: {}".format(eid))
        if label and label in log_map:
            r.ok_msg("endpoint label найден в Case: {}".format(label))
        else:
            r.fail_msg("endpoint label не найден в Case: {}".format(label))

    counts = (spec.get("profile") or {}).get("count") or {}
    for key in counts:
        if key in mapping or key in log_map:
            r.ok_msg("profile.count содержит: {}".format(key))
        else:
            r.fail_msg("profile.count ключ не найден в Case: {}".format(key))

    spec_labels = {e.get("label") for e in endpoints if e.get("label")}
    case_labels = set(log_map.keys())
    extra = sorted(case_labels - spec_labels)
    if extra:
        r.warn_msg("в Case есть label, которых нет в spec ({} шт.)".format(len(extra)))


def main() -> int:
    ap = argparse.ArgumentParser(description="Валидация spec.yaml против Case-классов")
    ap.add_argument("--spec", required=True, help="путь к spec.yaml")
    args = ap.parse_args()

    spec_path = os.path.abspath(args.spec)
    spec = load_spec(spec_path)

    r = Reporter()
    required_fields(r, spec)

    gatling_root = detect_gatling_root()
    base_dirs = [gatling_root, os.getcwd(), _REPO_ROOT]
    validate_labels(r, spec, base_dirs)

    r.summary()
    return 0 if r.passed() else 1


if __name__ == "__main__":
    sys.exit(main())
