---
name: sdd-spec-writer
description: Create SDD spec.yaml/spec.md from existing Gatling Case classes without inventing data. Use when asked to draft SDD specs or manual spec-driven load testing documentation.
disable-model-invocation: true
---

# SDD Spec Writer

## Quick Start

You must produce **two files**:
- `SDD/specs/<АС>/<Component>/spec.yaml`
- `SDD/specs/<АС>/<Component>/spec.md`

Never invent endpoints, paths, labels, counts, SLA, or data fields. If missing, write `TODO` in `spec.md` and stop.

## Required Inputs (must be present)

1) Case-класс: `gatling/src/test/java/cases/<АС>/<Component>Case.java`  
2) Профиль (если существует): `profiles/<АС>/<Component>/profile.yaml`  
3) Сценарий: `gatling/src/test/java/scenarios/<АС>/<Component>Scenario.java`

If any input is missing, stop and report which file is missing.

## Extract From Case Class

For each request:
- `id` = variable name after `public static HttpRequestActionBuilder`
- `label` = string inside `http("...")`
- `method` and `path` = `.get("/...")`, `.post("/...")`, `.put("/...")`, etc.
- `query` = `.queryParam(...)` (copy exact key/value or file path)
- `body` = `.body(ElFileBody("..."))` (copy exact file path)
- `checks.status` = `.check(status().is(<code>))` (default 200 if shown)

## Extract From Profile (if exists)

Copy as-is:
- `target_percent`, `rampup`, `95pct`, `50pct`, `rps`, `error_count`
- `count`
- `sla_per_label`
- `injection` block

If profile is missing, do not invent numeric values. Put TODO in `spec.md`.

## spec.yaml Template (DO NOT CHANGE STRUCTURE)

```yaml
meta:
  service: <АС>
  component: <Component>
  scenario: <ScenarioName>
  source: manual
  swagger: false
  description: "<кратко>"

request_classes:
  - src/test/java/cases/<АС>/<Component>Case.java

data_sources:
  feeders:
    - name: <feeder_name>
      source: <класс/метод>
      required_fields: [<field1>, <field2>]
      fallback: stub

profile:
  target_percent: <from profile.yaml>
  rampup: <from profile.yaml>
  thresholds:
    pct95: <from profile.yaml>
    pct50: <from profile.yaml>
    rps: <from profile.yaml>
    error_count: <from profile.yaml>
  count:
    <var_name>: <number>
  sla_per_label: {}

injection:
  duration: <from profile.yaml>
  rampup: <from profile.yaml>
  scenarios:
    <ScenarioName>:
      users: <from profile.yaml>
      request_classes:
        - src/test/java/cases/<АС>/<Component>Case.java

endpoints:
  - id: <var_name>
    label: <http("...")>
    method: <GET|POST|PUT|DELETE|PATCH>
    path: <"/api/...">
    query:
      <key>: <value or file>
    body: <file path if used>
    checks:
      status: 200
```

## spec.md Template

```
## <Component> (<АС>)

### Бизнес-поток
- 2–4 пункта.

### Данные и зависимости
- feeders, обязательные поля.

### Ограничения и допущения
- TODO для неизвестного.
```

## Validation (must run)

Run:
`python3 SDD/ltAuto/spec_validate.py --spec SDD/specs/<АС>/<Component>/spec.yaml`

If validation fails, fix only by aligning `id`, `label`, and `count` with Case-класс.
Do not invent values.
