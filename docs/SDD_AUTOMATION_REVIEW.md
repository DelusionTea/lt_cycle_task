## Анализ автоматизации и привязки к SDD-спекам

### 1) Инвентаризация текущих автоматизаций

Допущение: на момент начала работ **все спеки уже заполнены** в `SDD/specs/`,
поэтому фокус — на автоматизации генерации и валидации, а не на ручном заполнении.

**Jenkins пайплайны**
- `Jenkinsfile_NT_Start` — старт прогона, генерация `profile.properties` через `ltAuto/profile_to_props.py`, далее запуск Gatling.
- `Jenkinsfile_Gatling_Foreground_Analyze_Mail` — foreground запуск, затем парсинг `gatling_parser.py` и mail.
- `Jenkinsfile_NT_Analyze_Report` — ночная обработка результатов, `gatling_parser.py`, `compare_runs.py`, `summary_to_confluence.py`.

**ltAuto утилиты**
- `profile_to_props.py` — `profile.yaml` → `profile.properties` (веса из `count`).
- `log_labels_to_profile.py` — сопоставление labels из `simulation.log` с `count`.
- `dump_scenario_meta.py` — извлечение метаданных сценариев/симуляций.

**SDD утилиты**
- `SDD/ltAuto/spec_to_profile.py` — `spec.yaml` → `profile.spec.yaml`.
- `SDD/ltAuto/spec_validate.py` — проверка `spec → Case/labels`.

### 2) Что можно привязать к `spec.yaml`

1) **Генерация профиля**
   - `spec.yaml` → `profile.spec.yaml` (через `spec_to_profile.py`).
   - Далее `profile.spec.yaml` → `profile.properties` (через `profile_to_props.py`).

2) **Валидация соответствия**
   - `spec_validate.py` как gate‑проверка до запуска теста.

3) **Автоматическое обновление `count`**
   - `log_labels_to_profile.py` выдаёт подсказки по `count` из `simulation.log`.
   - Можно использовать для обновления `spec.yaml` (ручная правка по подсказкам).

4) **Метаданные сценариев**
   - `dump_scenario_meta.py` полезен для проверки соответствия сценариев спекам
     (особенно когда в сценарии ещё остались числовые веса).

### 3) Рекомендации по автоматическому обновлению

**Jenkins: gate‑шаги**
- Добавить в `Jenkinsfile_NT_Start` и `Jenkinsfile_Gatling_Foreground_Analyze_Mail` этап
  `spec_validate.py` перед генерацией `profile.properties`.

**Jenkins: spec‑first генерация**
- В `Generate profile.properties` запускать `spec_to_props.py` напрямую по
  `SDD/specs/<АС>/<Component>/spec.yaml` (без `profile.spec.yaml`).

**Контроль синхронизации**
- Ввести проверку: если есть и `profile.yaml`, и `profile.spec.yaml`,
  сравнивать ключи `count` и `request_classes`.

### 4) Будущие улучшения

- **spec_to_props**: объединить `spec_to_profile.py` + `profile_to_props.py`
  в один прямой пайплайн `spec.yaml → profile.properties`.
- **spec_from_log**: генерация черновой спеки на основе `simulation.log`
  и `case_parser.py` (без выдумывания полей).
- **CI‑флаг**: параметр `USE_SDD_SPEC=true` для переключения пайплайна на spec‑first.

### 5) Итог: что можно улучшить и привязать к спекам

- `profile.yaml` → заменить на `profile.spec.yaml`, генерируемый из спеки.
- Gate‑валидация `spec_validate.py` перед запуском.
- Авто‑подсказки `count` через `log_labels_to_profile.py`.
- Проверка соответствия сценариев с помощью `dump_scenario_meta.py`.

### 6) Реализация (текущее состояние)

- В `Jenkinsfile_NT_Start` добавлены параметры:
  - `USE_SDD_SPEC` (boolean)
  - `SPEC_PATH` (путь к spec.yaml)
  - при `USE_SDD_SPEC=true` запускается `spec_validate.py` и `spec_to_props.py`
    (без генерации `profile.spec.yaml`).
- В `Jenkinsfile_Gatling_Foreground_Analyze_Mail` добавлены те же параметры
  и spec‑first генерация профиля через `spec_to_props.py`.
- В `Jenkinsfile_NT_Analyze_Report` добавлены те же параметры и подготовка
  временного профиля `profile.from.spec.yaml` перед парсингом.
