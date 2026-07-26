# WORKING PROTOCOL: Outside Bank Contour

## Purpose

This protocol defines how to work productively with the LT project when:

- implementation happens only inside the bank contour;
- outside contour we prepare changes, instructions, and checklists;
- files can be transferred only one-by-one.

Primary goal: stable nightly `Start + Analyze` with fewer manual steps.

## Operating Model

1. Work is planned in weekly batches.
2. Execution is done in daily micro-steps.
3. Each micro-step is built around one transferred file.
4. Every change is delivered as a reusable change packet.
5. Every change must include rollback instructions.

## One-File Intake Protocol

For each session, request exactly one next file from the working repository.

Mandatory request format:

1. File path.
2. Why this file is needed now.
3. What decision will be made after analysis.
4. What file is likely needed next (optional forecast).

Template:

```text
Next file needed: <path>
Reason: <what we are verifying/fixing right now>
Expected outcome: <decision or patch type>
Possible next file: <path or "decide after review">
```

## Change Packet Standard

Every proposed change must be delivered with all sections below.

1. Goal
- What problem is solved.
- Why it matters for nightly stability/manual effort.

2. Patch
- Exact code/config/doc patch.
- Scope boundaries (what is intentionally not changed).

3. Apply Checklist (inside contour)
- Step-by-step actions.
- Commands or UI actions.
- Expected output after each step.

4. Validation Checklist
- Preflight checks.
- Run checks (`Start` and/or `Analyze`).
- Success criteria.

5. Rollback
- How to disable/revert safely.
- Which parameters/flags restore known-good flow.

## Step-by-Step Checklist Template

Use this for each implementation task inside contour.

```text
[ ] 1. Open file: <path>
[ ] 2. Apply patch exactly as provided
[ ] 3. Save and run local check: <command/check>
[ ] 4. Run pipeline stage/check: <stage name>
[ ] 5. Confirm expected result: <observable signal>
[ ] 6. Record status in task log
[ ] 7. If failed: execute rollback steps
```

## OpenSpec-Specific Guardrails

Always keep runtime chain unchanged:

`OpenSpec -> SDD/spec.yaml -> spec_validate -> spec_to_props/spec_to_profile -> Jenkins/Gatling`

Rules:

- `meta.service` must match Jenkins `APPLICATION`.
- `endpoints[].id` must match Case variable names.
- `endpoints[].label` must match `http("...")` labels.
- If `count` is missing, use documented fallback and validate before run.
- Never skip `spec_validate.py` before test execution.

## Priority Order for Reliability Work

When under low weekly capacity, use this order:

1. `Jenkinsfile_NT_Start`
2. `Jenkinsfile_NT_Analyze_Report`
3. `SDD/ltAuto/spec_validate.py`
4. `SDD/ltAuto/spec_to_props.py`
5. Pilot `SDD/specs/.../spec.yaml`
6. Parsing/reporting scripts (`gatling_parser.py`, `compare_runs.py`)

If an incident occurs, incident-related file gets top priority.

## Weekly Cadence (Recommended)

Weekly batch:

- define top 2-3 reliability goals;
- define one-file sequence for the week;
- define done criteria and rollback points.

Daily micro-step:

- transfer one file;
- perform analysis or apply one patch;
- run one validation slice;
- log outcomes and next file request.

## Success Metrics

Track at least these two metrics:

1. Manual steps per nightly cycle.
2. Share of successful `Start + Analyze` cycles without ad-hoc fixes.

Optional:

- time-to-fix for recurring incidents;
- number of failed runs stopped at preflight (before remote execution).

## Session Output Format (from assistant)

For each session, assistant output should include:

1. `Next file needed` (single file).
2. `Why now` (decision impact).
3. `Change packet` (if enough context).
4. `Inside-contour checklist`.
5. `Rollback steps`.

## Notes

- Keep instructions in repository docs for reproducibility.
- Use mixed examples policy:
  - real files for Jenkins/Python logic;
  - sanitized/synthetic data for sensitive business content.
