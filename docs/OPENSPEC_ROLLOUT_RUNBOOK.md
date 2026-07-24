# OpenSpec Rollout Runbook

## Purpose

Run OpenSpec in discovery-first bridge mode, while keeping current NT execution stable.

Runtime chain is unchanged:

`OpenSpec artifact -> openspec_to_sdd.py -> spec_validate.py -> spec_to_props/spec_to_profile -> Jenkins/Gatling`.

Reference contract: [`docs/OPENSPEC_DISCOVERY_CONTRACT.md`](docs/OPENSPEC_DISCOVERY_CONTRACT.md).

## Pilot scope

- Recommended pilot component: `ZK/ComplianceRequests`.
- Output SDD path: `SDD/specs/ZK/ComplianceRequests/spec.yaml`.

## Start job (night run) parameters

Set in `Jenkinsfile_NT_Start`:

- `USE_OPENSPEC=true`
- `OPENSPEC_PATH=SDD/specs/ZK/ComplianceRequests/openspec.raw.yaml`
- `USE_SDD_SPEC=false` (optional; OpenSpec already enables effective SDD path)
- `SPEC_PATH=SDD/specs/ZK/ComplianceRequests/spec.yaml`
- `APPLICATION=ZK`
- `packageSimulation=<target simulation>`
- `TARGET_PERCENT=100` (or desired percent)

What happens:

1. Convert OpenSpec artifact into `spec.yaml`.
2. Validate spec against Case labels/ids.
3. Generate `profile.properties`.
4. Run Gatling remotely (nohup).

## Analyze job parameters

Set in `Jenkinsfile_NT_Analyze_Report`:

- `USE_OPENSPEC=true`
- `OPENSPEC_PATH=SDD/specs/ZK/ComplianceRequests/openspec.raw.yaml`
- `SPEC_PATH=SDD/specs/ZK/ComplianceRequests/spec.yaml`
- `APPLICATION=ZK`

What happens:

1. Convert OpenSpec artifact into `spec.yaml`.
2. Validate spec.
3. Build `profile.from.spec.yaml`.
4. Parse, compare, publish summary.

## Local smoke before Jenkins

Run locally from repo root:

```bash
mkdir -p tmp/openspec-smoke
python3 SDD/ltAuto/openspec_to_sdd.py \
  --input SDD/specs/ZK/ComplianceRequests/spec.yaml \
  --output tmp/openspec-smoke/spec.yaml \
  --expected_service ZK
python3 SDD/ltAuto/spec_validate.py --spec tmp/openspec-smoke/spec.yaml
python3 SDD/ltAuto/spec_to_props.py --spec tmp/openspec-smoke/spec.yaml --output tmp/openspec-smoke/profile.properties
python3 SDD/ltAuto/spec_to_profile.py --spec tmp/openspec-smoke/spec.yaml --output tmp/openspec-smoke/profile.yaml
```

Expected result:

- converter exits `0`;
- `spec_validate.py` returns `PASS`;
- `profile.properties` and `profile.yaml` are generated.

## Equivalence criteria vs current SDD flow

Pilot is accepted when:

- same endpoint coverage (`endpoints.id` / `endpoints.label`);
- `spec_validate.py` remains green;
- `profile.properties` has expected keys and no missing counts;
- Analyze job creates standard outputs (`summary.json`, `delta_table.csv`, `test_result.csv`);
- Confluence publish behavior does not regress.

## Rollback (safe)

If discovery or conversion is unstable:

1. Set `USE_OPENSPEC=false`.
2. Keep `USE_SDD_SPEC=true` and fixed `SPEC_PATH` to the last known-good `spec.yaml`.
3. Re-run Start/Analyze with previous SDD-only path.

No rollback requires code revert in Gatling runtime scripts.

## Scale-out checklist

- Freeze one OpenSpec artifact template for each AS/domain.
- Add one component at a time.
- Keep `meta.service` aligned with Jenkins `APPLICATION` and Grafana config keys.
- Gate every new component with local smoke and one full Start/Analyze cycle.
