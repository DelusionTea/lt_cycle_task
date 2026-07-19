# Jenkins: Grafana screenshots → Confluence

Отдельная джоба рендерит PNG из Grafana через `ltAuto/render_export.py` **на генераторе нагрузки** и
создаёт новую дочернюю страницу в Confluence с блоком «Системные метрики (Grafana)».

## 1. Что куда перенести

Постоянно переносить ничего не нужно — Jenkins доставляет bundle на генератор
при каждом запуске (см. `Jenkinsfile_Grafana_Screenshots_to_Confluence`).

На генераторе должны быть доступны:
- `python3` (рекомендуется 3.8+)
- `pip install requests pyyaml`
- сетевой доступ к Grafana и Confluence

## 2. Конфиги

### 2.1 Grafana (для рендера)

Скопируйте пример и заполните:
- [`profiles/grafana.example.yaml`](profiles/grafana.example.yaml) → `profiles/grafana.yaml`

Проверьте:
- `grafana.url` (если не передаёте `GRAFANA_URL` параметром);
- `dashboards` (uid дашбордов);
- `applications` → имена datasource в Grafana.

### 2.2 Confluence parent page (по APPLICATION)

Скопируйте пример и заполните:
- `profiles/grafana_report.example.yaml` → `profiles/grafana_report.yaml`

Структура:
```yaml
applications:
  efsFinmonWeb:
    grafana_application: finmonweb
    confluence_parent_page_id: "123456789"
```

`APPLICATION` в Jenkins выбирает эти параметры.

## 3. Jenkins job

**Script Path:** `Jenkinsfile_Grafana_Screenshots_to_Confluence`

### Основные параметры

- `APPLICATION` — ключ из `profiles/grafana_report.yaml`
- `FROM_TIME`, `TO_TIME` — Unix epoch seconds (рендер переводится в мс внутри `render_export.py`)
- `GRAFANA_TOKEN_CRED` — secret text (Grafana service account token)
- `CONF_URL`, `CONF_SPACE`, `CONF_CREDS`
- `GRAFANA_URL` — если не хотите хранить URL в `profiles/grafana.yaml`
- `DRY_RUN` — сформировать XHTML без публикации в Confluence

## 4. Проверки окружения (smoke)

На генераторе:
```bash
python3 -c "import requests, yaml"
```

Проверка Grafana:
```bash
curl -sk -H "Authorization: Bearer $GRAFANA_TOKEN" "$GRAFANA_URL/api/health"
curl -sk -H "Authorization: Bearer $GRAFANA_TOKEN" "$GRAFANA_URL/api/datasources" | head -c 400
```

## 5. Требования к Grafana

На сервере Grafana должен быть установлен и запущен **Grafana Image Renderer**
(plugin/sidecar). Без него `/render/d-solo/...` не вернёт PNG.

## 6. Что делает джоба

1. Checkout репозитория.
2. Подбор `grafana_application` и `confluence_parent_page_id` из `profiles/grafana_report.yaml`.
3. Копирование bundle на генератор (`render_export.py`, `publish_grafana_to_confluence.py`,
   `confluence_manger_v2.py`, `grafana.yaml`).
4. Рендер PNG на генераторе → `output/systemMetrics`.
5. Создание дочерней страницы под `confluence_parent_page_id` и загрузка PNG.
6. Удаление `~/jenkins_job/grafana_${BUILD_NUMBER}` на генераторе.

## 7. Частые проблемы

| Симптом | Что проверить |
|---|---|
| PNG не создаются | токен Grafana, доступ до `/render/d-solo`, установлен Image Renderer |
| 401/403 Grafana | неверный токен / роль < Viewer |
| Пустые графики | неверный datasource или диапазон `FROM_TIME/TO_TIME` |
| Не создаётся страница | неверный `CONF_*` или `confluence_parent_page_id` |

Подробная изолированная проверка:  
[`docs/ISOLATED_TEST_render_and_confluence.md`](docs/ISOLATED_TEST_render_and_confluence.md)
