#!/usr/bin/env python3
"""Generate Gatling Case/Scenario/JSON/Feeder resources from swaggerZK.json."""

from __future__ import annotations

import hashlib
import json
import re
import shutil
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
GATLING = ROOT / "gatling" / "src" / "test"
SWAGGER = ROOT / "resources" / "swaggerZK.json"

HTTP_METHODS = ("get", "post", "put", "delete", "patch")
PLACEHOLDER_UUID = "00000000-0000-0000-0000-000000000001"
AUDIT_SKIP = frozenset({"id", "created", "updated", "createdBy", "updatedBy"})

FEEDER_QUERY_NAMES = {
    "region",
    "lockKey",
    "clientId",
    "ucpId",
    "ucpIds",
    "ids",
    "requestId",
    "caseId",
    "inn",
    "createdDate",
    "employeeNumber",
    "pageNum",
    "getWorkDay",
}


SHARED_JSON_DIR = "shared"
PAGING_FILTER_CANONICAL = {"paging": {"page": 1, "limit": 10}}


def content_key(payload) -> str:
    return json.dumps(payload, sort_keys=True, ensure_ascii=False)


def has_feeder_refs(payload) -> bool:
    return "#{" in content_key(payload)


def is_paging_filter(payload: dict) -> bool:
    return payload == PAGING_FILTER_CANONICAL


def schema_ref_name(schema: dict | None) -> str:
    if not schema:
        return ""
    if "$ref" in schema:
        return schema["$ref"].split("/")[-1]
    return ""


def short_hash(key: str) -> str:
    return hashlib.md5(key.encode()).hexdigest()[:8]


class JsonResourceRegistry:
    """Дедупликация JSON: shared/ для общих static, {Class}/ для class-specific."""

    def __init__(self) -> None:
        self.global_map: dict[str, str] = {}
        self.class_map: dict[tuple[str, str], str] = {}
        self.files: dict[str, object] = {}

    def resolve(self, payload, class_name: str, kind: str, schema_ref: str = "") -> str:
        key = content_key(payload)

        if not has_feeder_refs(payload) and key in self.global_map:
            return self.global_map[key]

        class_ck = (class_name, key)
        if class_ck in self.class_map:
            return self.class_map[class_ck]

        rel = self._allocate_path(key, payload, class_name, kind, schema_ref)
        self.files[rel] = payload
        self.class_map[class_ck] = rel
        if not has_feeder_refs(payload):
            self.global_map[key] = rel
        return rel

    def _allocate_path(self, key: str, payload, class_name: str, kind: str, schema_ref: str) -> str:
        if not has_feeder_refs(payload):
            if is_paging_filter(payload):
                return f"{SHARED_JSON_DIR}/PagingRequestExt_filter.json"
            if schema_ref:
                return f"{SHARED_JSON_DIR}/{schema_ref}_{kind}.json"
            return f"{SHARED_JSON_DIR}/{kind}_{short_hash(key)}.json"

        if schema_ref:
            return f"{class_name}/{schema_ref}_{kind}.json"
        return f"{class_name}/{kind}_{short_hash(key)}.json"

    def write_all(self, jsons_root: Path) -> int:
        count = 0
        for rel, payload in sorted(self.files.items()):
            path = jsons_root / rel
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
            count += 1
        return count


def resolve_schema(schema: dict, schemas: dict, depth: int = 0) -> dict:
    if not schema or depth > 12:
        return {}
    if "$ref" in schema:
        name = schema["$ref"].split("/")[-1]
        return resolve_schema(schemas.get(name, {}), schemas, depth + 1)
    if "allOf" in schema:
        merged: dict = {"type": "object", "properties": {}, "required": []}
        for part in schema["allOf"]:
            part_res = resolve_schema(part, schemas, depth + 1)
            merged["properties"].update(part_res.get("properties", {}))
            merged["required"] = list(set(merged.get("required", []) + part_res.get("required", [])))
        return merged
    return schema


def to_feeder_var(name: str) -> str:
    if re.search(r"[-_]", name):
        parts = re.split(r"[-_]", name)
        return parts[0].lower() + "".join(p[:1].upper() + p[1:] for p in parts[1:] if p)
    return name[:1].lower() + name[1:] if name else name


def class_prefix(class_name: str) -> str:
    return class_name[:1].lower() + class_name[1:] if class_name else ""


def feeder_var(class_name: str, param_name: str) -> str:
    """Уникальное имя session-переменной в рамках контроллера, напр. adClientMarkingsId."""
    prefix = class_prefix(class_name)
    base = to_feeder_var(param_name)
    if base.startswith(prefix) and len(base) > len(prefix):
        return base
    if not base:
        return prefix
    return prefix + base[0].upper() + base[1:]


def feeder_placeholder(class_name: str, param_name: str) -> str:
    return f"#{{{feeder_var(class_name, param_name)}}}"


def is_feeder_field(name: str, schema: dict | None = None) -> bool:
    if name in FEEDER_QUERY_NAMES:
        return True
    if name.endswith("Id") or name.endswith("Ids"):
        return True
    if schema:
        schema = resolve_schema(schema, {})
        if schema.get("format") == "uuid":
            return True
    return False


def sample_value(
    schema: dict,
    schemas: dict,
    name: str = "",
    depth: int = 0,
    feeder_mode: bool = False,
    class_name: str = "",
):
    if depth > 8:
        return "sample"
    schema = resolve_schema(schema, schemas, depth)
    if schema.get("enum"):
        return schema["enum"][0]
    t = schema.get("type")
    fmt = schema.get("format")
    if feeder_mode and class_name and is_feeder_field(name, schema):
        return feeder_placeholder(class_name, name)
    if t == "string":
        if fmt == "uuid":
            return PLACEHOLDER_UUID
        if fmt in ("date-time", "date"):
            return "2026-01-01T00:00:00Z"
        return f"sample_{name}" if name else "sample"
    if t == "integer":
        return schema.get("minimum", 1)
    if t == "number":
        return int(schema.get("minimum", 1))
    if t == "boolean":
        return False
    if t == "array":
        items = schema.get("items", {})
        if feeder_mode and class_name and name in {"ids", "ucpIds"}:
            return [feeder_placeholder(class_name, name)]
        item = sample_value(items, schemas, name, depth + 1, feeder_mode, class_name)
        return [item] if item is not None else []
    if t == "object" or schema.get("properties"):
        return build_object_payload(schema, schemas, depth + 1, feeder_mode, class_name)
    return None


def build_minimal_paging() -> dict:
    return {"page": 1, "limit": 10}


def is_paging_container(schema: dict) -> bool:
    return "paging" in schema.get("properties", {})


def example_value(prop: dict, key: str, feeder_mode: bool, class_name: str = ""):
    val = prop["example"]
    if feeder_mode and class_name and is_feeder_field(key, prop):
        return feeder_placeholder(class_name, key)
    return val


def build_object_payload(
    schema: dict,
    schemas: dict,
    depth: int = 0,
    feeder_mode: bool = False,
    class_name: str = "",
) -> dict:
    schema = resolve_schema(schema, schemas, depth)
    if schema.get("example") is not None and isinstance(schema["example"], dict):
        obj = schema["example"]
        if feeder_mode and class_name:
            return {
                key: feeder_placeholder(class_name, key) if is_feeder_field(key, {}) else val
                for key, val in obj.items()
            }
        return obj

    props = schema.get("properties", {})
    required = schema.get("required") or []

    if required:
        obj = {}
        for key in required:
            if key not in props:
                continue
            prop = props[key]
            nested = resolve_schema(prop, schemas, depth + 1)
            if nested.get("properties") or nested.get("$ref") or prop.get("$ref"):
                obj[key] = build_object_payload(prop, schemas, depth + 1, feeder_mode, class_name)
            else:
                val = sample_value(prop, schemas, key, depth + 1, feeder_mode, class_name)
                if val is not None:
                    obj[key] = val
        return obj

    if is_paging_container(schema):
        return {"paging": build_minimal_paging()}

    obj = {}
    for key, prop in props.items():
        if key in AUDIT_SKIP:
            continue
        if "example" in prop:
            obj[key] = example_value(prop, key, feeder_mode, class_name)
            continue
        nested = resolve_schema(prop, schemas, depth + 1)
        if nested.get("required") and (prop.get("$ref") or prop.get("type") == "object"):
            nested_obj = build_object_payload(prop, schemas, depth + 1, feeder_mode, class_name)
            if nested_obj:
                obj[key] = nested_obj

    if obj:
        return obj

    for key, prop in props.items():
        if key in AUDIT_SKIP:
            continue
        if prop.get("$ref") or prop.get("type") == "object":
            nested_obj = build_object_payload(prop, schemas, depth + 1, feeder_mode, class_name)
            if nested_obj:
                obj[key] = nested_obj

    if obj:
        return obj

    for key, prop in props.items():
        if key in AUDIT_SKIP:
            continue
        if prop.get("type") == "array":
            items = resolve_schema(prop.get("items", {}), schemas, depth + 1)
            if items.get("type") == "string":
                obj[key] = ["sample"]
            elif items.get("properties") or items.get("$ref"):
                item_obj = build_object_payload(prop.get("items", {}), schemas, depth + 1, feeder_mode, class_name)
                obj[key] = [item_obj] if item_obj else [{}]

    if obj:
        return obj

    for key, prop in props.items():
        if key in AUDIT_SKIP:
            continue
        if prop.get("type") in ("string", "integer", "number", "boolean"):
            val = sample_value(prop, schemas, key, depth + 1, feeder_mode, class_name)
            if val is not None:
                obj[key] = val
                break

    return obj


def build_payload(schema: dict, schemas: dict, feeder_mode: bool = False, class_name: str = ""):
    schema = resolve_schema(schema, schemas)
    if schema.get("type") == "array":
        items = resolve_schema(schema.get("items", {}), schemas)
        if items.get("type") == "string":
            if feeder_mode and class_name:
                return [feeder_placeholder(class_name, "id")]
            return ["sample"]
        item = build_object_payload(schema.get("items", {}), schemas, feeder_mode=feeder_mode, class_name=class_name)
        return [item] if item else [{}]
    if not schema.get("properties") and not schema.get("type") and not schema.get("$ref"):
        return {}
    if schema.get("type") not in (None, "object") and not schema.get("properties"):
        value = sample_value(schema, schemas, feeder_mode=feeder_mode, class_name=class_name)
        return value if value is not None else {}
    return build_object_payload(schema, schemas, feeder_mode=feeder_mode, class_name=class_name)


def build_required_object(schema: dict, schemas: dict, depth: int = 0, feeder_mode: bool = False, class_name: str = "") -> dict:
    return build_object_payload(schema, schemas, depth, feeder_mode, class_name)


def controller_of(path: str) -> str:
    m = re.match(r"^/api/v\d+/([^/]+)", path)
    if m:
        return m.group(1)
    m2 = re.match(r"^/([^/]+)/", path)
    if m2:
        return m2.group(1)
    return "root"


def to_java_class(controller_key: str) -> str:
    parts = re.split(r"[-_]", controller_key)
    return "".join(p[:1].upper() + p[1:] for p in parts if p)


def api_version(path: str) -> str:
    m = re.match(r"^/api/v(\d+)/", path)
    return m.group(1) if m else "0"


def to_var_suffix(path: str, method: str) -> str:
    version = api_version(path)
    p = re.sub(r"^/api/v\d+/", "", path).strip("/")
    p = re.sub(r"\{([^}]+)\}", r"By_\1", p)
    p = re.sub(r"[^a-zA-Z0-9]", "_", p)
    p = re.sub(r"_+", "_", p).strip("_")
    base = f"{method.upper()}_v{version}_{p}" if p else f"{method.upper()}_v{version}"
    return base


def get_path_params(op: dict) -> list[dict]:
    return [p for p in op.get("parameters", []) if p.get("in") == "path"]


def get_query_params(op: dict) -> list[dict]:
    return [p for p in op.get("parameters", []) if p.get("in") == "query"]


def get_body_schema(op: dict):
    rb = op.get("requestBody")
    if not rb:
        return None
    content = rb.get("content", {})
    for ct in ("application/json", "*/*", "application/*+json"):
        if ct in content:
            return content[ct].get("schema")
    for value in content.values():
        if "schema" in value:
            return value["schema"]
    return None


def query_schema(param: dict) -> dict:
    content = param.get("content", {})
    for ct in ("*/*", "application/json", "application/*+json"):
        if ct in content:
            return content[ct].get("schema", {})
    return param.get("schema", {})


def is_complex_query(param: dict, schemas: dict) -> bool:
    raw = query_schema(param)
    if "$ref" in raw:
        return True
    schema = resolve_schema(raw, schemas)
    t = schema.get("type")
    return t in ("object", "array") and param["name"] not in FEEDER_QUERY_NAMES


def java_path(path: str, path_params: list[dict], class_name: str) -> str:
    result = path
    for param in path_params:
        name = param["name"]
        result = result.replace("{" + name + "}", feeder_placeholder(class_name, name))
    return result


def collect_feeder_vars(controller_ops: list[dict], schemas: dict, class_name: str) -> dict[str, str]:
    vars_map: dict[str, str] = {}
    for op in controller_ops:
        swagger_op = op["op"]
        for param in get_path_params(swagger_op):
            var = feeder_var(class_name, param["name"])
            vars_map[var] = default_feeder_value(param["name"], param.get("schema", {}))
        for param in get_query_params(swagger_op):
            if is_complex_query(param, schemas):
                continue
            if needs_feeder_query(param):
                var = feeder_var(class_name, param["name"])
                vars_map[var] = default_feeder_value(param["name"], query_schema(param))
        body_schema = get_body_schema(swagger_op)
        if body_schema:
            collect_body_feeder_vars(body_schema, vars_map, schemas, class_name)
    return vars_map


def collect_body_feeder_vars(schema: dict, vars_map: dict[str, str], schemas: dict, class_name: str):
    schema = resolve_schema(schema, schemas)
    required = set(schema.get("required") or [])
    for key, prop in schema.get("properties", {}).items():
        if key in AUDIT_SKIP:
            continue
        if is_feeder_field(key, prop) and (key in required or "example" in prop):
            var = feeder_var(class_name, key)
            vars_map.setdefault(var, default_feeder_value(key, prop))
        if prop.get("type") == "object" or prop.get("$ref"):
            collect_body_feeder_vars(prop, vars_map, schemas, class_name)


def needs_feeder_query(param: dict) -> bool:
    name = param["name"]
    if name in FEEDER_QUERY_NAMES:
        return True
    if name.endswith("Id") or name.endswith("Ids"):
        return True
    schema = query_schema(param)
    if schema.get("format") == "uuid":
        return True
    return False


def default_feeder_value(name: str, schema: dict | None = None) -> str:
    schema = schema or {}
    if schema.get("type") == "array" or name.endswith("Ids") or name == "ids":
        return json.dumps([PLACEHOLDER_UUID])
    if schema.get("format") == "uuid":
        return PLACEHOLDER_UUID
    if schema.get("type") in ("integer", "number"):
        return "1"
    if schema.get("format") in ("date-time", "date"):
        return "2026-01-01T00:00:00Z"
    return f"sample_{name}"


def build_case_request(op_info: dict) -> str:
    method = op_info["method"]
    path = op_info["java_path"]
    var = op_info["var"]
    log = op_info["log"]

    lines = [
        f"    public static HttpRequestActionBuilder {var} =",
        f'            http("{log}")',
        f'                    .{method}("{path}")',
    ]

    if op_info.get("body_json"):
        rel = op_info["body_json"]
        lines.append(f'                    .body(ElFileBody(JSONS_PATH + "{rel}"))')

    for qp in op_info.get("query_jsons", []):
        lines.append(
            f'                    .queryParam("{qp["name"]}", '
            f'Methods.queryFromFile(JSONS_PATH + "{qp["rel"]}"))'
        )

    for qp in op_info.get("query_feeder", []):
        lines.append(f'                    .queryParam("{qp["name"]}", "#{{{qp["var"]}}}")')

    for qp in op_info.get("query_inline", []):
        lines.append(f'                    .queryParam("{qp["name"]}", "{qp["value"]}")')

    lines.extend(
        [
            "                    .headers(Headers.getCommonHeaders())",
            "                    .check(status().is(200));",
            "",
        ]
    )
    return "\n".join(lines)


def build_case_file(class_name: str, ops: list[dict]) -> str:
    requests = "\n".join(build_case_request(op) for op in ops)
    has_body = any(op.get("body_json") for op in ops)
    el_file_body_import = (
        "import static io.gatling.javaapi.core.CoreDsl.ElFileBody;\n"
        if has_body
        else ""
    )
    return (
        f"package cases.ZK;\n\n"
        f"import feeders.ZK.Headers;\n"
        f"import feeders.ZK.Methods;\n\n"
        f"import io.gatling.javaapi.http.HttpRequestActionBuilder;\n\n"
        f"{el_file_body_import}"
        f"import static io.gatling.javaapi.http.HttpDsl.http;\n"
        f"import static io.gatling.javaapi.http.HttpDsl.status;\n\n"
        f"public class {class_name}Case extends Methods {{\n\n"
        f'    private static final String JSONS_PATH = "JSONs/ZK/";\n\n'
        f"{requests}"
        f"}}\n"
    )


def build_scenario_file(class_name: str, ops: list[dict]) -> str:
    default_weight = max(1, 100 // len(ops))
    chain_defs = []
    debug_execs = []
    switch_choices = []

    for op in ops:
        var = op["var"]
        chain_defs.append(
            f"    public static ChainBuilder {var} =\n"
            f'            group("{var}").on(\n'
            f"                    exec({class_name}Case.{var}));"
        )
        debug_execs.append(f"            .exec({class_name}Case.{var})")
        switch_choices.append(f"                            new Choice.WithWeight({default_weight}, {var})")

    chains = "\n\n".join(chain_defs)
    debug = "\n".join(debug_execs)
    choices = ",\n".join(switch_choices)

    return (
        f"package scenarios.ZK;\n\n"
        f"import cases.ZK.{class_name}Case;\n\n"
        f"import io.gatling.javaapi.core.ChainBuilder;\n"
        f"import io.gatling.javaapi.core.Choice;\n"
        f"import io.gatling.javaapi.core.ScenarioBuilder;\n\n"
        f"import static feeders.ZK.Methods.rqUidsFeeder;\n"
        f"import static feeders.ZK.ZKFeeder.defaultFeeder;\n"
        f"import static io.gatling.javaapi.core.CoreDsl.*;\n\n"
        f"public class {class_name}Scenario {{\n\n"
        f"{chains}\n\n"
        f'    public static ScenarioBuilder scn = scenario("{class_name}")\n'
        f"            .feed(defaultFeeder)\n"
        f"            .feed(rqUidsFeeder)\n"
        f"            .forever().on(\n"
        f"                    randomSwitch().on(\n"
        f"{choices}\n"
        f"                    )\n"
        f"            );\n\n"
        f'    public static ScenarioBuilder Debug = scenario("Debug {class_name}")\n'
        f"            .feed(defaultFeeder)\n"
        f"            .feed(rqUidsFeeder)\n"
        f"{debug};\n"
        f"}}\n"
    )


def build_zk_feeder_file(all_feeder_vars: dict[str, str]) -> str:
    puts = []
    for var, value in sorted(all_feeder_vars.items()):
        escaped = value.replace("\\", "\\\\").replace('"', '\\"')
        puts.append(f'        row.put("{var}", "{escaped}");')

    puts_body = "\n".join(puts)
    return (
        f"package feeders.ZK;\n\n"
        f"import io.gatling.javaapi.core.FeederBuilder;\n\n"
        f"import java.util.HashMap;\n"
        f"import java.util.List;\n"
        f"import java.util.Map;\n\n"
        f"import static io.gatling.javaapi.core.CoreDsl.listFeeder;\n\n"
        f"/**\n"
        f" * Заглушка: подключить реальный источник данных (CSV/БД) для ZK.\n"
        f" * Единый feeder для всех Case-классов домена ZK.\n"
        f" * Имена переменных имеют префикс контроллера (напр. adClientMarkingsId).\n"
        f" */\n"
        f"public class ZKFeeder {{\n\n"
        f"    public static FeederBuilder<Object> defaultFeeder = listFeeder(\n"
        f"            List.of(defaultRow())\n"
        f"    ).circular();\n\n"
        f"    private static Map<String, Object> defaultRow() {{\n"
        f"        Map<String, Object> row = new HashMap<>();\n"
        f"{puts_body}\n"
        f"        return row;\n"
        f"    }}\n"
        f"}}\n"
    )


def prepare_operation(op: dict, schemas: dict, class_name: str, json_registry: JsonResourceRegistry) -> dict:
    swagger_op = op["op"]
    body_schema = get_body_schema(swagger_op)
    query_params = get_query_params(swagger_op)
    path_params = get_path_params(swagger_op)

    op["java_path"] = java_path(op["path"], path_params, class_name)
    op["body_json"] = None
    op["query_jsons"] = []
    op["query_feeder"] = []
    op["query_inline"] = []

    if body_schema is not None:
        payload = build_payload(body_schema, schemas, feeder_mode=True, class_name=class_name)
        ref = schema_ref_name(body_schema)
        op["body_json"] = json_registry.resolve(payload, class_name, "body", ref)

    for qp in query_params:
        name = qp["name"]
        if is_complex_query(qp, schemas):
            qp_schema = query_schema(qp)
            payload = build_payload(qp_schema, schemas)
            ref = schema_ref_name(qp_schema)
            rel = json_registry.resolve(payload, class_name, name, ref)
            op["query_jsons"].append({"name": name, "rel": rel})
        elif needs_feeder_query(qp):
            op["query_feeder"].append({"name": name, "var": feeder_var(class_name, name)})
        else:
            schema = query_schema(qp)
            value = sample_value(schema, schemas, name)
            if isinstance(value, str):
                inline = value
            elif isinstance(value, bool):
                inline = str(value).lower()
            else:
                inline = str(value)
            op["query_inline"].append({"name": name, "value": inline})

    return op


def clean_generated_zk(cases_dir: Path, scenarios_dir: Path, feeders_dir: Path, jsons_root: Path) -> None:
    for directory in (cases_dir, scenarios_dir):
        if directory.exists():
            shutil.rmtree(directory)
        directory.mkdir(parents=True)
    if jsons_root.exists():
        shutil.rmtree(jsons_root)
    jsons_root.mkdir(parents=True)
    if feeders_dir.exists():
        for path in feeders_dir.glob("*Feeder.java"):
            path.unlink()


def main() -> None:
    with open(SWAGGER, encoding="utf-8") as f:
        swagger = json.load(f)
    schemas = swagger.get("components", {}).get("schemas", {})

    raw_ops = []
    for path in sorted(swagger["paths"].keys()):
        path_item = swagger["paths"][path]
        for method in HTTP_METHODS:
            if method not in path_item:
                continue
            raw_ops.append(
                {
                    "method": method,
                    "path": path,
                    "op": path_item[method],
                    "controller": controller_of(path),
                }
            )

    by_controller: dict[str, list[dict]] = defaultdict(list)
    for item in raw_ops:
        by_controller[item["controller"]].append(item)

    cases_dir = GATLING / "java" / "cases" / "ZK"
    scenarios_dir = GATLING / "java" / "scenarios" / "ZK"
    feeders_dir = GATLING / "java" / "feeders" / "ZK"
    jsons_root = GATLING / "resources" / "JSONs" / "ZK"

    clean_generated_zk(cases_dir, scenarios_dir, feeders_dir, jsons_root)
    feeders_dir.mkdir(parents=True, exist_ok=True)

    total_ops = 0
    all_feeder_vars: dict[str, str] = {}
    json_registry = JsonResourceRegistry()

    for controller_key in sorted(by_controller.keys()):
        class_name = to_java_class(controller_key)
        controller_ops = by_controller[controller_key]
        controller_ops.sort(key=lambda x: (x["path"], x["method"]))

        for uc_idx, raw in enumerate(controller_ops, start=1):
            uc = f"UC{uc_idx:02d}"
            op = {
                **raw,
                "var": f"{uc}_{to_var_suffix(raw['path'], raw['method'])}",
                "log": f"{uc}_{raw['method'].upper()}_{raw['path']}",
            }
            prepare_operation(op, schemas, class_name, json_registry)
            controller_ops[uc_idx - 1] = op
            total_ops += 1

        feeder_vars = collect_feeder_vars(controller_ops, schemas, class_name)
        all_feeder_vars.update(feeder_vars)
        (cases_dir / f"{class_name}Case.java").write_text(
            build_case_file(class_name, controller_ops), encoding="utf-8"
        )
        (scenarios_dir / f"{class_name}Scenario.java").write_text(
            build_scenario_file(class_name, controller_ops), encoding="utf-8"
        )

    json_count = json_registry.write_all(jsons_root)

    (feeders_dir / "ZKFeeder.java").write_text(
        build_zk_feeder_file(all_feeder_vars), encoding="utf-8"
    )

    headers_java = (
        "package feeders.ZK;\n\n"
        "import java.util.HashMap;\n"
        "import java.util.Map;\n\n"
        "public class Headers {\n"
        "    public static Map<String, String> getCommonHeaders() {\n"
        "        Map<String, String> headers = new HashMap<>();\n"
        "        headers.put(\"Content-Type\", \"application/json\");\n"
        "        headers.put(\"Accept\", \"application/json\");\n"
        "        headers.put(\"rq-uuid\", \"#{rqUuid}\");\n"
        "        headers.put(\"source-system-id\", \"GATLING\");\n"
        "        headers.put(\"destination-system-id\", \"ZK\");\n"
        "        return headers;\n"
        "    }\n"
        "}\n"
    )

    methods_java = (
        "package feeders.ZK;\n\n"
        "import io.gatling.javaapi.core.FeederBuilder;\n"
        "import io.gatling.javaapi.core.Session;\n\n"
        "import java.io.IOException;\n"
        "import java.io.InputStream;\n"
        "import java.nio.charset.StandardCharsets;\n"
        "import java.util.Collections;\n"
        "import java.util.Map;\n"
        "import java.util.UUID;\n"
        "import java.util.function.Function;\n\n"
        "import static io.gatling.javaapi.core.CoreDsl.listFeeder;\n\n"
        "public class Methods {\n"
        "    public static FeederBuilder<Object> rqUidsFeeder = listFeeder(\n"
        "            Collections.singletonList(Map.of(\"rqUuid\", UUID.randomUUID().toString()))\n"
        "    ).circular();\n\n"
        "    public static Function<Session, String> queryFromFile(String classpathResource) {\n"
        "        return session -> readResource(classpathResource);\n"
        "    }\n\n"
        "    protected static String readResource(String classpathResource) {\n"
        "        InputStream stream = Methods.class.getClassLoader().getResourceAsStream(classpathResource);\n"
        "        if (stream == null) {\n"
        "            throw new IllegalStateException(\"Resource not found: \" + classpathResource);\n"
        "        }\n"
        "        try (InputStream in = stream) {\n"
        "            return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();\n"
        "        } catch (IOException e) {\n"
        "            throw new IllegalStateException(\"Failed to read resource: \" + classpathResource, e);\n"
        "        }\n"
        "    }\n"
        "}\n"
    )

    (feeders_dir / "Headers.java").write_text(headers_java, encoding="utf-8")
    (feeders_dir / "Methods.java").write_text(methods_java, encoding="utf-8")

    print(f"Generated {total_ops} requests in {len(by_controller)} controllers")
    print(f"JSON files: {json_count}")


if __name__ == "__main__":
    main()
