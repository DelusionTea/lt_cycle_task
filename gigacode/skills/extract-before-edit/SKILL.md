---
name: extract-before-edit
description: >-
  Перед правкой сценария или Simulation — снять метаданные скриптом dump_scenario_meta.py.
  Запрет на «прочитал Java глазами» для слабых моделей. Использовать перед codemod,
  injectOpen, sim_to_profile и align-count.
---

# Extract before edit: сначала факты, потом правка

Слабая модель **не парсит Java сама**. Сначала JSON из скрипта, правка — только по нему.

Контракт имён: [gatling-profile-conventions.md](../gatling-profile-conventions/SKILL.md).  
Режим итераций: [one-file-at-a-time.md](../one-file-at-a-time/SKILL.md).

## Когда применять

- Перед `weights_codemod.py` — сверить SCN, список весов, сумму.
- Перед `sim_to_profile.py` — убедиться, что веса найдены.
- Перед правкой `injectOpen` — список блоков и дефолтные users.
- Пользователь спрашивает «что в этом сценарии» — **только через скрипт**.

## Команды

Рабочий каталог: `gatling/gatlingScripts/`.

**Сценарий (веса + SCN + Case var↔log):**
```bash
python3 ltAuto/dump_scenario_meta.py \
  --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
  --request-classes src/test/java/cases/pprbSberrating
```

**Симуляция (injectOpen-блоки):**
```bash
python3 ltAuto/dump_scenario_meta.py \
  --simulation src/test/java/simulations/pprbSberrating/All/OTT_all_debug.java
```

**Сохранить в файл (для handoff):**
```bash
python3 ltAuto/dump_scenario_meta.py \
  --scenario .../LicensesScenario.java \
  --request-classes src/test/java/cases/pprbSberrating \
  --output /tmp/licenses_meta.json
```

## Что смотреть в JSON

| Поле | Значение |
|------|----------|
| `scenario.scn` | должен совпасть с `injection.scenarios.<SCN>` в profile.yaml |
| `scenario.weights[].var` | ключи для getWeight / count |
| `scenario.numeric_with_weight_left` | `true` → нужен codemod |
| `simulation.inject_blocks[]` | **один блок = одна итерация** injectOpen |
| `simulation.inject_blocks[].suggested` | шаблон замены (не копировать слепо — проверь SCN) |

## Запреты

- Не выводить список весов «из памяти» без запуска скрипта.
- Не править файл в той же итерации, если extract не запускался (кроме явной команды «только extract»).
- Не использовать `grep WithWeight` вместо `dump_scenario_meta` для итогового списка — grep только для **очереди файлов**.

## Gate

- Exit code 0, JSON валиден.
- Для сценария: `weights` не пуст (иначе codemod бессмысленен).
- Ответ пользователю — по [session-handoff-template.md](../session-handoff-template/SKILL.md).

## Связь с другими скиллами

| После extract | Следующий скилл |
|---------------|-----------------|
| `numeric_with_weight_left: true` | gatling-weights-to-profileconfig |
| нужен profile из RPS | build-profile-from-simulation |
| inject_blocks | injectopen-from-profile + inject_codemod.py |
| count vs log | align-count-from-simulation-log |
