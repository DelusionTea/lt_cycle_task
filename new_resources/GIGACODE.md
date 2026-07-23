# Repository Guidelines

## Overview

This repository holds load testing and performance monitoring artifacts for multiple banking domains. The primary tool is **Gatling** (Scala/Java DSL), orchestrated via **Jenkins** pipelines and supplemented by **Python** data loaders, a **Spring Boot** generator monitor, and **Kafka** integration.

---

## Project Structure & Module Organization

| Path | Purpose |
|------|---------|
| `gatling/gatlingScripts/` | **Maven project** (`pom.xml`) — Gatling simulations, feeders, scenarios, and resources |
| `gen_status-main/` | **Spring Boot** web service (`pom.xml`) — monitors Gatling generators via SSH |
| `send_XML_to_kafka/` | **Java** app (`pom.xml`) — sends XML messages to Kafka |
| `Loaders/` | **Python** scripts — seeds test databases (arbitrage, compliance, credit history, etc.) |
| `Jenkinsfile_*` | Root-level Jenkins pipelines — orchestrate test execution, cleanup, reporting |
| `certs/` | SSL/TLS certificates (PKCS12 keystore) |
| `SLA/` | YAML files with per-endpoint SLA targets |

**Source code layout** (`gatling/gatlingScripts/src/test/java/`):
```
cases/        — Test case logic (per AS domain)
feeders/      — Data feeders (per domain)
scenarios/    — Test scenarios (per domain)
simulations/  — Entry points (selected in Jenkins)
resources/    — Config, CSV, JSON, profiles, uploadFiles, certs
```

---

## Build, Test & Development Commands

**Primary project** — run from `gatling/gatlingScripts/`:

| Command | Action |
|---------|--------|
| `mvn clean test` | Compile and run Gatling simulations |
| `mvn gatling:test` | Execute Gatling test via plugin |
| `mvn clean package -DskipTests` | Package without running tests |

**Monitor service** — run from `gen_status-main/`:
```
mvn spring-boot:run    # Start on port 8088
```

---

## Coding Style & Naming Conventions

- **Java 11+** — `maven.compiler.release=11`, source/target 15
- **Gatling classes** follow PascalCase: `FinmonWebSimulation`, `PprbComplianceRequestsFeeder`
- **Directories** mirror domain names: `efsFinmonWeb/`, `pprbComplianceRequests/`
- **Python** uses `snake_case` for files and functions
- **Jenkinsfile** uses declarative pipeline style with `stages {}` blocks
- **YAML config** uses kebab-case keys (e.g., `connect-timeout-ms`)
- **No formal linter** — rely on Maven compile (`mvn clean compile`) for Java/Python

---

## Testing Guidelines

- **Gatling** tests live under `src/test/java/` (standard Maven layout)
- **Simulation classes** are the entry points — named `<Domain>Simulation.java`
- **Feeders** provide test data via CSV or feeder-builder patterns
- **Jenkins** selects which simulation to run via an interactive 2-step menu
- **No unit test framework** is declared — validation comes from Gatling execution reports

---

## Commit & Pull Request Guidelines

From the project's Git history and conventions:

- **Commit messages** should be descriptive of *what* changed and *why* (e.g., "Add starting-soon endpoint to scheduler", "Fix connection timeout in SSH service")
- **Jenkinsfiles** are the primary CI/CD artifact — any pipeline change must be tested in a sandbox first
- **Pull requests** should reference the corresponding Jira/issue and include evidence of a passing Gatling run
- **Avoid** committing compiled artifacts (`target/`, `results/`, `analysis_out/`) — they are in `.gitignore`

---

## Agent-Specific Instructions

When working with GigaCode on this repository:

1. **Always** check for an existing `.gigacode/skills/<name>/SKILL.md` before editing — a skill may already define the workflow
2. **Prefer** the `sdd-spec-writer` and `gatling-profile-conventions` skills when authoring or modifying load profiles
3. **Never** commit `.env`, credential files, or sensitive configuration (the `certs/` directory is excluded)
4. **Run** `mvn clean compile` after any Java change to verify compilation
5. **Keep** Jenkinsfile changes reversible — wrap critical operations in `post { aborted { ... } }` blocks