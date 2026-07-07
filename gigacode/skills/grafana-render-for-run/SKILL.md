---
name: grafana-render-for-run
description: >-
  Экспорт PNG системных метрик из Grafana по window.json прогона через render_export.py.
  Не править PromQL вручную. Gate — наличие PNG в results_path.
---

# Grafana: рендер метрик прогона

Скрипт: `ltAuto/render_export.py` (Grafana 11.x, token из env/credentials).

Используется в **Analyze**-джобе при `ENABLE_GRAFANA=true`.

## Входы

| Параметр | Откуда |
|----------|--------|
| `window.json` | `output/window.json` после `gatling_parser.py` |
| `GRAFANA_CONFIG` | `profiles/grafana.yaml` (относительно GATLING_DIR) |
| `GRAFANA_APPLICATION` | ключ АС в grafana.yaml (≠ обязательно APPLICATION) |
| `GRAFANA_TOKEN` | env или `--token` (не коммитить) |
| `--from_time` / `--to_time` | из window.json (миллисекунды) |

## Команда (локально)

```bash
cd gatling/gatlingScripts

# from/to из output/window.json
FROM=$(python3 -c "import json; print(json.load(open('output/window.json'))['from'])")
TO=$(python3 -c "import json; print(json.load(open('output/window.json'))['to'])")

export GRAFANA_TOKEN="<token>"

python3 ltAuto/render_export.py \
  --application <GRAFANA_APPLICATION> \
  --from_time "$FROM" \
  --to_time "$TO" \
  --config profiles/grafana.yaml \
  --results_path output/systemMetrics \
  --render_only_most_loaded_pods 1
```

Опционально `--grafana_url https://...` если не задан в yaml.

## Gate

```bash
find output/systemMetrics -name '*.png' | wc -l
# ожидается > 0
```

При частичных ошибках панелей скрипт может завершиться 0 с WARN — проверь количество PNG.

## Запреты для ИИ

- Не хардкодить token/URL в код или коммит.
- Не переписывать PromQL/дашборды «для фикса» — только параметры CLI.
- Не рендерить без окна прогона — сначала [parse-gatling-run.md](../parse-gatling-run/SKILL.md).

## Jenkins

См. [jenkins-nt-smoke.md](../jenkins-nt-smoke/SKILL.md): `ENABLE_GRAFANA`, `GRAFANA_CONFIG`, `GRAFANA_APPLICATION`, `GRAFANA_TOKEN_CRED`.

## Связанные скиллы

- Парс прогона: [parse-gatling-run.md](../parse-gatling-run/SKILL.md)
- Confluence (вложения PNG): [confluence-summary-publish.md](../confluence-summary-publish/SKILL.md)
