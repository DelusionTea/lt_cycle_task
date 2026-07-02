# НТ-пайплайн (бесконечный цикл) — сводка для ИИ

Назначение файла: дать ИИ полный контекст пайплайна непрерывного нагрузочного
тестирования (НТ), чтобы после отладки он понимал **что и где править**. Тут — карта
файлов, поток данных, точки интеграции, контракты (имена полей/property/CSV-колонок),
известные допущения и открытые места.

## 1. Что это и зачем

Цель: каждую ночь автоматически прогонять НТ (Gatling), анализировать результат,
сравнивать с предыдущим прогоном, публиковать краткое саммари в Confluence,
слать письмо, вести историю в Bitbucket. Дополнительно — рендер системных метрик
из Grafana 11.6.2.

Стек контура: Gatling (Java/Maven) + Jenkins (declarative) + удалённый генератор
(SSH, запуск через `nohup`) + Bitbucket (история) + Confluence + Grafana.

## 2. Архитектура: две Jenkins-джобы

Тест запускается на удалённой машине через `nohup`, чтобы пережить тех. работы
агентов Jenkins. Поэтому запуск и анализ **разделены**:

1. `Jenkinsfile_NT_Start` — стартует тест в фоне и быстро освобождает агент.
2. `Jenkinsfile_NT_Analyze_Report` — позже переподцепляется к завершённому прогону
   по `gatling_status.txt` + `gatling_report.tar.gz`, анализирует и репортит.

Связь между ними — только через файлы-состояния на удалённом хосте (status/tarball)
и историю в Bitbucket. Прямой зависимости/передачи переменных нет (это by design).

## 3. Поток данных (контур)

```
TARGET_PERCENT + profiles/profile.yaml (count) + Java Case-классы (request_classes)
        │ profile_to_props.py: веса = доли count по членам сценария (%)
        ▼
profile.properties ──scp──► remote: gatling_job/gatlingScripts/profile.properties
        │ (Start job: wrapper + nohup mvn gatling:test -DprofileProperties=...)
        ▼
ProfileConfig.java читает profile.properties → веса randomSwitch + интенсивность
        ▼
Gatling прогон → results/<runId>/simulation.log → tar.gz на remote
        │ (Analyze job: scp tarball, распаковка)
        ▼
gatling_parser.py  → output/{rps_response_table,rps_table,response_table,
                              checks_results,test_result}.csv + window.json
        ▼
compare_runs.py (current vs Bitbucket latest) → delta_table.csv, regressions.csv, summary.json
        │
        ├─ (опц.) render_export.py (окно из window.json) → output/systemMetrics/**.png
        ▼
summary_to_confluence.py → обновляет страницу Confluence (+ вложения картинок)
        ▼
mail (статус + ссылка) ; история коммитится в Bitbucket
```

## 4. Файлы и их роль (что править — здесь)

### Jenkins
- `Jenkinsfile_NT_Start` — старт-джоба. Cron `H 22 * * *`. Параметры: `START_TIME`,
  `packageSimulation`, `TARGET_PERCENT`, `PROFILE_YAML`, `UNATTENDED`, `CREDS`.
  Ключевое: стадия `Generate profile.properties` (вызов `profile_to_props.py`),
  стадия `Run test (nohup)` генерит `gatling_wrapper.sh` (status/lock/pid/tarball).
  Remote-пути в `environment{}` (см. `REMOTE_*`). `post { aborted }` — аварийная
  очистка (kill дерева процессов, сброс lockfile).
- `Jenkinsfile_NT_Analyze_Report` — анализ. Cron `H 2 * * *`. Стадии:
  `Fetch completed run` (проверка status SUCCESS/FAILED, иначе `NOT_BUILT`),
  `Clone history (Bitbucket)` (+защита от повторной обработки `last_processed.txt`),
  `Parse Gatling`, `Grafana system metrics` (под `when {ENABLE_GRAFANA}`),
  `Compare runs`, `Persist history (Bitbucket)`, `Confluence summary`, `Notify (mail)`.

### Python (`ltAuto/`)
- `gatling_parser.py` — парс `simulation.log` (TSV: RUN/USER/REQUEST/GROUP).
  Аргументы: `--simulation_log --profile --target_percent --output_dir --rampup
  --script_name --silence`. Выход (контракт downstream):
  `rps_response_table.csv` (главная таблица для compare), `rps_table.csv`,
  `response_table.csv`, `checks_results.csv`, `test_result.csv` (1/0),
  `window.json` (`{"from_ms":..,"to_ms":..}` — полное окно прогона для Grafana).
- `compare_runs.py` — сравнение текущего и предыдущего `rps_response_table.csv`.
  Аргументы: `--current --previous --error_threshold --test_result
  --current_run_id --previous_run_id --output_dir`. Выход: `delta_table.csv`
  (колонки: `label,rps_prev,rps_cur,d_rps,err_prev,err_cur,d_err_pp,
  pct95_prev,pct95_cur,d_pct95,status`), `regressions.csv`, `summary.json`
  (ключи: `status,stopped_working[],new_labels[],disappeared_labels[],
  stopped_working_count,run_id,prev_run_id,error_threshold,test_passed`).
  «Перестал отрабатывать» = error% пересёк `--error_threshold` (по умолч. 100).
- `summary_to_confluence.py` — строит storage-XML и обновляет страницу.
  Аргументы: `--summary --delta --application --build_url --system_metrics_dir
  --title --url --space -u/-p -pg --dry_run --output_dir`. Импортирует
  `confluence_manger_v2` (лежит в `resources/`; путь добавляется в sys.path).
  `--system_metrics_dir` → грузит PNG (`FileData`/`add_attachments`) и встраивает
  макросы `<ac:image><ri:attachment .../></ac:image>` в expand-блок.
  `--dry_run` пишет `output/summary_confluence.xhtml` (для локальной проверки).
- `case_parser.py` — парсер Java Case-классов Gatling. Регуляркой извлекает пары
  `{имя_переменной: имя_запроса_в_логе}` из объявлений `VAR = http("name")`
  (комментарии вырезаются). Используется и генератором весов, и парсером лога.
  Функции: `parse_case_classes(paths, base_dirs)`, `log_to_var`, `resolve_paths`.
- `profile_to_props.py` — `profile.yaml` → `profile.properties`.
  Масштабирует `inject.<scn>.users` на `k=target_percent/100` (min 1, если >0).
  **Веса считаются автоматически**: для сценария берёт его `request_classes`
  (→ var↔log), для каждого члена берёт `count[log]` и пишет
  `weight.<scn>.<var> = count_доля_в_процентах` (сумма ≤ 100, поправка на округление).
  Fallback: нет `request_classes` → legacy ручные `weights`; класс не найден →
  WARN + дефолты из кода. Контракт property (совпадает с ProfileConfig):
  `target_percent`, `injection.duration`, `injection.rampup`,
  `inject.<scn>.users`, `weight.<scn>.<var>` (ключ = имя переменной Case).
- `render_export.py` — рендер панелей Grafana 11.6.2 в PNG (см. §6).

### Java (`gatling/src/test/java/`)
- `config/ProfileConfig.java` — читает `profile.properties` (через
  `-DprofileProperties=...`, иначе cwd, иначе classpath; нет файла → дефолты).
  Методы: `getWeight(scn,choice,def)` → **double** `weight.scn.choice` (проценты);
  `getInjectUsers(scn,def)` → `inject.scn.users`; `getDuration/getRampup/
  getTargetPercent`. **Имена property — единственный контракт с profile_to_props.**
- `scenarios/pprbSberrating/LicensesScenario.java` — **шаблон** рефакторинга:
  хардкод весов `Choice.WithWeight(...)` заменён на `ProfileConfig.getWeight(SCN,
  "<имя_переменной_Case>", default)`. Ключ choice = имя переменной Case-класса
  (напр. `UC01_POST_Licenses_Summary`) — то же, что связано с лог-именем в Case и
  что генерит `profile_to_props`. Дефолты = прежние значения (поведение без
  profile.properties не меняется). `SCN="Licenses"` ↔ `injection.scenarios.Licenses`.

### Конфиги (`profiles/`)
- `profile.example.yaml` — профиль НТ + SLA. `count` (абс. цель на 100%),
  глобальные `95pct/50pct/rps/error_count`, `sla_per_label`, `request_classes`
  (список путей к Java Case-классам), секция `injection` (`duration`, `rampup`,
  `scenarios.<scn>.users`, `scenarios.<scn>.request_classes`). **`weights` в
  injection больше нет** — считаются из `count`. Ключи `count`/`sla_per_label`
  можно писать по имени переменной Case (резолвится в `name` из simulation.log)
  или прямо по лог-имени.
- `grafana.example.yaml` — конфиг render_export: `grafana{url,tz,scale,width,
  height,use_proxy}`, `dashboards{default,dropapp}`, `applications{ключ→[datasource]}`.

`.gitignore` исключает: `.venv/ output/ profile.properties run/ history_repo/ *.tar.gz`.
Реальные `profiles/profile.yaml` и `profiles/grafana.yaml` **коммитятся** (джобы
читают их из checkout). Секретов в них нет (токены — через Jenkins credentials).

## 5. Что чинил в render_export (Grafana 11.6.2 + image-renderer 4.1.2)

Исходник `resources/render_export.py` ломался на новой Grafana. Исправлено в
`ltAuto/render_export.py`:
1. `/render/d-solo/<uid>/<slug>` — слаг теперь подставляется (раньше доставался, но
   в URL не шёл; в Grafana 11 без слага рендер не отдаёт картинку).
2. Запросы к datasource через прокси Grafana `/api/datasources/proxy/uid/<uid>/...`
   с Bearer (у proxy-datasource поле `url` часто пустое/внутреннее). Фолбэк —
   прямой `url` при `--use_proxy 0`.
3. Токен/URL — из CLI/env (`GRAFANA_TOKEN`/`GRAFANA_URL`)/YAML, без хардкода секрета.
4. `verify=False` + отключение warnings (самоподписанные сертификаты), таймауты.
5. `tz`/`scale`/`width`/`height` в URL.
6. Устойчивость: ошибка отдельной панели не валит экспорт; стадия падает только при
   полном провале (0 успешных, есть ошибки). Проверка `Content-Type` (битый ответ
   не сохраняется как PNG).
7. Параметризация: `applications`, `dashboards`, `grafana.*` вынесены в YAML
   (`--config`/env `GRAFANA_CONFIG`). Приоритет: **CLI > env > YAML > встроенный дефолт**.

## 6. Локальная проверка (без сети контура)

```bash
python3 -m venv .venv && . .venv/bin/activate
pip install pandas pyyaml requests
# парс реального лога
python3 ltAuto/gatling_parser.py --simulation_log resources/simulation.log \
    --profile profiles/profile.example.yaml --output_dir output --script_name efsSberratingWeb
# сравнение (первый прогон — без --previous)
python3 ltAuto/compare_runs.py --current output/rps_response_table.csv \
    --error_threshold 100 --test_result output/test_result.csv \
    --current_run_id r1 --output_dir output
# саммари в XML без Confluence
python3 ltAuto/summary_to_confluence.py --summary output/summary.json \
    --delta output/delta_table.csv --application efsSberratingWeb --dry_run
# профиль -> properties
python3 ltAuto/profile_to_props.py --profile profiles/profile.example.yaml \
    --target_percent 50 --output profile.properties
# загрузка grafana-конфига (без сетевых вызовов выполняется load_config)
```
render_export целиком локально не проверить — нужны живые Grafana/Prometheus.

## 7. Открытые места / что доделать или проверить после отладки

1. **Интеграция инъекции в Simulation-класс.** В сценариях заменены только веса
   (getWeight, ключ = имя переменной Case). `getInjectUsers/getDuration/getRampup`
   ещё НЕ подключены в `injectOpen(...)` главного класса симуляции (аналог
   `resources/OTT_all_debug.java`) — исходник не предоставлен. Нужно: в Simulation
   заменить хардкод `injectOpen` на значения из `ProfileConfig`. 17+ сценариев из
   OTT_all_debug ещё не отрефакторены по весам — только `LicensesScenario` как шаблон.
   Для каждого сценария в `injection.scenarios.<scn>` нужно указать `request_classes`
   (Case-класс(ы) его запросов) — из них берутся члены и доли count для весов.
2. **Раскладка проекта vs wrapper.** Start-джоба синкает `gatling/gatlingScripts/`
   и `gatling/libs/` и запускает `mvn gatling:test` в `gatlingScripts`. Созданные
   Java-файлы лежат в maven-структуре `gatling/src/test/java/...`. Это шаблоны —
   их нужно положить в реальный исходный проект; проверить, что `-DprofileProperties`
   доходит до прогона и `ProfileConfig` грузит файл (в логе строка
   `[ProfileConfig] Loaded profile from ...`).
3. **Имена в profile.yaml.** Единый мостик имён — Java Case-класс(ы) в
   `request_classes`: переменная (choice-ключ getWeight) ↔ `http("...")` (лог-имя).
   `count`/`sla_per_label` пишутся по переменной (резолвятся в лог-имя) или прямо
   по лог-имени. Имя сценария `injection.scenarios.<scn>` ↔ `SCN` в Java. Рассинхрон
   этих связей → дефолтные веса / пропуски проверок попадания.
4. **Remote-пути и пользователь** (`/home/pprb_test/...`, label агента
   `sberlinux&&Linux_Default`, `apache-maven-3.9.6`) захардкожены под текущий контур.
5. **Confluence page id / space / URL** и **Bitbucket repo** — задаются параметрами
   джобы, по умолчанию пустые.
6. **Grafana**: `panel_ids` берутся все из дашборда; пороги SLA CPU/Mem (0.40/0.80)
   и список панелей пока в коде PromQL — кандидаты на вынос в YAML (не сделано).
7. **Идемпотентность анализа**: повторная обработка одного `runId` блокируется через
   `last_processed.txt` в истории. При ручных перезапусках это учитывать.

## 8. Быстрый чек-лист «где искать баг»

- Картинки Grafana пустые/ошибка → `render_export.save_grafana_panel_as_image`
  (слаг, `Content-Type`), либо токен/датасорсы (`get_datasource_map`, прокси).
- Пустые списки подов → `get_datasource_pods` (прокси vs прямой url, окно времени).
- Неверные веса/интенсивность → цепочка `count` + `request_classes` →
  `case_parser` (var↔log) → `profile_to_props.py` (доли count, имена property) →
  `ProfileConfig` (ключи) → сценарий (аргументы getWeight = имена переменных Case).
  Частые причины: путь к Case-классу не найден (WARN, веса=дефолты); лог-имя из
  Case отсутствует в `count` (вес 0); ключ choice в getWeight ≠ имя переменной Case.
- «Всё перестало отрабатывать»/неверная дельта → `compare_runs` (порог, матчинг
  label, наличие предыдущего `rps_response_table.csv` в Bitbucket latest).
- Confluence без картинок → порядок: вложения грузятся ДО `update_page`; имена
  вложений во вложениях == `ri:filename` в макросе (см. `collect_images`).
- Анализ-джоба «ничего не делает» → status != SUCCESS/FAILED (тест ещё идёт) или
  `runId` уже в `last_processed.txt`.
