---
name: parse-gatling-run
description: >-
  Разбор прогона НТ: simulation.log → CSV через gatling_parser.py, сравнение с
  предыдущим через compare_runs.py. Для Analyze-джобы и локальной отладки.
  APPLICATION = каталог profiles/<APPLICATION>/.
---

# Разбор прогона Gatling (parse + compare)

Поток ночного цикла: **simulation.log → gatling_parser → compare_runs → summary/mail**.

Рабочий каталог: `gatling/gatlingScripts/` (или корень с `ltAuto/` и фикстурой `resources/`).

## Входы

| Параметр | Значение |
|----------|----------|
| `simulation.log` | из tarball прогона или `resources/simulation.log` |
| `profile` | `profiles/<APPLICATION>/profile.yaml` |
| `APPLICATION` | имя АС = каталог profiles/ |
| `target_percent` | из Jenkins или yaml (по умолчанию 100) |

## Шаг 1: парс лога

```bash
cd gatling/gatlingScripts   # или корень scaffold

python3 ltAuto/gatling_parser.py \
  --simulation_log /path/to/simulation.log \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --target_percent 100 \
  --output_dir output \
  --script_name efsFinmonWeb \
  --rampup 0
```

**Выход** (в `output/`):
- `rps_response_table.csv` — основная таблица для compare
- `test_result.csv` — SLA pass/fail
- `window.json` — окно для Grafana (`from`/`to`)

Gate: файлы существуют, в логе парсера нет красной ошибки «пустой simulation.log».

## Шаг 2: сравнение с предыдущим прогоном

```bash
python3 ltAuto/compare_runs.py \
  --current output/rps_response_table.csv \
  --previous history/<APPLICATION>/previous/rps_response_table.csv \
  --test_result output/test_result.csv \
  --current_run_id <runId> \
  --previous_run_id <prevRunId> \
  --output_dir output
```

Если предыдущего прогона нет — `--previous` опустить (первый прогон).

**Выход:**
- `delta_table.csv`
- `regressions.csv`
- `summary.json` — для Confluence/mail

## Шаг 3: verify профиля (рекомендуется)

```bash
python3 ltAuto/verify_profile.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --application efsFinmonWeb
```

Скилл: [verify-gatling-profile.md](../verify-gatling-profile/SKILL.md).

## Локальная отладка (scaffold)

```bash
python3 ltAuto/gatling_parser.py \
  --simulation_log resources/simulation.log \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --output_dir /tmp/nt_out \
  --script_name test
```

## Запреты для ИИ

- Не подставлять `profiles/profile.yaml` (устаревший путь) — только `profiles/<APPLICATION>/`.
- Не пересчитывать RPS/percentiles вручную — доверять CSV.
- Не менять SLA в yaml «чтобы прошло» без задачи пользователя.

## Связанные скиллы

- count по логу: [align-count-from-simulation-log.md](../align-count-from-simulation-log/SKILL.md)
- Grafana: [grafana-render-for-run.md](../grafana-render-for-run/SKILL.md)
- Confluence: [confluence-summary-publish.md](../confluence-summary-publish/SKILL.md)
- Jenkins: [jenkins-nt-smoke.md](../jenkins-nt-smoke/SKILL.md)
