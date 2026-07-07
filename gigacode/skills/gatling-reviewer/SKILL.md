---
name: gatling-reviewer
description: >-
  Ревью Gatling PR для коллег: жёсткие gate (mvn compile, verify_profile, JSON,
  security grep) + семантический чек-лист requirements. Использовать при ревью
  PR, изменениях в cases/scenarios/simulations/feeders/profiles, перед merge НТ-кода,
  или по запросу «проверь Gatling» / skill gatling-reviewer.
---

# Ревьюер кода Gatling (самостоятельное ревью с ИИ)

Скилл для **коллег**: провести ревью без участия эксперта. ИИ **не заменяет** gate-скрипты —
сначала запускает проверки, потом читает код по чек-листу.

## Когда применять

- PR с изменениями в `src/test/java/{cases,scenarios,simulations,feeders}/`
- Правки `profiles/<АС>/profile.yaml`, `ProfileConfig`, веса/injectOpen
- Перед merge в ветку с ночным НТ

## Быстрый старт (скопируй в GigaCode)

```text
skill: gatling-reviewer
Модуль (АС): pprbSberrating
Файлы PR: <список или diff>

1) Запусти review_gates.py (strict).
2) Если RESULT: FAIL — только FAIL, без «ОК».
3) Семантическое ревью по review_script.md.
4) Отчёт в формате ниже. Вердикт: MERGE / FIX REQUIRED.
```

Рабочий каталог: **`gatling/gatlingScripts/`** (где `pom.xml`, `src/`, `ltAuto/`, `profiles/`).

---

## Фаза 1 — жёсткие gates (обязательно)

ИИ **обязан** выполнить команды и вставить вывод. Без gates вердикт **FIX REQUIRED**.

### 1.1 Единый gate-скрипт ревью

```bash
cd gatling/gatlingScripts

# По модулю (типично для PR целиком по одной АС):
python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py \
  --module pprbSberrating \
  --strict

# По конкретным файлам из PR:
python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py \
  --files \
    src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
    src/test/java/cases/pprbSberrating/LicensesCase.java \
  --strict

# С профилем НТ (если меняли profile.yaml или веса):
python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py \
  --module efsFinmonWeb \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --application efsFinmonWeb \
  --compile \
  --strict
```

**Критерий:** последняя строка `RESULT: PASS`. Иначе — блокирующие замечания.

| Gate | Что проверяет |
|------|----------------|
| `mvn test-compile` | Java компилируется (**линтер сборки**) |
| `verify_profile.py` | числовые `WithWeight`, SCN, profile.yaml, round-trip |
| Security grep | секреты в Cases/Scenarios; feeders без `DEBUG ONLY` |
| Cases | `extends Methods`, `http()` → `.check(status())` |
| JSON | `ElFileBody` → файл существует + валидный JSON |
| Simulations | `Simulation`, `setUp`, `protocols`; WARN на хардкод `atOnceUsers` |

Флаг `--quick` — только compile + verify (быстрый прогон).  
Флаг `--skip-compile` — если нет Maven локально (тогда WARN, не для merge).

### 1.2 Дополнительные gates по типу изменений

**Изменён Scenario с весами** (должен быть ProfileConfig):
```bash
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/<module>/<Name>Scenario.java \
  --compile --strict
```

**Изменён Simulation с injectOpen:**
```bash
python3 ltAuto/dump_scenario_meta.py \
  --simulation src/test/java/simulations/<module>/.../OTT_all_debug.java

grep -n 'atOnceUsers([0-9]' src/test/java/simulations/<path>.java \
  && echo "WARN: хардкод users — нужен inject_codemod.py"
```

**Изменён profile.yaml:**
```bash
python3 ltAuto/verify_profile.py \
  --profile profiles/<APPLICATION>/profile.yaml \
  --application <APPLICATION> --strict

python3 ltAuto/profile_to_props.py \
  --profile profiles/<APPLICATION>/profile.yaml \
  --target_percent 100 --output /tmp/check.properties
```

**Контракт имён var↔log↔yaml** (если трогали count/веса):
```bash
# см. gigacode/skills/gatling-profile-conventions/SKILL.md
python3 ltAuto/log_labels_to_profile.py \
  --simulation_log <path/to/simulation.log> \
  --profile profiles/<APPLICATION>/profile.yaml \
  --application <APPLICATION>
```

---

## Фаза 2 — семантическое ревью (чек-лист)

После **PASS** gates (или параллельно для WARN) — пройти по файлам:

| Документ | Содержание |
|----------|------------|
| [requirements.md](requirements.md) | Архитектура, именование, Cases/Scenarios/Simulations, безопасность |
| [review_script.md](review_script.md) | Пошаговый чек-лист, формат замечаний `[module:path:line]` |

### NT-пайплайн (если затронут профиль/веса/inject)

Сверка с `gigacode/skills/`:

| Тема | Скилл |
|------|-------|
| Контракт имён | `gatling-profile-conventions` |
| Веса → ProfileConfig | `gatling-weights-to-profileconfig` + `weights_codemod.py` |
| injectOpen | `injectopen-from-profile` + `inject_codemod.py` |
| count по логу | `align-count-from-simulation-log` |
| Gate после правок | `verify-gatling-profile` |

**Блокирующие для NT-профиля:**
- числовой `new Choice.WithWeight(25, ...)` без `ProfileConfig.getWeight`
- choice-ключ ≠ имя переменной Case
- SCN в Java ≠ `injection.scenarios.<SCN>` в yaml
- `profiles/profile.yaml` в корне (устарело) — только `profiles/<APPLICATION>/`

---

## Фаза 3 — ручные grep (быстрые линтеры)

ИИ выполняет и цитирует результат:

```bash
MODULE=pprbSberrating

# Чужие Headers/Methods (типичная ошибка pprbComplianceRequests)
grep -rn 'import feeders\.' src/test/java/cases/$MODULE/ | grep -v "feeders\.$MODULE"

# Case без status check (дублирует gate, но наглядно для отчёта)
grep -rn '= http(' src/test/java/cases/$MODULE/ | head -20

# Закомментированные WithWeight не трогать — только активные
grep -rn 'Choice\.WithWeight' src/test/java/scenarios/$MODULE/ | grep -v '^[^:]*://'

# Production URL в debug
grep -rni 'production\.' src/test/java/simulations/$MODULE/ || true
```

---

## Режимы ревью

| Режим | Команда | Когда |
|-------|---------|-------|
| **strict** (merge) | `review_gates.py ... --strict --compile` | Перед merge |
| **quick** | `review_gates.py ... --quick` | Черновик PR, ранний фидбек |
| **full** | strict + review_script.md целиком | Крупный PR |

---

## Формат отчёта (обязательный)

```markdown
## Ревью Gatling — <module / PR>

### Gates (жёсткие)
| Gate | Результат | Вывод |
|------|-----------|-------|
| review_gates.py | PASS/FAIL | `RESULT: ...` |
| verify_profile | PASS/FAIL/skip | ... |
| mvn test-compile | PASS/FAIL/skip | ... |

### Блокирующие (FAIL) — исправить до merge
1. [module:path:line] описание

### Предупреждения (WARN)
1. ...

### Семантика (из review_script)
- Cases: ...
- Scenarios: ...
- Simulations: ...

### NT-профиль (если применимо)
- ProfileConfig / profile.yaml: ...

### Вердикт
- [ ] **MERGE** — все gates PASS, нет блокирующих
- [ ] **FIX REQUIRED** — есть FAIL или критичные проблемы безопасности
```

**Правило для ИИ:** не ставить MERGE, если не приложен вывод `review_gates.py` с `RESULT: PASS`.

---

## Архитектура (кратко)

| Префикс | Примеры АС |
|---------|------------|
| `pprb*` | `pprbSberrating`, `pprbComplianceRequests`, `ZK` |
| `efs*` | `efsFinmonWeb`, `efsSberratingWeb` |

```
src/test/java/{cases,scenarios,simulations,feeders}/<module>/
profiles/<APPLICATION>/profile.yaml   # APPLICATION = имя cases/
src/test/resources/JSONs/<module>/...
ltAuto/*.py                           # gate-скрипты НТ
```

## Безопасность (напоминание)

- Пароли БД **только** в feeders + `// DEBUG ONLY` (модули `pprb*`, `efs*`)
- **Запрещено** в Cases/Scenarios: пароли, API keys, JWT secrets
- Формат замечания: `[pprbSberrating:Cases/ArbitrCase.java:25] ...`

---

## Делегирование эксперту (эскалация)

Эскалируй автору PR / мейнтейнеру НТ, если:
- gates PASS, но меняется контракт профиля для production Jenkins
- новая АС без `profiles/<APPLICATION>/`
- `verify_profile` FAIL и автор не знает SCN/count

---

## Связанные файлы скилла

```
gigacode/skills/gatling-reviewer/
├── SKILL.md              ← этот файл
├── requirements.md       ← стандарты кода
├── review_script.md      ← детальный чек-лист
└── scripts/
    └── review_gates.py   ← жёсткие gates
```

*Обновлено: 2026-07-07*
