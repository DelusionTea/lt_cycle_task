# OpenSpec Discovery Contract (Bridge to SDD)

## Scope

This document fixes the minimum contract for `OpenSpec/GigaCLI -> SDD/spec.yaml` in bridge mode.
Execution remains unchanged:

`openspec_to_sdd.py -> spec_validate.py -> spec_to_props.py/spec_to_profile.py`.

## Discovery inputs (required)

GigaCLI discovery must provide one artifact in YAML or JSON with these required fields:

- `service` (or `meta.service`) -> target AS (must match Jenkins `APPLICATION`).
- `component` (or `meta.component`) -> component name.
- `scenario` (or `meta.scenario`) -> Gatling scenario name.
- `request_classes` -> Case classes used by scenario.
- `endpoints[]` with:
  - `id` (Case variable name);
  - `label` (`http("...")` label from Case);
  - `method` (GET/POST/PUT/...);
  - `path`.

Optional but recommended:

- `profile.count` (requests/hour at 100% load).
- `profile.thresholds`, `profile.target_percent`, `profile.rampup`.
- `injection` block (`duration`, `rampup`, `scenarios`).
- `meta.description`, `meta.swagger`.

## Accepted source shapes by converter

`SDD/ltAuto/openspec_to_sdd.py` accepts:

1. **Normalized OpenSpec shape** (`endpoints[]` list).
2. **OpenAPI-like shape** (`paths` object with `operationId`/`x-label`/`summary`).

Aliases resolved automatically:

- service: `meta.service | service | application | system`
- component: `meta.component | component | domain | name`
- scenario: `meta.scenario | scenario | load_scenario`
- request classes: `request_classes | requestClasses | cases`
- counts: `profile.count | load.count | counts | traffic.count`

## Mapping OpenSpec -> SDD

| OpenSpec input | SDD output |
| --- | --- |
| `meta.service` / `service` | `meta.service` |
| `meta.component` / `component` | `meta.component` |
| `meta.scenario` / `scenario` | `meta.scenario` |
| `meta.description` / `description` | `meta.description` |
| `meta.source` / `source` | `meta.source` (default: `openspec-gigacli`) |
| `request_classes` | `request_classes` |
| `endpoints[]` | `endpoints[]` |
| `profile.count` (or aliases) | `profile.count` |
| `profile.thresholds` | `profile.thresholds` |
| `profile.target_percent`, `profile.rampup` | `profile.target_percent`, `profile.rampup` |
| `injection` | `injection` |

## Fallback rules

- If `profile.count` is missing, converter writes zero counts for each `endpoints[].id`.
- If thresholds are missing, defaults are applied:
  - `pct95=1000`, `pct50=500`, `rps=1`, `error_count=100`.
- If `injection` is missing, a safe default block is written.
- If `meta.source` is missing, converter sets `openspec-gigacli`.
- Hard validation is not replaced: `spec_validate.py` remains mandatory gate.

## GigaCLI command capture template

Record the exact team command in CI/docs (replace placeholders):

```bash
gigacli openspec export \
  --service <AS> \
  --component <Component> \
  --format yaml \
  --output SDD/specs/<AS>/<Component>/openspec.raw.yaml
```

Then convert:

```bash
python3 SDD/ltAuto/openspec_to_sdd.py \
  --input SDD/specs/<AS>/<Component>/openspec.raw.yaml \
  --output SDD/specs/<AS>/<Component>/spec.yaml \
  --expected_service <AS>
```

## Non-goals for this stage

- No direct runtime from OpenSpec in Jenkins/Gatling.
- No replacement of `spec_validate.py`.
- No changes in `gatling_parser.py` processing logic.
