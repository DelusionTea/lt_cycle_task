# GigaCode Skills — Gatling / НТ-пайплайн

Скиллы для агента GigaCode. Каждый скилл — отдельная папка с **`SKILL.md`** (обязательный entrypoint).

```text
gigacode/skills/
├── task-router-gigacode/     ← старт сессии: выбор скилла по задаче
├── one-file-at-a-time/         ← обязателен при массовых правках
├── gatling-reviewer/           ← ревью PR для коллег
├── verify-gatling-profile/
└── ...
```

## Как использовать в GigaCode

```
@task-router-gigacode
```
или
```
skill: gatling-reviewer
Модуль: pprbSberrating
```

Стартовая инструкция для ИИ (контекст репозитория): [docs/ai/AI_INSTRUCTION_GIGACODE.md](../docs/ai/AI_INSTRUCTION_GIGACODE.md).

## Список скиллов

| Скилл | Назначение |
|-------|------------|
| `task-router-gigacode` | Маршрут «задача → скилл → скрипт → gate» |
| `session-handoff-template` | Формат ответа после итерации |
| `one-file-at-a-time` | Один файл за итерацию + gate |
| `gatling-profile-conventions` | Контракт имён var↔log↔yaml |
| `extract-before-edit` | `dump_scenario_meta.py` перед правкой |
| `gatling-weights-to-profileconfig` | Codemod весов |
| `build-profile-from-simulation` | profile.yaml из RPS |
| `align-count-from-simulation-log` | count по simulation.log |
| `injectopen-from-profile` | inject_codemod |
| `onboard-new-as-domain` | Новая АС |
| `verify-gatling-profile` | Gate verify_profile.py |
| `parse-gatling-run` | gatling_parser + compare_runs |
| `grafana-render-for-run` | render_export |
| `confluence-summary-publish` | Confluence dry-run |
| `jenkins-nt-smoke` | Jenkins NT чек-лист |
| `gatling-reviewer` | Ревью PR (gates + чек-лист) |

Рабочий каталог команд: `gatling/gatlingScripts/` (там `pom.xml`, `ltAuto/`, `profiles/`).
