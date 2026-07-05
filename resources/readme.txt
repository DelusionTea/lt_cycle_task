# Структура проекта: PPRB Compl — Gatling & ltAuto

## Общее описание

Проект — **комплекс нагрузочного тестирования** для **PPRB** (Платформа Правового Риск-Бизнеса / Compliance platform). Состоит из:

- **Gatling 3.9.5** (Java 11/15) — ядро сценариев HTTP-нагрузки
- **ltAuto** — Python (3.x) утилиты автоматизации прогонов, парсинга логов, отчётов
- **Jenkins** — CI/CD пайплайны для удалённого запуска
- **Python-загрузчики** данных в БД (для контрагентов, рейтингов, кредитных историй, инспекций...)

---

## 1. Ядро: Gatling Scripts

### Структура каталогов

```text
gatling/gatlingScripts/
├── pom.xml                      # Maven POM: Java 11/15, Gatling 3.9.5
├── ltAuto/                      # ★ Python-автоматизация (7 скриптов)
│   ├── case_parser.py           # Парсинг Java Case → {var: log_name}
│   ├── compare_runs.py          # Сравнение прогонов (delta, regression)
│   ├── confluence_manger_v2.py # REST Confluence API клиент
│   ├── gatling_parser.py        # Парсер simulation.log → CSV метрики
│   ├── profile_to_props.py      # profile.yaml → profile.properties
│   ├── render_export.py         # Экспорт Grafana 11.6.2 → PNG
│   └── summary_to_confluence.py # XML → Confluence (summary page)
├── profiles/                    # YAML конфигурации SLA + инъекции
│   ├── profile.yaml             # Основной профиль efsFinmonWeb
│   ├── profile.example.yaml     # Шаблон
│   └── grafana.example.yaml     # Пример Grafana
├── resources/                   # (пусто) — под ресурсы/библиотеки
└── src/
    └── test/
        ├── java/
        │   ├── cases/           # ~100+ Java Case-классов (Gatling HTTP builders)
        │   │   ├── efsFinmonWeb/
        │   │   ├── efsFinmonMob/
        │   │   ├── efsSberbusinessAuth/
        │   │   ├── pprbComplianceRequests/  # ~60 штук
        │   │   ├── pprbSberrating/         # ~25 штук
        │   │   └── ZK/                     # ~90 штук
        │   ├── config/          # ProfileConfig.java — читатель .properties
        │   ├── feeders/         # ~50 feeder-классов (данные, заголовки)
        │   ├── scenarios/       # ~20 Gatling-сценариев
        │   └── simulations/    # ~40 + Sim классов (LMAX, STAB, STUB, FMAX)
        └── resources/           # lib/*.jar (Kafka, Akka, PostgreSQL, gatling-kafka)
```

### Версии (pom.xml)

| Компонент            | Версия    |
|----------------------|-----------|
| **Gatling (highcharts, jdbc)** | 3.9.5 |
| **Gatling Maven Plugin**      | 4.3.7 |
| **Java**               | 11 (source/target: 15) |
| **PostgreSQL JDBC**    | 42.6.0 |
| **Kafka Clients**      | 3.6.1 |
| **Akka**              | 2.6.21 |
| **Gatling Kafka Plugin** | 3.5 |
| **Apache Commons Lang3** | 3.10 |

---

## 2. ltAuto — Скрипты автоматизации

### Зачем нужны

Связывают **Gatling → YAML профили → CSV метрики → Confluence/Grafana** в единый цикл НТ (ночное тестирование).

### Поток работы

```
profile.yaml  (цели, SLA, сценарии)
     ↓
profile_to_props.py  — генерирует profile.properties
     ↓                        (веса randomSwitch вычисляются автоматически)
Gatling (mvn gatling:test)
     ↓
simulation.log  (сырой TSV)
     ↓
gatling_parser.py  — → rps_response_table.csv, rps_table.csv, response_table.csv
                       → checks_results.csv, test_result.csv, window.json
     ↓
compare_runs.py  — → delta_table.csv, regressions.csv, summary.json
     ↓
summary_to_confluence.py  → → Confluence page (Summary)
     ↑
render_export.py  — PNG из Grafana → прикрепляются к странице Confluence
```

### Ключевые скрипты

| Скрипт | Вход | Выход | Назначение |
|--------|------|-------|-----------|
| **`profile_to_props.py`** | `profile.yaml`, `*.java` | `profile.properties` | Автоматический расчёт весов `randomSwitch` |
| **`case_parser.py`** | `*.java` | `{var: log_name}` | Парсинг Java Case → имя запроса в `simulation.log` |
| **`gatling_parser.py`** | `simulation.log` + `profile.yaml` | `*_table.csv`, `test_result.csv` | Метрики по label: count, error%, rps, pct50/95 |
| **`compare_runs.py`** | `rps_response_table.csv` (×2) | `delta_table.csv`, `regressions.csv` | Дельта между прогонами |
| **`summary_to_confluence.py`** | `summary.json`, `delta_table.csv` | Confluence storage XML | Публикация в Confluence |
| **`confluence_manger_v2.py`** | — | — | REST-клиент Confluence (загрузка/обновление) |
| **`render_export.py`** | Grafana API | PNG файлы | Экспорт панелей Grafana в картинки |

### Формат profile.yaml (ключевые секции)

```yaml
count:                       # Цели на 100% профиля (абсолютные)
  UC03_GET_/params: 1152
  UC10_POST_/mop-1-0: 9144
  ...
sla_per_label: {}           # Точечные SLA (переопределение)
injection:                   # Параметры инъекции
  duration: 600
  rampup: 60
  scenarios:
    efsFinmonWeb:
      users: 50             # интенсивность
      request_classes:       # ссылки на Java Case-классы
        - resources/Case.java
```

---

## 3. Jenkins CI/CD

### Файлы

```text
gatlingJenkins/
├── Jenkinsfile                  # Основной пайплайн (3 stages, SSH, nohup)
├── Jenkinsfile_1               # Вариант
├── Jenkinsfile_d               # Debug-вариант
├── JenkinsfileStopTest          # Остановка теста
├── testOneJenk                  # Одноразовый запуск
├── cleanup_java_dirs.sh        # Очистка Java-директорий на агенте
└── readme.txt
```

### Пайплайн (Jenkinsfile)

1. **checkout** — исходники из репозитория
2. **Интерактивный выбор** — `input` с `SimulationClass` (выбор из `simulations/`)
3. **SSH-деплой** — `rsync` на удалённый Linux-агент
4. **Запуск** — `mvn gatling:test -B -q` (logback ERROR → тишина)
5. **Сбор метрик** — `ltAuto/*.py` на агенте
6. **Публикация** — Confluence + Grafana

### Управление

- `TARGET_PERCENT` — масштабирование целей (10%..100%)
- `ACTION` — `ЗАПУСТИТЬ ТЕСТ` / `ТОЛЬКО ОБНОВИТЬ СКРИПТЫ`
- `lockfileGatling.txt` — защита от одновременных прогонов

---

## 4. Загрузчики данных (Python)

### Модули

| Модуль | Каталог | Назначение |
|--------|---------|-----------|
| **Arbitrage** | `Loaders/arbitrage-loader/` | 3 скрипта: детальный + метрики + тело |
| **Compliance Risk** | `Loaders/compliance-risk-loader/` | 3+ скрипта: инкремент, вставка, full |
| **Counteragent** | `Loaders/counteragent-loader/` | 5 скриптов: организации, транзакции |
| **Credit History** | `Loaders/credithistory-loader/` | 4 скрипта: контракты + метрики |
| **Finance** | `Loaders/finance-loader/` | 5 скриптов: common, factor, метрики, yearly |
| **Inspections** | `Loaders/inspections-loader/` | 2 скрипта: детальный + метрика |
| **Licenses** | `Loaders/licenses-loader/` | 3 скрипта: категория, детальный, метрика |
| **Regdata** | `Loaders/regdata-loader/` | 2 скрипта: economic + registration |
| **Regdata Status** | `Loaders/regdata-status-loader/` | 1 скрипт: детальный |
| **Sberrating** | `Loaders/sberrating-loader/` | 1 скрипт |

Каждый модуль имеет:
- `*_loader.py` — Python-скрипт с логикой вставки
- `*_body.txt` — тело запроса (шаблон/строки)
- `.tkt` / `.csv` — файлы с данными (архив, метрики)

---

## 5. GenApp / Kafka / XML

### Вспомогательные модули

| Компонент | Технологии | Версии |
|-----------|-----------|--------|
| **generator_app** | Spring Boot 2.7.5, Java 11, H2, JSch | 2.7.5 |
| **send_XML_to_kafka** | Kafka Clients 3.0.0, SLF4J 1.7.32 | 3.0.0 |
| **KTSMagic** | Java (raw Main.class) + YAML | — |

---

## 6. Глоссарий

| Термин | Значение |
|--------|---------|
| **PPRB** | Платформа Правового Риск-Бизнеса (compliance) |
| **НТ** | Нагрузочное Тестирование |
| **LMAX** | Load Maximum (пиковая нагрузка) |
| **STAB** | Стабильность (длительная) |
| **STUB** | Заглушка (мок) |
| **FMAX** | Full Maximum (полный максимум) |
| **UC** | Use Case (вариант использования — номер из требований) |
| **SLA** | Service Level Agreement (время отклика, rps, errors) |
| **simulation.log** | Файл лога Gatling (TSV: RUN, USER, REQUEST, GROUP) |