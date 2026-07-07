---
name: injectopen-from-profile
description: >-
  Заменить хардкод injectOpen(atOnceUsers(N)) на ProfileConfig.getInjectUsers(SCN, N)
  в Simulation.java через inject_codemod.py. Один блок за итерацию (--limit 1).
  Gate mvn test-compile.
---

# injectOpen из ProfileConfig

Интенсивность сценариев читается из `profile.properties`:
`inject.<SCN>.users` ← `injection.scenarios.<SCN>.users × target_percent`.

Java API: `ProfileConfig.getInjectUsers(SCN, def)`, `getDuration(def)`, `getRampup(def)`.

Контракт SCN: [gatling-profile-conventions.md](../gatling-profile-conventions/SKILL.md).  
Режим: [one-file-at-a-time.md](../one-file-at-a-time/SKILL.md) — **один блок injectOpen за итерацию**.

## Не правь вручную — используй codemod

Скрипт: **`ltAuto/inject_codemod.py`** (аналог `weights_codemod.py`).

### Перед правкой: extract (опционально)

```bash
python3 ltAuto/dump_scenario_meta.py \
  --simulation src/test/java/simulations/pprbSberrating/All/OTT_all_debug.java
```

### One-file-at-a-time (рекомендуется)

**Dry-run** — оценить одну замену:
```bash
python3 ltAuto/inject_codemod.py \
  src/test/java/simulations/pprbSberrating/All/OTT_all_debug.java \
  --scenarios-dir src/test/java/scenarios \
  --only-scenario LicensesScenario \
  --limit 1 \
  --dry-run
```

**Применить одну замену:**
```bash
python3 ltAuto/inject_codemod.py \
  src/test/java/simulations/pprbSberrating/All/OTT_all_debug.java \
  --scenarios-dir src/test/java/scenarios \
  --only-scenario LicensesScenario \
  --limit 1
```

Следующая итерация — другой `--only-scenario` (например `RatingScenario`).

### Откуда берётся SCN

1. `--scn-map LicensesScenario=Licenses` или `--scn-map-json map.json`
2. Константа `String SCN = "..."` в `*Scenario.java` (если `--scenarios-dir` задан)
3. Эвристика: `LicensesScenario` → `"Licenses"`; `StatisticsScenario.scn_ott_debug_1` → `"Statistics_1"`

**Не угадывай SCN** — при сомнении сверь с Scenario-классом и `injection.scenarios` в yaml.

### Пример «было → стало»

**Было:**
```java
LicensesScenario.scn_ott_debug
    .injectOpen(atOnceUsers(5))
```

**Стало (codemod):**
```java
import config.ProfileConfig;

LicensesScenario.scn_ott_debug
    .injectOpen(atOnceUsers(ProfileConfig.getInjectUsers("Licenses", 5)))
```

`5` — дефолт, если `profile.properties` нет.

## profile.yaml (один SCN за итерацию)

```yaml
injection:
  duration: 3600
  rampup: 60
  scenarios:
    Licenses:
      users: 5
      request_classes:
        - src/test/java/cases/pprbSberrating
```

## Gate

```bash
mvn -q -DskipTests test-compile
```

Если profile уже настроен:
```bash
python3 ltAuto/verify_profile.py \
  --profile profiles/pprbSberrating/profile.yaml \
  --application pprbSberrating \
  --compile
```

## Запреты

- Не запускать codemod на весь `OTT_all_debug.java` без `--limit 1` / `--only-scenario` (слабые модели).
- Не путать SCN с именем класса `LicensesScenario` (SCN часто `Licenses`).
- Не трогать `.protocols(...)` в той же итерации.

## Ограничения codemod

- Поддерживается только `injectOpen(atOnceUsers(N))` на одной или двух строках.
- `constantUsersPerSec`, `rampUsers`, сложный throttle — вручную по шаблону или отдельная задача.
- Throttle `reachRps` / `holdFor` codemod **не** меняет (пока).

## Связанные скиллы

- Extract: [extract-before-edit.md](../extract-before-edit/SKILL.md)
- Новая АС: [onboard-new-as-domain.md](../onboard-new-as-domain/SKILL.md)
- Handoff: [session-handoff-template.md](../session-handoff-template/SKILL.md)
