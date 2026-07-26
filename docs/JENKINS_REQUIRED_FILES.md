# Jenkins Required Files Matrix

## Purpose

This document lists required repository files for key Jenkins jobs and explains:

- where each file is used (stage),
- what breaks if the file is missing,
- where the file must be placed in the repository.

Use this as a transfer checklist when files are moved one-by-one into bank contour.

---

## 1) `Jenkinsfile_NT_Start`

### Required structure
- `gatling/gatlingScripts/`
  - expected subpaths: `ltAuto/`, `src/`, `profiles/`

### Required files (always or by mode)

| File path | Stage | Required when | Why |
|---|---|---|---|
| `gatling/gatlingScripts/ltAuto/profile_to_props.py` | `Validate repository files`, `Generate profile.properties` | non-SDD mode | Generates `profile.properties` from `profile.yaml`. |
| `SDD/ltAuto/openspec_preflight.py` | `Validate repository files`, `Preflight SDD/OpenSpec` | SDD/OpenSpec mode | Fail-fast convert/validate/dry generation. |
| `SDD/ltAuto/spec_to_props.py` | `Validate repository files`, `Generate profile.properties` | SDD/OpenSpec mode | Generates `profile.properties` from `spec.yaml`. |
| `${SPEC_PATH}` (runtime path) | `Validate repository files` | SDD/OpenSpec mode | Input spec for validation and profile generation. |
| `${OPENSPEC_PATH}` (runtime path) | `Validate repository files` | OpenSpec mode | Input OpenSpec artifact for conversion. |
| `gatling/gatlingScripts/${PROFILE_YAML}` | `Validate repository files` | non-SDD mode | Input profile for non-SDD flow. |

### If missing
- Job fails early with explicit path and target stage.
- Action: add file to exact path shown in error message.

---

## 2) `Jenkinsfile_NT_Analyze_Report`

### Required structure
- `gatling/gatlingScripts/`
  - expected subpaths: `ltAuto/`, `profiles/`

### Required files

| File path | Stage | Required when | Why |
|---|---|---|---|
| `gatling/gatlingScripts/ltAuto/gatling_parser.py` | `Validate repository files`, `Parse Gatling` | always | Parses `simulation.log` to output CSV/summary. |
| `gatling/gatlingScripts/ltAuto/compare_runs.py` | `Validate repository files`, `Compare runs` | always | Compares current and previous runs. |
| `gatling/gatlingScripts/ltAuto/summary_to_confluence.py` | `Validate repository files`, `Confluence summary` | always | Publishes result summary to Confluence. |
| `SDD/ltAuto/openspec_preflight.py` | `Validate repository files`, `Prepare SDD profile` | SDD/OpenSpec mode | Preflight check before profile creation. |
| `SDD/ltAuto/spec_to_profile.py` | `Validate repository files`, `Prepare SDD profile` | SDD/OpenSpec mode | Builds `profile.from.spec.yaml`. |
| `${SPEC_PATH}` (runtime path) | `Validate repository files` | SDD/OpenSpec mode | Spec input for preflight and profile build. |
| `${OPENSPEC_PATH}` (runtime path) | `Validate repository files` | OpenSpec mode | OpenSpec input for conversion. |
| `gatling/gatlingScripts/${PROFILE_YAML}` | `Validate repository files` | non-SDD mode | Non-SDD profile input for parser SLA context. |

---

## 3) `Jenkinsfile_Gatling_Foreground_Analyze_Mail`

### Required structure
- `gatling/gatlingScripts/`
  - expected subpaths: `ltAuto/`, `src/`, `profiles/`

### Required files

| File path | Stage | Required when | Why |
|---|---|---|---|
| `gatling/gatlingScripts/ltAuto/profile_to_props.py` | `Validate repository files`, `Generate profile.properties` | non-SDD mode | Profile to properties conversion. |
| `gatling/gatlingScripts/ltAuto/gatling_parser.py` | `Validate repository files`, `Parse Gatling` | always | Parses test run log. |
| `SDD/ltAuto/openspec_preflight.py` | `Validate repository files`, `Preflight SDD/OpenSpec` | SDD/OpenSpec mode | Early validation and dry generation. |
| `SDD/ltAuto/spec_to_props.py` | `Validate repository files`, `Generate profile.properties` | SDD/OpenSpec mode | Builds runtime properties from spec. |
| `SDD/ltAuto/spec_to_profile.py` | `Validate repository files`, `Generate profile.properties` | SDD/OpenSpec mode | Builds `profile.from.spec.yaml` for parse stage. |
| `${SPEC_PATH}` (runtime path) | `Validate repository files` | SDD/OpenSpec mode | Main spec input. |
| `${OPENSPEC_PATH}` (runtime path) | `Validate repository files` | OpenSpec mode | OpenSpec input. |
| `gatling/gatlingScripts/${PROFILE_YAML}` | `Validate repository files` | non-SDD mode | Profile input in non-SDD flow. |

---

## 4) `Jenkinsfile_Grafana_Screenshots_to_Confluence`

### Required files

| File path | Stage | Required when | Why |
|---|---|---|---|
| `profiles/grafana_report.yaml` | `Resolve config` | always | Maps `APPLICATION` -> `grafana_application` + `confluence_parent_page_id`. |
| `profiles/grafana.yaml` | `Prepare bundle` | when `GRAFANA_CONFIG_B64` is empty | Datasource/dashboard config for render. |
| `ltAuto/render_export.py` | `Prepare bundle`, `Render on generator` | always | Renders Grafana screenshots. |
| `ltAuto/publish_grafana_to_confluence.py` | `Prepare bundle`, `Publish/Dry-run` | always | Builds/publishes Confluence content. |
| `resources/confluence_manger_v2.py` | `Prepare bundle`, `Publish/Dry-run` | always | Dependency of publish script. |

### Required keys inside config

In `profiles/grafana_report.yaml` for each `APPLICATION`:
- `applications.<APPLICATION>.grafana_application`
- `applications.<APPLICATION>.confluence_parent_page_id`

If key is empty/missing, job fails with explicit message.

---

## One-file transfer recommendation

When transferring by one file, use this order for fastest unblocking:

1. Jenkinsfile of current job (`Jenkinsfile_*`).
2. Script called in the first failing stage.
3. Config file for that stage (`profiles/*.yaml` or `SDD/specs/...`).
4. Dependency script imported by the previous script.

---

## Quick triage flow

1. Read first `error(...)` line in Jenkins log.
2. Copy missing path from message.
3. Place file exactly at that path in repository.
4. Re-run from start (or from safe restart point if allowed).

This repository now includes fail-fast checks to make this cycle explicit.
