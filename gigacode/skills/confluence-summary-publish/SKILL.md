---
name: confluence-summary-publish
description: >-
  Публикация краткого саммари НТ в Confluence через summary_to_confluence.py.
  Сначала --dry_run, gate — summary_confluence.xhtml. Не ходить в Confluence без dry_run.
---

# Confluence: краткое саммари прогона

Скрипт: `ltAuto/summary_to_confluence.py`  
Вход: `output/summary.json`, `output/delta_table.csv` (от [parse-gatling-run.md](../parse-gatling-run/SKILL.md)).

## Шаг 1: dry-run (обязателен для ИИ)

```bash
cd gatling/gatlingScripts

python3 ltAuto/summary_to_confluence.py \
  --summary output/summary.json \
  --delta output/delta_table.csv \
  --application efsFinmonWeb \
  --build_url "${BUILD_URL:-local}" \
  --system_metrics_dir output/systemMetrics \
  --dry_run \
  --output_dir output
```

**Gate:**
```bash
test -s output/summary_confluence.xhtml && wc -c output/summary_confluence.xhtml
```

Проверь в xhtml:
- статус PASS/FAIL;
- таблица дельт;
- `ri:filename` для PNG совпадает с файлами в `system_metrics_dir`.

## Шаг 2: публикация (только по явной задаче пользователя)

```bash
python3 ltAuto/summary_to_confluence.py \
  --summary output/summary.json \
  --delta output/delta_table.csv \
  --application efsFinmonWeb \
  --build_url "$BUILD_URL" \
  --system_metrics_dir output/systemMetrics \
  --url "$CONF_URL" \
  --space "$CONF_SPACE" \
  --page "$CONF_PAGE_SUMMARY" \
  -u "$CONF_USER" \
  -p "$CONF_PASSWORD" \
  --output_dir output
```

Креды — из Jenkins `CONF_CREDS`, **не** в репозиторий.

## Запреты для ИИ

- Не публиковать в Confluence без успешного `--dry_run`.
- Не генерировать XML/HTML вручную — только скрипт.
- Не включать пароли в ответ/handoff.

## Jenkins

Analyze-джоба вызывает скрипт после `compare_runs.py`. Параметры: `CONF_URL`, `CONF_SPACE`, `CONF_PAGE_SUMMARY`, `CONF_CREDS`.

См. [jenkins-nt-smoke.md](../jenkins-nt-smoke/SKILL.md).

## Handoff

Формат: [session-handoff-template.md](../session-handoff-template/SKILL.md) — приложи размер xhtml и список PNG.
