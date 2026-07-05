# Задача: внедрить и проверить пайплайн ночного НТ

## Цель
Развернуть непрерывный цикл нагрузочного тестирования: ночной запуск Gatling →
автоматический анализ → краткое саммари в Confluence + письмо → сравнение с прошлым
прогоном (дельта, «отвалившиеся» запросы) → история в Bitbucket. Плюс рендер
системных метрик из Grafana 11.6.2.

Эту задачу нужно сначала **проверить на одном сервисе и одной симуляции** (smoke),
убедиться, что всё работает, и только потом включать по расписанию.

---

## Что входит в поставку (уже готово в этом репозитории)

Пути ниже даны для боевой раскладки: ядро — в `gatling/gatlingScripts/`,
Jenkins-файлы — в `gatlingJenkins/` (в этом scaffold-репозитории они лежат в корне).

| Файл (боевой путь) | Назначение |
|------|------------|
| `gatlingJenkins/Jenkinsfile_NT_Start` | Старт-джоба: запускает тест в фоне (nohup) на генераторе |
| `gatlingJenkins/Jenkinsfile_NT_Analyze_Report` | Анализ-джоба: подхватывает результат, считает, репортит |
| `gatling/gatlingScripts/ltAuto/gatling_parser.py` | Парсит `simulation.log` → CSV-таблицы + окно прогона |
| `gatling/gatlingScripts/ltAuto/compare_runs.py` | Сравнение с прошлым прогоном, поиск «отвалившихся» |
| `gatling/gatlingScripts/ltAuto/summary_to_confluence.py` | Публикация саммари + картинок в Confluence |
| `gatling/gatlingScripts/ltAuto/profile_to_props.py` | `profile.yaml` → `profile.properties`; авторасчёт весов из count |
| `gatling/gatlingScripts/ltAuto/case_parser.py` | Разбор Java Case-классов: переменная ↔ имя запроса в логе |
| `gatling/gatlingScripts/ltAuto/render_export.py` | Рендер панелей Grafana 11.6.2 в PNG |
| `gatling/gatlingScripts/src/test/java/config/ProfileConfig.java` | Чтение профиля в Gatling |
| `gatling/gatlingScripts/src/test/java/scenarios/pprbSberrating/LicensesScenario.java` | Шаблон сценария с весами из профиля |
| `gatling/gatlingScripts/profiles/profile.example.yaml` | Пример профиля НТ и SLA |
| `gatling/gatlingScripts/profiles/grafana.example.yaml` | Пример конфига Grafana |

---

## Часть A. Что куда скопировать

### A1. Скрипты пайплайна
1. Папки `ltAuto/` и `profiles/` должны лежать в `gatling/gatlingScripts/`
   (там же, где `pom.xml` и `src/`) — этот каталог Jenkins чекаутит как scm и джобы
   обращаются к нему через `env.GATLING_DIR = "gatling/gatlingScripts"`.
2. Jenkins-файлы (`Jenkinsfile_NT_Start`, `Jenkinsfile_NT_Analyze_Report`) положи в
   каталог `gatlingJenkins/` рядом с остальными пайплайнами.
3. `confluence_manger_v2.py` держи **в `ltAuto/`** (в боевом репо он уже там); скрипт
   `summary_to_confluence.py` ищет его рядом (в `ltAuto/`), затем в `resources/` и корне.

### A2. Java в проект Gatling
4. `ProfileConfig.java` — в пакет `config`
   (путь `gatling/gatlingScripts/src/test/java/config/ProfileConfig.java`).
5. `LicensesScenario.java` (пример из `scenarios/pprbSberrating/`) — это **шаблон**.
   В своих реальных сценариях замени
   хардкод весов в `randomSwitch` на вызовы `ProfileConfig`. Ключ choice —
   **имя переменной из Case-класса** (то, что стоит слева от `= http("...")`):

   Было:
   ```java
   new Choice.WithWeight(25, exec(LicensesCase.UC01_POST_Licenses_Summary))
   ```
   Стало:
   ```java
   new Choice.WithWeight(
       ProfileConfig.getWeight("Licenses", "UC01_POST_Licenses_Summary", 25),
       exec(LicensesCase.UC01_POST_Licenses_Summary))
   ```
   где `"Licenses"` — имя сценария (совпадает с ключом в `profile.yaml`),
   `"UC01_POST_Licenses_Summary"` — имя переменной Case (choice-ключ),
   `25` — дефолт на случай отсутствия profile.properties.

> Веса задавать вручную НЕ нужно — они считаются автоматически из долей `count`
> (см. A3). Здесь ты только «прокидываешь» вес из профиля в сценарий.
> На время smoke достаточно отрефакторить **один** сценарий (например Licenses).

### A3. Заполни конфиги (скопируй example → рабочий файл)
6. `profiles/profile.example.yaml` → `profiles/profile.yaml`. Заполни:
   - `request_classes:` — пути к твоим Java Case-классам (где запросы объявлены
     через `http("...")`, как в `cases/pprbSberrating/LicensesCase.java`). Пути
     относительны `gatlingScripts`, можно указать **каталог** — тогда возьмутся все
     `.java` внутри (напр. `src/test/java/cases/efsFinmonWeb`), конкретный файл или
     glob. Это единый источник связи «имя переменной ↔ имя запроса в логе».
   - `count:` — ожидаемое число запросов на 100% профиля. **Ключи можно писать по
     имени переменной Case** (напр. `UC01_POST_Licenses_Summary`) — они сами
     резолвятся в лог-имя; либо прямо по лог-имени из `simulation.log`.
   - SLA: `95pct`, `50pct`, `rps`, `error_count` (+ при желании `sla_per_label`,
     ключи так же — по переменной или лог-имени).
   - `injection.scenarios.<имя>` — `users` (интенсивность на 100%) и
     `request_classes` (какие запросы входят в сценарий). **`weights` НЕ задаём** —
     вес каждого запроса считается автоматически как доля его `count` среди
     запросов сценария. Имя `<имя>` сценария должно совпасть с `SCN` в коде
     сценария (первый аргумент `getWeight`).
7. `profiles/grafana.example.yaml` → `profiles/grafana.yaml` (если нужен рендер
   Grafana). Заполни `grafana.url`, проверь `dashboards` (uid) и `applications`
   (ключ АС → список datasource в Grafana).

> Как считаются веса: `weight[запрос] = count[запрос] / Σcount(запросов сценария) × 100%`.
> Пример: count 9000/200/100/100 → веса ≈ 95.74 / 2.13 / 1.06 / 1.06 (сумма 100).

> `profile.properties` создаётся автоматически и в гит не коммитится. Рабочие
> `profile.yaml` и `grafana.yaml` — коммить (джобы читают их из репозитория).

---

## Часть B. Что настроить в Jenkins

Создай две Pipeline-джобы из `Jenkinsfile_NT_Start` и `Jenkinsfile_NT_Analyze_Report`.

### B1. Credentials (создать заранее в Jenkins)
- `CREDS` — доступ к генератору (Username/Password) — для обеих джоб.
- `BITBUCKET_CREDS` — доступ к репозиторию истории (Username/Password).
- `CONF_CREDS` — учётка Confluence (Username/Password).
- `GRAFANA_TOKEN_CRED` — Service account token Grafana (тип **Secret text**) — только
  если включаешь рендер метрик.

### B2. Параметры старт-джобы (`Jenkinsfile_NT_Start`)
- `packageSimulation` — класс симуляции (по умолчанию `pprbSberrating.All.OTT_all_debug`).
- `ACTION` — `ЗАПУСТИТЬ ТЕСТ` (синк + прогон) или `ТОЛЬКО ОБНОВИТЬ СКРИПТЫ` (только rsync).
- `TARGET_PERCENT` — для smoke поставь небольшой, например `10`.
- `PROFILE_YAML` — `profiles/profile.yaml` (путь относительно `gatling/gatlingScripts`).
- `START_TIME` — `now`.
- `UNATTENDED` — для ручного smoke оставь `false` (будет интерактив при занятом генераторе).
- `CREDS` — выбери креды генератора.
- При необходимости поправь remote-пути в блоке `environment{}` и label агента
  `sberlinux&&Linux_Default`, версию maven `apache-maven-3.9.6` под свой контур.

### B3. Параметры анализ-джобы (`Jenkinsfile_NT_Analyze_Report`)
- `APPLICATION` — имя АС для истории/отчёта (например `efsSberratingWeb`).
- `PROFILE_YAML` — `profiles/profile.yaml`.
- `ERROR_THRESHOLD` — порог error% для «перестал отрабатывать» (по умолч. `100`).
- `CREDS`, `BITBUCKET_REPO`, `BITBUCKET_CREDS`.
- `CONF_URL`, `CONF_SPACE`, `CONF_PAGE_SUMMARY`, `CONF_CREDS`.
- Grafana (опционально): `ENABLE_GRAFANA=true`, `GRAFANA_URL` (или оставь пустым,
  если задан в `grafana.yaml`), `GRAFANA_CONFIG=profiles/grafana.yaml`,
  `GRAFANA_APPLICATION` (ключ АС из `grafana.yaml`), `GRAFANA_TOKEN_CRED`.
- `MAIL_TO` — получатели письма.

> На время smoke отключи cron-триггеры (запускай руками), чтобы не ловить ночной запуск.

---

## Часть C. Smoke-проверка (один сервис, одна симуляция)

Цель — пройти весь путь руками и убедиться, что каждый шаг отрабатывает.

### Шаг 0. Локальная проверка скриптов (на своей машине)
> Команды ниже — из каталога `gatling/gatlingScripts` (тогда `ltAuto/`, `profiles/`
> и `src/test/java/cases/...` из `request_classes` резолвятся). В этом scaffold —
> из корня, где `simulation.log` лежит в `resources/`.

```bash
python3 -m venv .venv && . .venv/bin/activate
pip install pandas pyyaml requests
python3 ltAuto/gatling_parser.py --simulation_log resources/simulation.log \
    --profile profiles/profile.yaml --output_dir output --script_name smoke
python3 ltAuto/compare_runs.py --current output/rps_response_table.csv \
    --error_threshold 100 --test_result output/test_result.csv \
    --current_run_id r1 --output_dir output
python3 ltAuto/summary_to_confluence.py --summary output/summary.json \
    --delta output/delta_table.csv --application smoke --dry_run
```
Ожидаемо: появились `output/*.csv`, `output/window.json`, `output/summary.json`,
`output/summary_confluence.xhtml`. Открой xhtml — должна быть таблица и статус.

### Шаг 1. Старт-джоба
1. Запусти `Jenkinsfile_NT_Start` с `TARGET_PERCENT=10`, `START_TIME=now`.
2. В логе должно быть: `cat profile.properties` с твоими весами; строка
   «Тест запущен в фоне (nohup)».
3. На генераторе проверь, что появились `gatling_status.txt` (`STARTED ...`),
   `lockfileGatling.txt=1`, процесс mvn идёт.

### Шаг 2. Дождись завершения теста
Когда тест закончится, на генераторе:
- `gatling_status.txt` = `SUCCESS ...` (или `FAILED ...`),
- появился `gatling_report.tar.gz`, `lockfileGatling.txt=0`.

### Шаг 3. Анализ-джоба
1. Запусти `Jenkinsfile_NT_Analyze_Report` (с теми же кредами/АС).
2. Проверь по стадиям:
   - `Fetch completed run` — status распознан, tarball скачан, найден `simulation.log`.
   - `Parse Gatling` — создались CSV и `window.json`.
   - `Grafana system metrics` (если включал) — в логе «Рендер завершён: успешно N…»,
     в `output/systemMetrics/**` лежат PNG.
   - `Compare runs` — первый прогон без предыдущего отработал (дельта = baseline).
   - `Persist history` — коммит в Bitbucket прошёл (в репо появилась папка прогона
     и `latest/`, обновился `last_processed.txt`).
   - `Confluence summary` — страница обновилась.
   - `Notify (mail)` — письмо ушло на `MAIL_TO`.

### Шаг 4. Проверка отчётности
- Открой страницу Confluence: статус прогона, метаданные, блок «Дельта по
  измерениям», (если включал Grafana) блок «Системные метрики».
- Проверь письмо: тема `[НТ][<АС>] <статус> (отвалилось: N)` и ссылка на Confluence.

### Шаг 5. Проверка сравнения (дельта между прогонами)
1. Прогони Старт + Анализ **второй раз**.
2. На второй итерации `Compare runs` должен показать ненулевую дельту, а в саммари —
   корректные «новые/исчезнувшие/отвалившиеся» запросы (если такие появятся).
3. Чтобы проверить детект «перестал отрабатывать», можно временно понизить
   `ERROR_THRESHOLD` или сэмулировать ошибки на сервисе.

---

## Часть D. Критерии приёмки (Definition of Done)
- [ ] Старт-джоба запускает тест в фоне, не держит агент.
- [ ] Анализ-джоба сама подхватывает завершённый прогон и не падает, если тест ещё идёт.
- [ ] Веса/интенсивность берутся из `profile.yaml` (в логе `[ProfileConfig] Loaded ...`).
- [ ] CSV и `summary.json` формируются, история коммитится в Bitbucket.
- [ ] Саммари публикуется в Confluence; письмо приходит со статусом и ссылкой.
- [ ] (Если включён Grafana) PNG системных метрик рендерятся и прикладываются к странице.
- [ ] Повторный прогон показывает корректную дельту и пометку «отвалившихся» запросов.

---

## Если что-то не работает — куда смотреть
- **Картинки Grafana пустые/ошибка** → проверь `GRAFANA_TOKEN` (Service account,
  роль ≥ Viewer), `grafana.url`, что в `grafana.yaml` правильные `dashboards`/
  `applications`. При проблемах с datasource попробуй `--use_proxy 0`.
- **Веса не применились** → сверь: (1) путь в `request_classes` существует (иначе в
  логе `profile_to_props` будет WARN и веса = дефолты из кода); (2) имя сценария
  `injection.scenarios.<имя>` == первый аргумент `getWeight`; (3) имя переменной
  Case == второй аргумент `getWeight`; (4) лог-имя из Case есть в `count` (иначе
  вес 0). В логе Gatling ищи `[ProfileConfig] Loaded profile from ...`.
- **Анализ-джоба «NOT_BUILT»** → тест ещё не завершён (status не SUCCESS/FAILED),
  либо прогон уже обработан (`last_processed.txt`).
- **Нет дельты** → в Bitbucket нет `history/<АС>/latest/rps_response_table.csv`
  (это первый прогон) — норм; на втором прогоне появится.
- **Confluence без картинок** → проверь, что `ENABLE_GRAFANA=true` и каталог
  `output/systemMetrics` не пуст; имена вложений совпадают с макросами.

Подробная техкарта по внутренностям — в `docs/NT_PIPELINE_AI_CONTEXT.md`.
