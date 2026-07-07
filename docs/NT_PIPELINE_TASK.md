# Задача: внедрить и проверить пайплайн ночного НТ

**Для кого:** инженер, который разворачивает автоматизацию впервые.  
**Цель:** за минимальное время пройти smoke (одна АС + одна симуляция), затем включить ночной цикл.

> Техкарта для ИИ: `docs/NT_PIPELINE_AI_CONTEXT.md`  
> Скиллы GigaCode: `gigacode/skills/` (см. `gigacode/README.md`)  
> Инструкция агенту: `docs/ai/AI_INSTRUCTION_GIGACODE.md`

---

## Быстрый маршрут (5 шагов)

| # | Вы | Агент (GigaCode) | Gate |
|---|-----|------------------|------|
| 1 | Скопировать файлы по карте ниже (§1) | — | дерево каталогов на месте |
| 2 | `@onboard-new-as-domain` + APPLICATION | один SCN → codemod весов | `verify_profile.py` PASS |
| 3 | Заполнить `profiles/<АС>/profile.yaml` | `@align-count-from-simulation-log` или `@build-profile-from-simulation` | verify PASS |
| 4 | Локально: parser + compare + confluence dry-run | `@parse-gatling-run` | CSV + xhtml |
| 5 | Jenkins Start → Analyze (smoke) | `@jenkins-nt-smoke` | Confluence + mail |

**Не ждите**, пока все 17+ сценариев будут на ProfileConfig — для smoke достаточно **одного SCN** (например Licenses) и рабочего `profile.yaml`.

---

## 1. Куда что положить

### 1.1 Два репозитория / зоны

| Зона | Что | Пример пути |
|------|-----|-------------|
| **Ядро Gatling** | pom.xml, Java, ltAuto, profiles | `gatling/gatlingScripts/` |
| **Jenkins** | NT Start / Analyze | `gatlingJenkins/` (в scaffold — корень `Jenkinsfile_NT_*`) |
| **Скиллы агента** | GigaCode (не копировать на генератор) | `gigacode/skills/<имя>/SKILL.md` |
| **Checkstyle** | стиль Java (корень LT-репо) | `checkstyle/gatling-checkstyle.xml` |

Jenkins всегда: `env.GATLING_DIR = "gatling/gatlingScripts"`.

### 1.2 Дерево `gatling/gatlingScripts/` (боевое)

```text
gatling/gatlingScripts/
├── pom.xml
├── checkstyle/                    ← или ссылка на ../../checkstyle из корня LT
├── ltAuto/                        ← все Python-скрипты (§1.3)
├── profiles/
│   ├── README.md
│   ├── grafana.yaml               ← из grafana.example.yaml
│   ├── grafana.example.yaml
│   └── <APPLICATION>/             ← имя АС = cases/<APPLICATION>/
│       └── profile.yaml           ← НЕ profiles/profile.yaml в корне!
├── src/test/java/
│   ├── config/ProfileConfig.java
│   ├── cases/<APPLICATION>/       ← *Case.java
│   ├── scenarios/<пакет>/         ← *Scenario.java
│   ├── simulations/...          ← *Simulation.java
│   └── feeders/<APPLICATION>/
└── src/test/resources/
    ├── JSONs/<APPLICATION>/...
    └── feeders/<APPLICATION>/...
```

**Правило:** `APPLICATION` = имя каталога в `cases/` и `profiles/` (напр. `efsFinmonWeb`, `pprbSberrating`).

### 1.3 Python `ltAuto/` — что скопировать

| Скрипт | Назначение | Когда нужен |
|--------|------------|-------------|
| `profile_to_props.py` | yaml → properties, **веса из count** | Jenkins Start, всегда |
| `case_parser.py` | var ↔ log | зависимость |
| `profile_paths.py` | пути profiles/\<АС\>/ | зависимость |
| `gatling_parser.py` | simulation.log → CSV | Analyze |
| `compare_runs.py` | дельта прогонов | Analyze |
| `summary_to_confluence.py` | Confluence | Analyze |
| `confluence_manger_v2.py` | REST Confluence | рядом в ltAuto/ |
| `render_export.py` | Grafana → PNG | Analyze (опц.) |
| `verify_profile.py` | **gate PASS/FAIL** | после каждой правки |
| `weights_codemod.py` | WithWeight → ProfileConfig | рефакторинг |
| `inject_codemod.py` | injectOpen → ProfileConfig | позже smoke |
| `sim_to_profile.py` | RPS + веса → yaml (count в req/h) | сборка профиля |
| `log_labels_to_profile.py` | log → ключи count | заполнение yaml |
| `dump_scenario_meta.py` | метаданные перед правкой | extract |

### 1.4 Java — минимум для smoke

| Файл | Куда |
|------|------|
| `ProfileConfig.java` | `src/test/java/config/` |
| Шаблон сценария | `src/test/java/scenarios/.../LicensesScenario.java` (из репо) |
| Остальные Case/Scenario/Simulation | уже в вашем Gatling-проекте |

### 1.5 Jenkins

| Файл | Куда |
|------|------|
| `Jenkinsfile_NT_Start` | `gatlingJenkins/` |
| `Jenkinsfile_NT_Analyze_Report` | `gatlingJenkins/` |

### 1.6 Scaffold vs production

| | **Scaffold (этот репо LT)** | **Production** |
|---|------------------------------|------------------|
| Запуск Python | корень LT или `resources/` | `cd gatling/gatlingScripts` |
| profile | `profiles/efsFinmonWeb/profile.yaml` | `profiles/<APPLICATION>/profile.yaml` |
| simulation.log | `resources/simulation.log` | из tarball прогона |
| pom + checkstyle | `resources/pom.xml` | `gatling/gatlingScripts/pom.xml` |

---

## 2. Что попросить у агента (GigaCode)

Скиллы лежат в **`gigacode/skills/`**. Вызов: `@имя-скилла` или `skill: имя-скилла`.

### 2.1 Стартовый промпт (любая задача)

```text
Прочитай docs/ai/AI_INSTRUCTION_GIGACODE.md и @task-router-gigacode.
Режим: @one-file-at-a-time + @session-handoff-template.
APPLICATION: efsFinmonWeb
Задача: <одна фраза из маршрутизатора>.
Сделай ТОЛЬКО первую итерацию. Gate обязателен. Стоп до «далее».
```

### 2.2 Типовые задачи → скилл

| Задача человеку | Скилл | Gate |
|-----------------|-------|------|
| Подключить новую АС | `@onboard-new-as-domain` | verify PASS |
| Вынести веса одного сценария | `@gatling-weights-to-profileconfig` | `verify_profile.py --scenario ... --compile` |
| Собрать profile из RPS | `@build-profile-from-simulation` | verify + round-trip |
| Заполнить count по логу | `@align-count-from-simulation-log` | verify `--profile` |
| injectOpen (позже) | `@injectopen-from-profile` | `inject_codemod.py --limit 1` + compile |
| Ревью PR коллеги | `@gatling-reviewer` | `review_gates.py --strict` |
| Проверить Jenkins | `@jenkins-nt-smoke` | чек-лист параметров |
| После прогона локально | `@parse-gatling-run` | CSV + summary.json |
| Confluence dry-run | `@confluence-summary-publish` | xhtml не пуст |
| Grafana PNG | `@grafana-render-for-run` | PNG > 0 |

### 2.3 Пример: первый SCN за одну сессию

```text
@onboard-new-as-domain
APPLICATION: pprbSberrating
Первый сценарий: src/test/java/scenarios/pprbSberrating/LicensesScenario.java
1) dump_scenario_meta.py --dry-run
2) weights_codemod.py на этот файл
3) verify_profile.py --scenario ... --compile
Только LicensesScenario. Жду «далее».
```

### 2.4 Чего **не** просить агента

- «Отрефакторь все scenarios сразу» — только `@one-file-at-a-time`.
- «Посчитай веса/count вручную» — только скрипты.
- «Готово» без вывода `RESULT: PASS` или `mvn test-compile`.

---

## 3. Конфиги (profile + Grafana)

### 3.1 profile.yaml

Путь: **`profiles/<APPLICATION>/profile.yaml`**.

Jenkins: `APPLICATION=efsFinmonWeb`, `PROFILE_YAML` **пусто** → auto `profiles/efsFinmonWeb/profile.yaml`.

```yaml
description: "Профиль НТ efsFinmonWeb"
target_percent: 100
request_classes:
  - src/test/java/cases/efsFinmonWeb

count:                    # ключи = имя переменной Case (или log-имя)
  UC10_POST_Mop_1_0: 9144   # запросов/час на 100% профиля

95pct: 3000
50pct: 3500
rps: 8
error_count: 50

injection:
  duration: 3600
  rampup: 60
  scenarios:
    Licenses:             # = SCN в Java и getWeight(..., "UC..", ...)
      users: 5
      request_classes:
        - src/test/java/cases/pprbSberrating
```

- **`count` — запросов в час на 100% профиля** (`target_percent` масштабирует интенсивность при прогоне, не меняет соотношение весов). Значения считают из **весов randomSwitch + RPS throttle**:
  `count_i = round(RPS_total × weight_i / Σweight × 3600)` — см. `sim_to_profile.py`.
- **`weights` в yaml не задаём** — считает `profile_to_props.py` из **долей** `count` (абсолютные единицы не важны, важны пропорции).
- `injection.duration` — длительность hold из throttle (сек), **не** множитель для `count`.
- `profile.properties` **не коммитить** — генерируется на Start-джобе.

### 3.2 Grafana

`profiles/grafana.example.yaml` → `profiles/grafana.yaml` (url, dashboards uid, applications).

### 3.3 Контракт имён (критично)

| Уровень | Пример |
|---------|--------|
| Case variable | `UC01_POST_Licenses_Summary` |
| getWeight choice | то же имя |
| simulation.log | `UC01_POST_/licenses/summary` |
| injection.scenarios | `Licenses` (= SCN) |

Скилл: `@gatling-profile-conventions`.

---

## 4. Jenkins (кратко)

### 4.1 Credentials

`CREDS`, `BITBUCKET_CREDS`, `CONF_CREDS`, `GRAFANA_TOKEN_CRED` (если Grafana).

### 4.2 Start (`Jenkinsfile_NT_Start`)

| Параметр | Smoke |
|----------|-------|
| `APPLICATION` | `efsFinmonWeb` |
| `PROFILE_YAML` | пусто |
| `TARGET_PERCENT` | `10` |
| `packageSimulation` | ваш debug-класс |
| `ACTION` | `ЗАПУСТИТЬ ТЕСТ` |
| cron | **отключить** на время smoke |

В логе: `PROFILE_YAML=profiles/<APPLICATION>/profile.yaml`, `profile.properties`, «Тест запущен в фоне».

### 4.3 Analyze (`Jenkinsfile_NT_Analyze_Report`)

| Параметр | Значение |
|----------|----------|
| `APPLICATION` | тот же, что Start |
| `PROFILE_YAML` | пусто или явный путь |
| `ENABLE_GRAFANA` | false на первом smoke |
| `MAIL_TO`, Confluence | заполнить |

Скилл: `@jenkins-nt-smoke`.

---

## 5. Smoke-проверка (чек-лист)

### Шаг 0 — локально (15 мин)

```bash
cd gatling/gatlingScripts   # или корень LT для scaffold
python3 -m venv .venv && . .venv/bin/activate
pip install pandas pyyaml requests

# Gate профиля
python3 ltAuto/verify_profile.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --application efsFinmonWeb

# Парс (scaffold: resources/simulation.log)
python3 ltAuto/gatling_parser.py \
  --simulation_log resources/simulation.log \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --output_dir output --script_name smoke

python3 ltAuto/compare_runs.py \
  --current output/rps_response_table.csv \
  --output_dir output --current_run_id r1

python3 ltAuto/summary_to_confluence.py \
  --summary output/summary.json --delta output/delta_table.csv \
  --application smoke --dry_run
```

Ожидаемо: `output/*.csv`, `window.json`, `summary.json`, `summary_confluence.xhtml`.

Опционально:
```bash
mvn -q -DskipTests test-compile
mvn -q checkstyle:check          # стиль; failOnViolation=false пока legacy
```

### Шаг 1 — Start-джоба

Проверить на генераторе: `gatling_status.txt`, lockfile, процесс mvn.

### Шаг 2 — дождаться SUCCESS/FAILED + tarball

### Шаг 3 — Analyze-джоба

Стадии: Fetch → Parse → Compare → History → Confluence → Mail.

### Шаг 4 — второй прогон

Проверить дельту в `compare_runs` и Confluence.

---

## 6. Поэтапная автоматизация (не всё сразу)

Путь автоматизации **может быть непройден** — это нормально для первого smoke.

| Этап | Содержание | Скилл / скрипт | Обязательно для smoke |
|------|------------|----------------|------------------------|
| **0** | Файлы на месте, parser работает | §1, §5 шаг 0 | да |
| **1** | Один SCN: веса → ProfileConfig | weights_codemod + verify | да |
| **2** | profile.yaml для APPLICATION | onboard / align-count | да |
| **3** | Jenkins Start + Analyze | jenkins-nt-smoke | да |
| **4** | injectOpen из профиля | inject_codemod | нет |
| **5** | Все SCN домена | one-file-at-a-time × N | нет |
| **6** | Grafana PNG | render_export | нет |
| **7** | checkstyle failOnViolation=true | checkstyle/README | нет |

---

## 7. Definition of Done

- [ ] `profiles/<APPLICATION>/profile.yaml` в git, verify PASS.
- [ ] Хотя бы один Scenario с `ProfileConfig.getWeight` (или smoke на дефолтах + yaml готов).
- [ ] Start запускает nohup, в логе `[ProfileConfig] Loaded ...`.
- [ ] Analyze: CSV, summary.json, Confluence (dry-run локально OK).
- [ ] История в Bitbucket, второй прогон — дельта.
- [ ] (Опц.) Grafana PNG на странице.

---

## 8. Troubleshooting

| Симптом | Куда смотреть |
|---------|----------------|
| Веса = дефолты из кода | `request_classes`, SCN, ключи count; лог `profile_to_props` |
| verify FAIL | `python3 ltAuto/verify_profile.py ...` — текст FAIL |
| PROFILE_YAML не тот | `APPLICATION` + пустой PROFILE_YAML |
| Analyze NOT_BUILT | тест ещё идёт или run уже обработан |
| Grafana пусто | token, grafana.yaml, window.json |
| Агент «сделал всё» | нет PASS в выводе — `@session-handoff-template` |

Подробности: `docs/NT_PIPELINE_AI_CONTEXT.md`.

---

## 9. Карта документов

| Документ | Зачем |
|----------|-------|
| `docs/NT_PIPELINE_TASK.md` | **этот файл** — что куда, smoke, промпты |
| `docs/NT_PIPELINE_AI_CONTEXT.md` | контракты CSV, Jenkins, для глубокой отладки |
| `docs/ai/AI_INSTRUCTION_GIGACODE.md` | старт для GigaCode |
| `gigacode/README.md` | список скиллов |
| `profiles/README.md` | layout profiles/\<АС\>/ |
| `checkstyle/README.md` | Java-стиль |
| `resources/readme.txt` | обзор всего PPRB Compl |

*Обновлено: 2026-07-07*
