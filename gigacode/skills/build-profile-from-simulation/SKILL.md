---
name: build-profile-from-simulation
description: >-
  Составляет profile.yaml (count и injection) из весов randomSwitch сценария и
  суммарного RPS из throttle симуляции. Использовать, когда нужно «собрать профиль
  по весам и rps», «сгенерировать count из throttle» или подготовить profile.yaml
  под существующую симуляцию. Опирается на скрипт sim_to_profile.py.
---

# Профиль из весов и RPS (throttle)

Не считай count вручную — арифметику делает скрипт. Твоя роль: собрать входные
данные (веса читаются из сценария; RPS и длительность — из throttle или от
пользователя), запустить скрипт и проверить результат.

Сначала прочитай контракт имён: [gatling-profile-conventions.md](../gatling-profile-conventions/SKILL.md).

## Формула (закодирована в скрипте)

```
count_i = round(RPS_total × вес_i / Σвесов × 3600)   # запросов/час на 100% профиля
```
Веса читаются в обеих формах: хардкод `WithWeight(25, exec(Case.VAR))` и уже
вынесенные `getWeight(SCN, "VAR", 25)`. Ключи `count` — имена переменных.
`injection.duration` берётся из `holdFor` throttle (сек), но **не** умножает count.

## Чек-лист (копируй и отмечай)

```
- [ ] 1. Найти RPS_total (throttle: reachRps) и duration hold (holdFor)
- [ ] 2. Запустить sim_to_profile.py
- [ ] 3. Проверить: сумма весов, все запросы на месте, Σcount/3600 ≈ RPS_total
- [ ] 4. Дозаполнить SLA (95pct/50pct/error_count) и выверить injection.users
- [ ] 5. Прогнать profile_to_props.py — профиль читается без ошибок
```

**1. Данные о нагрузке.** Найди в симуляции throttle:
```java
.throttle(reachRps(200).in(60), holdFor(600))   // RPS_total=200, duration=600
```
Если throttle нет или парсинг неоднозначен — возьми числа у пользователя.

**2. Генерация** (из каталога `gatling/gatlingScripts`):
```bash
python3 ltAuto/sim_to_profile.py \
    --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
    --rps 200 --duration 600 \
    --scenario-name Licenses \
    --request-classes src/test/java/cases/pprbSberrating \
    --output profiles/pprbSberrating/profile.generated.yaml
```
Скрипт также умеет парсить RPS/длительность из файла симуляции — тогда вместо
`--rps/--duration` передай `--simulation <путь к Simulation.java>`.

**3. Верификация чисел.** `Σcount_i / 3600` должно давать ≈ `RPS_total`;
все запросы сценария присутствуют; веса просуммированы (в превью показано
`сумма весов`). Если запрос пропущен — проверь, что его строка не закомментирована.

**4. Дозаполни** в сгенерированном yaml: реальные `95pct/50pct/rps/error_count`
(в шаблоне стоят TODO-заглушки), при необходимости `sla_per_label`. Выверь
`injection.scenarios.<SCN>.users` (в open-модели ≠ RPS напрямую — это стартовая оценка).

**5. Gate — round-trip и profile_to_props:**
```bash
python3 ltAuto/verify_profile.py \
  --scenario <файл_сценария.java> \
  --profile profiles/pprbSberrating/profile.generated.yaml \
  --scenario-name <SCN> --round-trip
```
См. [verify-gatling-profile.md](../verify-gatling-profile/SKILL.md). **RESULT: PASS** — иначе стоп.

## Ограничения

- Если в сценарии несколько блоков `randomSwitch` с одинаковыми переменными —
  берётся последнее значение веса. Для конкретного блока временно закомментируй
  остальные или передай отдельный файл.
- Значения `holdFor(...)`/`during(...)` считаются в **секундах**. Если в коде
  указаны `Duration.ofMinutes(...)` — передай `--duration` в секундах вручную.
