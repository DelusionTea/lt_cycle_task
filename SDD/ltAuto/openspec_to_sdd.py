"""Convert OpenSpec/GigaCLI artifact to SDD spec.yaml.

Bridge mode:
- OpenSpec stays source of authoring.
- Jenkins/Gatling keep running against SDD spec.yaml via existing tools.

Usage:
  python3 SDD/ltAuto/openspec_to_sdd.py \
    --input path/to/openspec.yaml \
    --output SDD/specs/<AS>/<Component>/spec.yaml \
    --expected_service <AS>
"""

from __future__ import annotations

import argparse
import json
import os
import sys
from typing import Any

import yaml


def _load_any(path: str) -> dict[str, Any]:
    with open(path, encoding="utf-8") as fh:
        text = fh.read()
    try:
        data = yaml.safe_load(text)
    except yaml.YAMLError as exc:
        raise ValueError(f"cannot parse input as YAML/JSON: {exc}") from exc
    if not isinstance(data, dict):
        raise ValueError("input root must be a mapping/object")
    return data


def _normalize_list(value: Any) -> list[Any]:
    if value is None:
        return []
    if isinstance(value, list):
        return value
    return [value]


def _deep_get(source: dict[str, Any], path: list[str]) -> Any:
    node: Any = source
    for key in path:
        if not isinstance(node, dict) or key not in node:
            return None
        node = node[key]
    return node


def _pick_first(source: dict[str, Any], paths: list[list[str]], default: Any = None) -> Any:
    for path in paths:
        value = _deep_get(source, path)
        if value is not None and value != "":
            return value
    return default


def _normalize_endpoint(item: dict[str, Any], index: int) -> dict[str, Any]:
    eid = _pick_first(item, [["id"], ["endpoint_id"], ["operation_id"], ["operationId"]])
    label = _pick_first(item, [["label"], ["request_label"], ["x-label"], ["name"], ["summary"]])
    method = _pick_first(item, [["method"], ["http_method"]], default="GET")
    path = _pick_first(item, [["path"], ["url"], ["uri"], ["route"]], default="")

    normalized: dict[str, Any] = {
        "id": eid,
        "label": label,
        "method": str(method).upper(),
        "path": path,
    }
    checks = item.get("checks")
    if isinstance(checks, dict):
        normalized["checks"] = checks
    return normalized


def _extract_endpoints(payload: dict[str, Any]) -> list[dict[str, Any]]:
    # Preferred shape: endpoints: [{id, label, method, path, ...}]
    endpoints_raw = _pick_first(
        payload,
        [["endpoints"], ["operations"], ["requests"], ["api", "endpoints"]],
        default=[],
    )
    if isinstance(endpoints_raw, list):
        out: list[dict[str, Any]] = []
        for idx, item in enumerate(endpoints_raw, start=1):
            if isinstance(item, dict):
                out.append(_normalize_endpoint(item, idx))
        return out

    # OpenAPI-like fallback: paths: {"/x": {"get": {"operationId": "...", "x-label": "..."}}}
    paths_obj = payload.get("paths")
    if isinstance(paths_obj, dict):
        out = []
        for route, methods in paths_obj.items():
            if not isinstance(methods, dict):
                continue
            for method, desc in methods.items():
                if method.lower() not in {"get", "post", "put", "patch", "delete", "head", "options"}:
                    continue
                if not isinstance(desc, dict):
                    desc = {}
                item = {
                    "id": desc.get("operationId"),
                    "label": desc.get("x-label") or desc.get("summary"),
                    "method": method,
                    "path": route,
                    "checks": {"status": 200},
                }
                out.append(_normalize_endpoint(item, len(out) + 1))
        return out
    return []


def _extract_counts(payload: dict[str, Any], endpoint_ids: list[str]) -> dict[str, int]:
    count_raw = _pick_first(
        payload,
        [
            ["profile", "count"],
            ["load", "count"],
            ["counts"],
            ["traffic", "count"],
        ],
        default={},
    )
    if isinstance(count_raw, dict) and count_raw:
        out: dict[str, int] = {}
        for key, value in count_raw.items():
            try:
                out[str(key)] = int(value)
            except (TypeError, ValueError):
                out[str(key)] = 0
        return out
    return {eid: 0 for eid in endpoint_ids if eid}


def _ensure_required(spec: dict[str, Any]) -> None:
    meta = spec.get("meta") or {}
    for key in ("service", "component", "scenario", "source"):
        if not meta.get(key):
            raise ValueError(f"missing required field: meta.{key}")
    if not spec.get("request_classes"):
        raise ValueError("missing required field: request_classes")
    endpoints = spec.get("endpoints") or []
    if not endpoints:
        raise ValueError("missing required field: endpoints")
    for idx, endpoint in enumerate(endpoints, start=1):
        if not endpoint.get("id"):
            raise ValueError(f"endpoints[{idx}].id is required")
        if not endpoint.get("label"):
            raise ValueError(f"endpoints[{idx}].label is required")


def build_sdd_spec(payload: dict[str, Any], expected_service: str = "") -> dict[str, Any]:
    meta_payload = payload.get("meta") if isinstance(payload.get("meta"), dict) else {}

    service = _pick_first(
        payload,
        [["meta", "service"], ["service"], ["application"], ["system"]],
        default="",
    )
    component = _pick_first(
        payload,
        [["meta", "component"], ["component"], ["domain"], ["name"]],
        default="",
    )
    scenario = _pick_first(
        payload,
        [["meta", "scenario"], ["scenario"], ["load_scenario"]],
        default=component,
    )
    description = _pick_first(
        payload,
        [["meta", "description"], ["description"]],
        default="Generated from OpenSpec artifact",
    )

    endpoints = _extract_endpoints(payload)
    endpoint_ids = [e.get("id") for e in endpoints if e.get("id")]
    counts = _extract_counts(payload, endpoint_ids)

    request_classes = _pick_first(
        payload,
        [["request_classes"], ["requestClasses"], ["cases"]],
        default=[],
    )
    request_classes = [str(x) for x in _normalize_list(request_classes) if str(x).strip()]

    profile = payload.get("profile") if isinstance(payload.get("profile"), dict) else {}
    injection = payload.get("injection") if isinstance(payload.get("injection"), dict) else {}

    spec = {
        "meta": {
            "service": service,
            "component": component,
            "scenario": scenario,
            "source": _pick_first(payload, [["meta", "source"], ["source"]], default="openspec-gigacli"),
            "swagger": bool(meta_payload.get("swagger", False)),
            "description": description,
        },
        "request_classes": request_classes,
        "profile": {
            "target_percent": int(profile.get("target_percent", 100)),
            "rampup": int(profile.get("rampup", 0)),
            "thresholds": profile.get(
                "thresholds",
                {"pct95": 1000, "pct50": 500, "rps": 1, "error_count": 100},
            ),
            "count": counts,
            "sla_per_label": profile.get("sla_per_label", {}),
        },
        "injection": injection or {"duration": 600, "rampup": 60, "scenarios": {}},
        "endpoints": endpoints,
    }

    if expected_service and service and expected_service != service:
        raise ValueError(
            "meta.service mismatch: expected '{}' but got '{}'".format(expected_service, service)
        )
    _ensure_required(spec)
    return spec


def write_yaml(path: str, data: dict[str, Any]) -> None:
    parent = os.path.dirname(path)
    if parent:
        os.makedirs(parent, exist_ok=True)
    with open(path, "w", encoding="utf-8") as fh:
        yaml.safe_dump(data, fh, sort_keys=False, allow_unicode=True)


def main() -> int:
    parser = argparse.ArgumentParser(description="Convert OpenSpec/GigaCLI artifact to SDD spec.yaml")
    parser.add_argument("--input", required=True, help="path to OpenSpec artifact (yaml/json)")
    parser.add_argument("--output", required=True, help="path to SDD spec.yaml")
    parser.add_argument("--expected_service", default="", help="optional strict check for meta.service")
    parser.add_argument("--dump_json", default="", help="optional path to dump normalized JSON for debug")
    args = parser.parse_args()

    try:
        payload = _load_any(os.path.abspath(args.input))
        spec = build_sdd_spec(payload, expected_service=(args.expected_service or "").strip())
        write_yaml(os.path.abspath(args.output), spec)
        if args.dump_json:
            with open(os.path.abspath(args.dump_json), "w", encoding="utf-8") as fh:
                json.dump(spec, fh, ensure_ascii=False, indent=2)
        print("[openspec] SDD spec written to {}".format(args.output))
        return 0
    except Exception as exc:  # noqa: BLE001
        print("[openspec] FAIL: {}".format(exc), file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
