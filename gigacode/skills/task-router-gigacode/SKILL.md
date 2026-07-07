---
name: task-router-gigacode
description: >-
  Маршрутизатор задач для GigaCode/Flash: по фразе пользователя выбирает скилл,
  скрипт и gate-проверку. Вставлять в начало сессии вместо длинного контекста.
---

# Маршрутизатор задач (GigaCode)

Прочитай этот файл **первым**. Выбери **одну** строку таблицы — не смешивай скиллы в одной итерации.

Рабочий каталог: **`gatling/gatlingScripts/`**.  
Массовые задачи: всегда **[one-file-at-a-time](../one-file-at-a-time/SKILL.md)**.

## Таблица маршрутизации

| Фраза / задача пользователя | Скилл | Скрипт / действие | Gate |
|-----------------------------|-------|-------------------|------|
| «вынести веса», «ProfileConfig», `WithWeight` | [gatling-weights-to-profileconfig](../gatling-weights-to-profileconfig/SKILL.md) | `weights_codemod.py` **на 1 файл** | `verify_profile.py --scenario ... --compile` |
| «собрать профиль», RPS/throttle → yaml | [build-profile-from-simulation](../build-profile-from-simulation/SKILL.md) | `sim_to_profile.py` **на 1 SCN** | `verify_profile.py --round-trip` |
| «count по логу», «заполнить profile.yaml» | [align-count-from-simulation-log](../align-count-from-simulation-log/SKILL.md) | `log_labels_to_profile.py` | `verify_profile.py --profile ... --application ...` |
| «injectOpen», «users из профиля» | [injectopen-from-profile](../injectopen-from-profile/SKILL.md) | `inject_codemod.py` **--limit 1** | `mvn test-compile` |
| «новая АС», «подключить домен» | [onboard-new-as-domain](../onboard-new-as-domain/SKILL.md) | чек-лист по шагам | verify на каждом шаге |
| «что в файле перед правкой» | [extract-before-edit](../extract-before-edit/SKILL.md) | `dump_scenario_meta.py` | exit 0, JSON прочитан |
| «разобрать прогон», simulation.log | [parse-gatling-run](../parse-gatling-run/SKILL.md) | `gatling_parser.py` + `compare_runs.py` | CSV/summary.json созданы |
| «Grafana PNG», системные метрики | [grafana-render-for-run](../grafana-render-for-run/SKILL.md) | `render_export.py` | PNG > 0 |
| «Confluence саммари» | [confluence-summary-publish](../confluence-summary-publish/SKILL.md) | `summary_to_confluence.py --dry_run` | xhtml не пуст |
| «Jenkins NT», старт/анализ | [jenkins-nt-smoke](../jenkins-nt-smoke/SKILL.md) | чек-лист параметров | пути `${GATLING_DIR}/...` |
| «ревью PR», «проверь Gatling» | [gatling-reviewer](../gatling-reviewer/SKILL.md) | `review_gates.py --strict` | RESULT: PASS |
| после **любой** правки profile/весов | [verify-gatling-profile](../verify-gatling-profile/SKILL.md) | `verify_profile.py` | **RESULT: PASS** |
| контракт имён, сомнения в ключах | [gatling-profile-conventions](../gatling-profile-conventions/SKILL.md) | только чтение | — |
| формат ответа после итерации | [session-handoff-template](../session-handoff-template/SKILL.md) | шаблон ответа | — |

## Жёсткие правила (Flash)

1. **Один файл / один SCN / один injectOpen-блок** за ответ — см. one-file-at-a-time.
2. **Не считай веса и count вручную** — только скрипты.
3. **Не пиши «готово»** без вывода gate-команды и `RESULT: PASS` (где применимо).
4. **APPLICATION = имя каталога** `profiles/<APPLICATION>/` и `cases/<APPLICATION>/`.
5. **PROFILE_YAML пусто** → `profiles/<APPLICATION>/profile.yaml`.

## Шаблон старта сессии (копируй пользователю)

```text
Режим: task-router-gigacode + one-file-at-a-time.
Задача: <одна фраза из таблицы>.
APPLICATION: <имя_АС>
Файл (если известен): <один путь>
Сделай ТОЛЬКО первую итерацию: скрипт → gate → стоп.
Не переходи дальше без моего «далее».
```

Стартовая точка репозитория: [AI_INSTRUCTION_GIGACODE.md](../../../docs/ai/AI_INSTRUCTION_GIGACODE.md).
