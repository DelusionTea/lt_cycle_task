---
name: onboard-new-as-domain
description: >-
  Подключить новую АС (application system): profiles/<АС>/, cases/<АС>, один сценарий
  за итерацию через weights_codemod и verify. Чек-лист для GigaCode без «сделай всё сразу».
---

# Онбординг новой АС (домена)

**APPLICATION** = имя каталога в `cases/` и `profiles/` (например `efsFinmonWeb`, `pprbSberrating`).

Режим: [one-file-at-a-time.md](../one-file-at-a-time/SKILL.md) на **каждом** шаге ниже.

## Чек-лист (по порядку, один шаг = одна или несколько итераций)

```
- [ ] 1. Каталог Case существует: src/test/java/cases/<APPLICATION>/
- [ ] 2. profiles/<APPLICATION>/profile.yaml создан (из example или sim_to_profile)
- [ ] 3. request_classes указывает на cases/<APPLICATION>
- [ ] 4. Один Scenario → weights_codemod → verify --compile
- [ ] 5. count в profile (log_labels или sim_to_profile) → verify --profile
- [ ] 6. Round-trip весов (опционально): verify --round-trip
- [ ] 7. Simulation: один injectOpen → injectopen-from-profile
- [ ] 8. Jenkins: APPLICATION=<APPLICATION>, PROFILE_YAML пусто
```

Не отмечай пункт, пока gate для него не **PASS**.

## Шаг 1–2: структура

```text
profiles/
└── <APPLICATION>/
    └── profile.yaml

src/test/java/
├── cases/<APPLICATION>/          # UC*.java
├── scenarios/<пакет>/            # *Scenario.java
└── simulations/...               # главный Simulation.java
```

Скопировать шаблон:
```bash
mkdir -p profiles/<APPLICATION>
cp profiles/pprbSberrating/profile.example.yaml profiles/<APPLICATION>/profile.yaml
# отредактировать description, request_classes — ОДИН блок за итерацию
```

Минимум в yaml:
```yaml
description: "Профиль НТ <APPLICATION>"
target_percent: 100
request_classes:
  - src/test/java/cases/<APPLICATION>
count: {}
injection:
  duration: 3600
  rampup: 60
  scenarios: {}
```

## Шаг 4: первый сценарий (веса)

```bash
python3 ltAuto/dump_scenario_meta.py \
  --scenario src/test/java/scenarios/<пакет>/<First>Scenario.java \
  --request-classes src/test/java/cases/<APPLICATION>

python3 ltAuto/weights_codemod.py .../<First>Scenario.java --dry-run
python3 ltAuto/weights_codemod.py .../<First>Scenario.java
python3 ltAuto/verify_profile.py --scenario .../<First>Scenario.java --compile
```

Скилл: [gatling-weights-to-profileconfig.md](../gatling-weights-to-profileconfig/SKILL.md).

## Шаг 5: count / injection

**Вариант A** — есть simulation.log:
[align-count-from-simulation-log.md](../align-count-from-simulation-log/SKILL.md)

**Вариант B** — есть веса + RPS:
[build-profile-from-simulation.md](../build-profile-from-simulation/SKILL.md)

Добавь в yaml **один** SCN в `injection.scenarios`:
```yaml
injection:
  scenarios:
    Licenses:
      users: 10
      request_classes:
        - src/test/java/cases/<APPLICATION>
```

Gate:
```bash
python3 ltAuto/verify_profile.py \
  --profile profiles/<APPLICATION>/profile.yaml \
  --application <APPLICATION>
```

## Шаг 7: Simulation

[injectopen-from-profile.md](../injectopen-from-profile/SKILL.md) — по одному SCN:

```bash
python3 ltAuto/inject_codemod.py src/test/java/simulations/.../OTT_all_debug.java \
  --scenarios-dir src/test/java/scenarios \
  --only-scenario LicensesScenario --limit 1
mvn -q -DskipTests test-compile
```

## Шаг 8: Jenkins (smoke)

[jenkins-nt-smoke.md](../jenkins-nt-smoke/SKILL.md):

- `APPLICATION=<APPLICATION>`
- `PROFILE_YAML` пусто → `profiles/<APPLICATION>/profile.yaml`
- `GATLING_DIR=gatling/gatlingScripts`

## Очередь сценариев

Список файлов — только перечисление, не правка:
```bash
grep -rlE 'new Choice\.WithWeight\(\s*[0-9]' src/test/java/scenarios/<пакет>/ | sort
```

Дальше — по одному через task-router.

## Типичные ошибки

- `profiles/profile.yaml` в корне вместо `profiles/<APPLICATION>/`.
- SCN в Java ≠ ключ в `injection.scenarios`.
- count по лог-имени вместо имени переменной Case.

Стартовая точка: [AI_INSTRUCTION_GIGACODE.md](../../../docs/ai/AI_INSTRUCTION_GIGACODE.md).
