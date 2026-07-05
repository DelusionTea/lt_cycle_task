# Изолированная проверка `render_export.py` и `summary_to_confluence.py`

Инструкция для проверки двух скриптов **без Jenkins и без удалённого генератора** —
локально, по одному сервису/странице. Цель — убедиться, что рендер метрик из
Grafana и публикация саммари в Confluence работают, прежде чем включать их в пайплайн.

> Какой render_export проверять. Рабочая версия — **`ltAuto/render_export.py`**
> (её вызывает пайплайн: поддерживает `--config`, `--token`, env, слаг в d-solo,
> прокси-datasource). `resources/render_export.py` — исходная legacy-версия с
> захардкоженными URL/токеном; проверяется отдельно только для сравнения (раздел 4).

---

## 0. Общая подготовка окружения (один раз)

> Пути. В этом scaffold скрипты лежат в корне (`ltAuto/`, `profiles/`, `resources/`) —
> команды ниже даны для него. В боевом репозитории всё это внутри
> `gatling/gatlingScripts/` (`ltAuto/`, `profiles/`, `src/`), а `confluence_manger_v2.py`
> лежит в `ltAuto/`. Там запускайте из каталога `gatling/gatlingScripts` (тогда
> относительные пути `ltAuto/...`, `profiles/...` совпадут с примерами).

```bash
cd /path/to/LT                      # scaffold-корень (папки ltAuto/, profiles/, resources/)
                                    # в боевом репо: cd .../gatling/gatlingScripts
python3 -m venv .venv
. .venv/bin/activate
pip install requests pyyaml pandas  # requests+pyyaml — для render; pandas — для парсера
```

Проверка, что скрипты хотя бы импортируются/показывают справку:

```bash
python3 ltAuto/render_export.py --help
python3 ltAuto/summary_to_confluence.py --help
```

---

## 1. Изолированная проверка `ltAuto/render_export.py`

Скрипту нужна **сетевая доступность до Grafana** и **service account token**
(роль Viewer или выше). Больше ничего (ни Jenkins, ни генератора) не требуется.

### 1.1. Данные, которые надо получить заранее
- `GRAFANA_URL` — базовый адрес Grafana (например `https://grafana.example:3000`).
- `GRAFANA_TOKEN` — service account token (в Grafana: *Administration → Service
  accounts → Add token*). Роль ≥ Viewer.
- Ключ АС (`--application`) и его datasource — из `profiles/grafana.yaml`
  (скопируйте из `profiles/grafana.example.yaml` и поправьте `url`/`applications`/`dashboards`).

### 1.2. Экспорт переменных окружения
```bash
export GRAFANA_URL="https://grafana.example:3000"
export GRAFANA_TOKEN="glsa_xxx_your_service_account_token"
```

### 1.3. Пре-чек связи и авторизации (curl, до запуска скрипта)
```bash
# health — должно вернуть {"database":"ok",...}
curl -sk -H "Authorization: Bearer $GRAFANA_TOKEN" "$GRAFANA_URL/api/health"; echo

# список datasource — должен вернуть JSON-массив (проверка прав токена)
curl -sk -H "Authorization: Bearer $GRAFANA_TOKEN" "$GRAFANA_URL/api/datasources" | head -c 400; echo
```
- `401/403` → проблема с токеном/правами (см. §1.7).
- Пустой ответ/таймаут → нет сети до Grafana из этой машины.

### 1.4. Временнóе окно (мс)
Для теста возьмём последний час:
```bash
FROM_MS=$(( ($(date +%s) - 3600) * 1000 ))
TO_MS=$(( $(date +%s) * 1000 ))
echo "$FROM_MS $TO_MS"
```
(Если проверяете конкретный прошедший прогон — возьмите `from_ms/to_ms` из
`output/window.json`, который делает `gatling_parser.py`.)

### 1.5. Запуск (безопасный первый прогон)
Первый прогон — с `--render_only_most_loaded_pods 0` (не ходить за «самым
нагруженным подом», меньше зависимостей) и в отдельную папку:

```bash
python3 ltAuto/render_export.py \
    --config profiles/grafana.yaml \
    --application debug \
    --from_time "$FROM_MS" \
    --to_time "$TO_MS" \
    --results_path /tmp/grafana_test \
    --render_only_most_loaded_pods 0
```

При проблемах с datasource попробуйте прямой доступ вместо прокси:
```bash
    ... --use_proxy 0
```

### 1.6. Что проверить (критерии успеха)
- В конце лога: `Рендер завершён: успешно N, ошибок M` (N > 0).
- Появились картинки:
  ```bash
  find /tmp/grafana_test -name '*.png' -ls
  ```
- Откройте любой PNG — на графике должны быть данные за выбранное окно
  (а не пустой холст / «No data»).
- Код возврата: `echo $?` → `0`. (`1` — если ни одна панель не отрендерилась.)

### 1.7. Разбор частых проблем
| Симптом | Причина / что делать |
|---|---|
| `Не задан токен Grafana` | не экспортирован `GRAFANA_TOKEN` (или `--token`). |
| `401/403` на API или рендере | токен без прав/просрочен; нужна роль ≥ Viewer. |
| PNG не сохранился, в логе `status=302/404`, `type=text/html` | проблема URL рендера: проверьте, что дашборд-uid в `grafana.yaml.dashboards` верный; слаг подставляется автоматически. |
| Пустая картинка «No data» | нет метрик за окно `from/to`, либо не тот datasource/АС. |
| `Не найдено подов…` / пустые списки | не тот datasource в `applications`, либо окно без данных; попробуйте `--use_proxy 0`. |
| `image-renderer` ошибка | не установлен/не запущен плагин рендера в Grafana. |

---

## 2. Изолированная проверка `ltAuto/summary_to_confluence.py`

Скрипт умеет два режима:
- **`--dry_run`** — без сети: строит storage-XML и сохраняет в файл (проверка вёрстки).
- **боевой** — публикует на страницу Confluence (нужны доступ и `confluence_manger_v2.py`).

Скрипту на вход нужны `summary.json` и `delta_table.csv`. Их можно либо
сгенерировать из реального лога (реалистично), либо взять минимальные заглушки.

### 2.1. Готовим входные данные из реального лога (рекомендуется)
```bash
mkdir -p /tmp/conf_test
# 1) распарсить лог -> output-таблицы
python3 ltAuto/gatling_parser.py \
    --simulation_log "resources/simulation copy.log" \
    --profile profiles/profile.yaml \
    --output_dir /tmp/conf_test \
    --script_name isolated_test || true      # exit 2 при непопадании в SLA — это ок для теста

# 2) сравнить (первый прогон — без --previous) -> summary.json, delta_table.csv
python3 ltAuto/compare_runs.py \
    --current /tmp/conf_test/rps_response_table.csv \
    --error_threshold 100 \
    --test_result /tmp/conf_test/test_result.csv \
    --current_run_id isolated-1 \
    --output_dir /tmp/conf_test

ls -l /tmp/conf_test/summary.json /tmp/conf_test/delta_table.csv
```

### 2.2. Dry-run без картинок (проверка вёрстки XML)
```bash
python3 ltAuto/summary_to_confluence.py \
    --summary /tmp/conf_test/summary.json \
    --delta   /tmp/conf_test/delta_table.csv \
    --application isolated_test \
    --dry_run \
    --output_dir /tmp/conf_test
```
Проверить:
```bash
echo "символов:"; wc -c /tmp/conf_test/summary_confluence.xhtml
# бегло глянуть структуру (статус-панель, таблица дельты)
head -c 1200 /tmp/conf_test/summary_confluence.xhtml; echo
```
Ожидаемо: файл `summary_confluence.xhtml` создан, содержит статус-панель и
раскрывающийся блок «Дельта по измерениям».

### 2.3. Dry-run с картинками системных метрик
Создадим пару «картинок» (можно взять реальные из §1 или сделать заглушки):
```bash
mkdir -p /tmp/conf_test/sysmetrics/os_cmpl/pod-a/all
printf 'PNG' > /tmp/conf_test/sysmetrics/os_cmpl/pod-a/all/panel_1.png
printf 'PNG' > /tmp/conf_test/sysmetrics/os_cmpl/pod-a/all/panel_2.png

python3 ltAuto/summary_to_confluence.py \
    --summary /tmp/conf_test/summary.json \
    --delta   /tmp/conf_test/delta_table.csv \
    --application isolated_test \
    --system_metrics_dir /tmp/conf_test/sysmetrics \
    --dry_run \
    --output_dir /tmp/conf_test

# в XML должны появиться макросы вложений картинок:
grep -o 'ri:filename="[^"]*"' /tmp/conf_test/summary_confluence.xhtml
```
Ожидаемо: в логе `картинок: 2`, в XML — `ri:filename="os_cmpl__pod-a__all__panel_1.png"` и т.п.

### 2.4. Боевая публикация в Confluence (по желанию, нужен доступ)
Нужны: URL Confluence, space key, логин/пароль (или токен как пароль), ID тестовой
страницы. И доступный модуль `confluence_manger_v2.py` (в боевом репо лежит рядом,
в `ltAuto/`; в scaffold — в `resources/`; скрипт ищет его в обоих местах).

Создайте **отдельную тестовую страницу** в Confluence и возьмите её `pageId`
(из URL `.../pages/viewpage.action?pageId=NNN` или из `...>tiny/...`).

```bash
python3 ltAuto/summary_to_confluence.py \
    --summary /tmp/conf_test/summary.json \
    --delta   /tmp/conf_test/delta_table.csv \
    --application isolated_test \
    --system_metrics_dir /tmp/conf_test/sysmetrics \
    --url   "https://confluence.example" \
    --space "SPACEKEY" \
    -u "$CONF_USER" -p "$CONF_PASS" \
    -pg "123456789"
```
Проверить:
- лог: `Страница 123456789 обновлена (версия X, картинок: N)`;
- открыть страницу в браузере: статус-панель, таблица дельты, блок «Системные
  метрики (Grafana)» с картинками.

### 2.5. Разбор частых проблем
| Симптом | Причина / что делать |
|---|---|
| `Не найден confluence_manger_v2` | модуль недоступен; положите `confluence_manger_v2.py` в `ltAuto/` (боевой вариант) либо в `resources/`. |
| `KeyError` при чтении summary/delta | подали не те файлы; сгенерируйте их через §2.1 (форматы должны быть от `compare_runs.py`). |
| `401/403` при публикации | неверные логин/пароль или нет прав на страницу/space. |
| Картинки не видны на странице | вложения грузятся ДО обновления контента; проверьте, что `--system_metrics_dir` не пуст и имена файлов совпали с `ri:filename` в XML. |
| Страница не меняется | неверный `pageId`; проверьте, что это ID именно тестовой страницы. |

---

## 3. Связка render → summary (полу-интеграционная проверка)
Чтобы проверить оба скрипта вместе (как в analyze-джобе, но локально):
```bash
# 1) отрендерить метрики в ту же папку output
python3 ltAuto/render_export.py --config profiles/grafana.yaml \
    --application debug --from_time "$FROM_MS" --to_time "$TO_MS" \
    --results_path /tmp/conf_test/sysmetrics --render_only_most_loaded_pods 0
# 2) собрать саммари с этими картинками (dry-run)
python3 ltAuto/summary_to_confluence.py \
    --summary /tmp/conf_test/summary.json --delta /tmp/conf_test/delta_table.csv \
    --application isolated_test --system_metrics_dir /tmp/conf_test/sysmetrics --dry_run \
    --output_dir /tmp/conf_test
```
Успех: в `summary_confluence.xhtml` появились `ri:filename` для реально
отрендеренных панелей.

---

## 4. (Опционально) Проверка legacy `resources/render_export.py`
Только для сравнения — эта версия с захардкоженными `api_url_base` и `api_key`
внутри файла и без слага в d-solo. Запуск:
```bash
python3 resources/render_export.py --application debug
```
Ожидаемо на Grafana 11.6.2: рендер может падать/возвращать пустые картинки
(отсутствие слага в `/render/d-solo`, прямой доступ к datasource). Это и есть
причина перехода на `ltAuto/render_export.py`. Для боевого использования берите
именно версию из `ltAuto/`.

---

## 5. Уборка
```bash
rm -rf /tmp/grafana_test /tmp/conf_test
deactivate   # выйти из venv
```

Полная техкарта пайплайна — в `docs/NT_PIPELINE_AI_CONTEXT.md`,
пошаговое внедрение — в `docs/NT_PIPELINE_TASK.md`.
