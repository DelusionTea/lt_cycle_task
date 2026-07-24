# PLAN: Автоматизация LT через OpenSpec (SDD-first)

## Цель

Сделать OpenSpec единым входом для описания нагрузки и максимально автоматизировать nightly-контур:

`OpenSpec -> SDD/spec.yaml -> validate -> profile.properties/profile.yaml -> Start/Analyze -> Confluence/Grafana`.

## Текущее состояние (уже выполнено)

### 1) OpenSpec bridge в коде
- Добавлен конвертер `SDD/ltAuto/openspec_to_sdd.py`:
  - поддержка YAML/JSON входа;
  - mapping полей в `SDD/spec.yaml`;
  - fallback для `count`, `thresholds`, `injection`, `meta.source`;
  - проверка `meta.service` через `--expected_service`.

### 2) Jenkins pre-step без ломки runtime
- `Jenkinsfile_NT_Start`:
  - добавлены параметры `USE_OPENSPEC`, `OPENSPEC_PATH`;
  - перед `spec_validate.py` добавлена конвертация OpenSpec -> SDD spec.
- `Jenkinsfile_NT_Analyze_Report`:
  - добавлены `USE_OPENSPEC`, `OPENSPEC_PATH`;
  - в `Prepare SDD profile` добавлена конвертация перед валидацией.
- `Jenkinsfile_Gatling_Foreground_Analyze_Mail`:
  - добавлены `USE_OPENSPEC`, `OPENSPEC_PATH`;
  - конвертация выполняется до `spec_validate.py`.

### 3) Документация и пресеты
- Контракт OpenSpec: `docs/OPENSPEC_DISCOVERY_CONTRACT.md`.
- Runbook запуска/rollback: `docs/OPENSPEC_ROLLOUT_RUNBOOK.md`.
- Presets Jenkins:
  - `docs/JENKINS_PRESET_OPENSPEC_PILOT.yaml`
  - `docs/JENKINS_PRESET_OPENSPEC_SAFE_SMOKE.yaml`

### 4) Технический smoke (локально)
- Пройдена цепочка:
  - `openspec_to_sdd.py -> spec_validate.py -> spec_to_props.py -> spec_to_profile.py`.
- Результат: `spec_validate` PASS, профили генерируются.

---

## План автоматизации (последовательность шагов)

## Этап A. Промышленный Pilot (1 компонент)
**Цель:** стабильно запустить `ZK/ComplianceRequests` через OpenSpec в прод-потоке.

1. Подготовить OpenSpec артефакт через GigaCLI:
   - `SDD/specs/ZK/ComplianceRequests/openspec.raw.yaml`.
2. Запустить `Start` по `SAFE_SMOKE` пресету (`TARGET_PERCENT=5`).
3. Запустить `Analyze` по `SAFE_SMOKE` пресету.
4. Повторить `Start + Analyze` по `PILOT` пресету (`TARGET_PERCENT=100`).
5. Зафиксировать baseline-результаты:
   - статус джоб;
   - наличие `summary.json`, `test_result.csv`, `delta_table.csv`;
   - публикация в Confluence.

**Критерий Done:**
- 3+ успешных цикла подряд без ручных правок конфигурации.

---

## Этап B. Автоматизация качества спеки (Quality Gates)
**Цель:** отлавливать ошибки контракта до удаленного запуска.

1. Расширить `SDD/ltAuto/spec_validate.py` проверками:
   - дубликаты `endpoints.id`;
   - дубликаты `endpoints.label`;
   - пустые `path`/`method`;
   - несоответствие `meta.service` и Jenkins `APPLICATION`.
2. Добавить локальный preflight-скрипт:
   - `ltAuto/openspec_preflight.py` (конвертация + validate + dry summary).
3. В Jenkins вынести preflight в отдельный stage перед sync/run.

**Критерий Done:**
- При некорректной спеке pipeline падает на preflight, не доходя до remote run.

---

## Этап C. Автоматизация профилирования нагрузки
**Цель:** уменьшить ручную работу по `count`/SLA.

1. Добавить утилиту автоподсказки `count` из истории прогонов:
   - источник: `history_repo/history/<APPLICATION>/latest/rps_response_table.csv`.
2. Добавить режимы профиля в OpenSpec:
   - `smoke`, `regular`, `peak`.
3. Поддержать выбор режима профиля параметром Jenkins (например, `PROFILE_MODE`).
4. Включить автоматический fallback:
   - если нет count -> `0`;
   - если есть история -> предзаполнение средними значениями.

**Критерий Done:**
- Для пилотной АС `count` и SLA обновляются без ручной правки YAML минимум в 80% запусков.

---

## Этап D. Автоматизация сценариев и simulation selection
**Цель:** безопасные и быстрые smoke-прогоны по компонентам.

1. Добавить генератор ограниченной simulation по компоненту:
   - на основе `spec.yaml` формировать набор сценариев только нужного компонента.
2. Добавить Jenkins-параметр `SIMULATION_SCOPE`:
   - `component_only` / `domain_all`.
3. Для smoke по умолчанию использовать `component_only`.

**Критерий Done:**
- Smoke-время сокращено, а воздействие на стенд ниже, чем при `ZK_all_debug`.

---

## Этап E. Масштабирование на домены
**Цель:** тиражировать процесс по другим АС/компонентам.

1. Ввести шаблон onboarding-компонента:
   - OpenSpec export;
   - convert;
   - validate;
   - safe smoke;
   - pilot run.
2. Сформировать реестр покрытия:
   - какие компоненты переведены на OpenSpec;
   - статус стабильности nightly.
3. Добавить регулярный аудит drift (spec vs Case labels).

**Критерий Done:**
- Планомерное подключение новых компонентов без регрессий в основном контуре.

---

## Матрица приоритетов

1. **Сначала:** Этап A (стабильный пилот в Jenkins).
2. **Далее:** Этап B (усиление quality gates).
3. **Потом:** Этап C (автопрофилирование count/SLA).
4. **Затем:** Этап D (component-only simulation).
5. **Параллельно после стабилизации:** Этап E (масштабирование).

---

## Операционный чеклист на каждый запуск

1. Проверить актуальность `openspec.raw.yaml`.
2. Проверить параметры `USE_OPENSPEC=true`, `OPENSPEC_PATH`, `SPEC_PATH`, `APPLICATION`.
3. Убедиться, что `meta.service == APPLICATION`.
4. Для первого запуска использовать safe-smoke (`TARGET_PERCENT=5`).
5. Проверить, что pre-step конвертации и validate прошли успешно.
6. Проверить артефакты Analyze и публикацию в Confluence.

---

## Безопасный rollback

Если OpenSpec-путь нестабилен:

1. `USE_OPENSPEC=false`.
2. `USE_SDD_SPEC=true`.
3. Использовать last-known-good `SPEC_PATH`.
4. Продолжать nightly без изменений runtime-скриптов Gatling.

---

## Версия 2: спринт-план на 8 недель (с трудозатратами)

Ниже рабочая версия roadmap в формате спринтов.  
Оценки приведены в **чел-днях** (man-days) и рассчитаны на команду 2-3 инженера
(LT + Python/Jenkins + поддержка доменной команды).

## Sprint 0 (уже выполнено) — Foundation
**Период:** завершен  
**Цель:** подготовить безопасный OpenSpec bridge без поломки runtime.

**Сделано:**
- `openspec_to_sdd.py` добавлен.
- Jenkins pre-step добавлен в `Start/Analyze/Foreground`.
- Контракт, runbook и пресеты оформлены в `docs/*`.
- Локальный smoke пройден.

**Факт трудозатрат:** ~5-7 чел-дней.

---

## Sprint 1 (Недели 1-2) — Production Pilot Stabilization
**Цель:** стабилизировать реальный пилот `ZK/ComplianceRequests` в ночном контуре.

**Задачи:**
1. Провести 3+ последовательных цикла `SAFE_SMOKE` (`Start + Analyze`).
2. Провести 2+ цикла `PILOT` (`TARGET_PERCENT=100`).
3. Зафиксировать baseline-метрики:
   - время выполнения;
   - частота фейлов;
   - качество отчетов в Confluence.
4. Описать типовые инциденты/ошибки запуска.

**Артефакты:**
- обновление `docs/OPENSPEC_ROLLOUT_RUNBOOK.md` (реальные кейсы),
- журнал baseline (в docs или Confluence).

**Оценка:** 6-8 чел-дней.

---

## Sprint 2 (Недели 3-4) — Quality Gates and Preflight
**Цель:** fail-fast до remote run и уменьшение "дорогих" падений.

**Задачи:**
1. Усилить `SDD/ltAuto/spec_validate.py`:
   - дубликаты `endpoints.id` и `endpoints.label`;
   - пустые `method/path`;
   - дополнительные проверки согласованности `meta.service`.
2. Добавить `ltAuto/openspec_preflight.py`:
   - convert -> validate -> compact summary.
3. В Jenkins добавить отдельный `Preflight` stage (до `Prepare & Sync`).
4. Обновить пресеты и инструкцию по ошибкам preflight.

**Артефакты:**
- обновленный `spec_validate.py`,
- новый `openspec_preflight.py`,
- изменения в `Jenkinsfile_NT_Start` и `Jenkinsfile_NT_Analyze_Report`.

**Оценка:** 8-10 чел-дней.

---

## Sprint 3 (Недели 5-6) — Count/SLA Automation
**Цель:** снизить ручные правки `count` и повысить реалистичность профиля.

**Задачи:**
1. Утилита подсказки/заполнения `count` из `history_repo`.
2. Поддержка режимов профиля в OpenSpec:
   - `smoke`, `regular`, `peak`.
3. Jenkins параметр `PROFILE_MODE` + передача в конвертер/пост-процессинг.
4. Документировать SLA policy (минимум для критичных endpoint'ов).

**Артефакты:**
- новая утилита в `ltAuto` или `SDD/ltAuto`,
- доработанный `openspec_to_sdd.py` (при необходимости),
- обновленные Jenkins presets.

**Оценка:** 10-12 чел-дней.

---

## Sprint 4 (Недели 7-8) — Component-only Simulations + Scale Template
**Цель:** безопасные smoke-прогоны по компоненту и готовность к масштабированию.

**Задачи:**
1. Генератор simulation-контура по компоненту из `spec.yaml`.
2. Параметр `SIMULATION_SCOPE` (`component_only|domain_all`) в Start job.
3. Стандартизированный onboarding template для новых компонентов:
   - export -> convert -> preflight -> smoke -> pilot.
4. Реестр покрытия компонентов OpenSpec по АС.

**Артефакты:**
- генератор simulation (новая утилита/скрипт),
- изменения в `Jenkinsfile_NT_Start`,
- документ "OpenSpec coverage registry".

**Оценка:** 12-14 чел-дней.

---

## Сводная оценка 6-8 недель

- **Sprint 1:** 6-8 чел-дней  
- **Sprint 2:** 8-10 чел-дней  
- **Sprint 3:** 10-12 чел-дней  
- **Sprint 4:** 12-14 чел-дней

**Итого (без Sprint 0):** 36-44 чел-дня за 8 недель.  
**Итого с уже выполненным Sprint 0:** 41-51 чел-день.

---

## Распределение ролей (рекомендуемое)

- **LT engineer (0.5-1.0 FTE):**
  сценарии/симуляции, smoke-проверки, SLA-правила.
- **Automation engineer Python/Jenkins (0.5-1.0 FTE):**
  конвертеры, validate/preflight, pipeline stages, presets.
- **Domain owner / Analyst (0.2-0.4 FTE):**
  ревью OpenSpec полей, приоритеты endpoint'ов, accept критерии.

---

## KPI на конец 8-й недели

1. Не менее 2 доменов с рабочим OpenSpec-потоком.
2. Не менее 70% пилотных запусков проходят без ручной правки спеки.
3. Все ошибки контракта ловятся на preflight (до remote run).
4. Время подготовки нового компонента к nightly снижено минимум на 30%.
