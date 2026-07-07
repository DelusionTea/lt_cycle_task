---
name: one-file-at-a-time
description: >-
  Обязательный режим работы для слабых моделей (DeepSeek Flash и аналоги): обрабатывает
  репозиторий Gatling/ltAuto строго по одному файлу за итерацию с gate-проверкой
  перед переходом к следующему. Использовать при массовом рефакторинге весов,
  сборке profile.yaml, правках сценариев/simulations, доработке ltAuto или Jenkins —
  когда нельзя доверять модели «сделать всё сразу».
---

# Режим «один файл за итерацию»

Этот скилл **главнее** остальных при массовых задачах. Слабая модель не должна
менять несколько файлов в одном ответе.

## Жёсткие правила (нарушение = стоп)

1. **Один файл за итерацию** — в одном ответе меняется максимум **один** путь.
2. **Сначала gate, потом следующий файл** — без зелёной проверки переход запрещён.
3. **Не трогай соседние файлы** «заодно», даже если «очевидно».
4. **Не правь вручную** то, что делает скрипт (`weights_codemod.py`, `sim_to_profile.py`).
5. **Не читай весь репозиторий** — только текущий файл + минимум контекста (Case-каталог сценария).
6. После итерации **остановись** и сообщи: файл, что сделано, вывод gate, следующий кандидат.

Контракт имён: [gatling-profile-conventions.md](../gatling-profile-conventions/SKILL.md).

## Рабочий каталог

Все команды — из **`gatling/gatlingScripts/`** (там `pom.xml`, `ltAuto/`, `profiles/`, `src/`).

```text
gatling/gatlingScripts/
├── ltAuto/              ← Python: НЕ переписывать логику без задачи; запускать скрипты
├── profiles/            ← YAML: один profile за раз
├── src/test/java/
│   ├── cases/<домен>/   ← только чтение (имена var↔log)
│   ├── config/          ← ProfileConfig.java — редко, отдельная итерация
│   ├── scenarios/       ← рефакторинг весов: ОДИН *Scenario.java за итерацию
│   └── simulations/     ← injectOpen/throttle: ОДИН Simulation.java за итерацию
└── pom.xml
```

Jenkins-файлы — в **`gatlingJenkins/`** (отдельный репо-корень), тоже **один Jenkinsfile за итерацию**.

---

## Цикл одной итерации (копируй в ответ)

```text
Итерация N
- [ ] Файл: <единственный путь>
- [ ] Действие: <dry-run | codemod | sim_to_profile | ручная правка по шаблону>
- [ ] Gate: <команда> → результат OK/FAIL
- [ ] Статус: DONE / BLOCKED
- [ ] Следующий кандidat (НЕ трогать до подтверждения): <путь>
```

**При FAIL:** исправь **только этот файл**, повтори gate. Не открывай следующий.

---

## Как выбрать очередь файлов

Пользователь задаёт **область** (домен/задачу). Ты **только перечисляешь** кандидатов, не правишь все сразу.

### Рефакторинг весов → ProfileConfig

Список файлов с хардкод-весами (один домен):
```bash
cd gatling/gatlingScripts
grep -rlE 'new Choice\.WithWeight\(\s*[0-9]' src/test/java/scenarios/pprbSberrating/ | sort
```

Эталон уже готов: `src/test/java/scenarios/pprbSberrating/LicensesScenario.java` — сверяйся с ним.

**На каждую итерацию** — скилл [gatling-weights-to-profileconfig.md](../gatling-weights-to-profileconfig/SKILL.md), но **только один путь**:
```bash
python3 ltAuto/weights_codemod.py <ОДИН_ФАЙЛ.java> --dry-run
python3 ltAuto/weights_codemod.py <ОДИН_ФАЙЛ.java>
python3 ltAuto/verify_profile.py --scenario <ОДИН_ФАЙЛ.java> --compile
```
Gate: см. [verify-gatling-profile.md](../verify-gatling-profile/SKILL.md). **RESULT: PASS** — единственный критерий «итерация OK».

`--scenario-name` указывай, если SCN не выводится из имени класса (сверь с `profiles/<имя_АС>/profile.yaml`).

### Профиль из весов одного сценария

**Один** сценарий → **один** yaml (не правь `profile.yaml` пачкой):
```bash
python3 ltAuto/sim_to_profile.py \
  --scenario src/test/java/scenarios/<домен>/<One>Scenario.java \
  --rps <число> --duration <сек> \
  --scenario-name <SCN> \
  --request-classes src/test/java/cases/<домен> \
  --output profiles/<имя_АС>/profile.<SCN>.generated.yaml
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/<домен>/<One>Scenario.java \
  --profile profiles/<имя_АС>/profile.<SCN>.generated.yaml \
  --scenario-name <SCN> --round-trip --allow-numeric-weights
```
(до codemod — с `--allow-numeric-weights`; после codemod — `--compile` без этого флага)

Скилл: [build-profile-from-simulation.md](../build-profile-from-simulation/SKILL.md).

### Simulation (injectOpen / throttle)

Один файл, например `src/test/java/simulations/.../OTT_all_debug.java`:

```bash
python3 ltAuto/inject_codemod.py <Simulation.java> \
  --scenarios-dir src/test/java/scenarios \
  --only-scenario LicensesScenario \
  --limit 1 --dry-run
python3 ltAuto/inject_codemod.py ... # без --dry-run
```

Скилл: [injectopen-from-profile.md](../injectopen-from-profile/SKILL.md).  
**Одна итерация = `--limit 1`** (или `--only-scenario` + `--only-field`).

Gate: `mvn -q -DskipTests test-compile`.

### profiles/<имя_АС>/profile.yaml

- **Не генерируй count для всех UC сразу** из головы.
- Итерация: добавить/исправить **одну секцию** (один SCN или один блок `count` для одного домена) + gate:
```bash
python3 ltAuto/profile_to_props.py --profile profiles/<имя_АС>/profile.yaml --target_percent 100 --output /tmp/check.properties
```

### ltAuto/*.py

Один скрипт за задачу. Gate — запуск на фикстуре:
```bash
python3 ltAuto/gatling_parser.py --simulation_log <один.log> --profile profiles/profile.example.yaml --output_dir /tmp/out --script_name test
```

### Jenkins (gatlingJenkins/)

Один `Jenkinsfile*` за итерацию. Gate — синтаксис (если доступен) или чек-лист путей `${GATLING_DIR}/ltAuto/...`.

---

## Таблица: тип файла → что можно в одной итерации

| Тип | Путь | Действие | Gate |
|-----|------|----------|------|
| Сценарий | `src/test/java/scenarios/**/*.java` | `weights_codemod.py` на **1 файл** | `verify_profile.py --scenario ... [--compile]` |
| Case | `src/test/java/cases/**/*.java` | только чтение / сверка имён | `verify_profile.py --profile ...` |
| Профиль | `profiles/*.yaml` | правка **одного SCN/count-блока** | `verify_profile.py --profile ...` |
| Simulation | `src/test/java/simulations/**/*.java` | `inject_codemod.py --limit 1` | `mvn test-compile` |
| Config | `src/test/java/config/ProfileConfig.java` | редко, отдельная задача | compile |
| Python | `ltAuto/*.py` | точечный фикс | `--help` или тест-команда из docstring |
| Jenkins | `gatlingJenkins/Jenkinsfile*` | одна стадия/путь | ручной чек-лист |

---

## Шаблон промпта для пользователя (GigaCode / Flash)

Скопируй в начало сессии:

```text
Режим: one-file-at-a-time (gigacode/skills/one-file-at-a-time/SKILL.md).
Задача: <кратко>.
Область: gatling/gatlingScripts/src/test/java/scenarios/pprbSberrating/
Сделай ТОЛЬКО первую итерацию: найди список файлов, обработай ОДИН файл, gate, стоп.
Не переходи ко второму файлу без моего «далее».
```

Для продолжения:

```text
Далее. Следующая итерация по one-file-at-a-time. Файл: <путь из очереди>.
```

---

## Журнал прогресса (веди в ответах)

```text
| # | Файл | Gate | Статус |
|---|------|------|--------|
| 1 | scenarios/.../LicensesScenario.java | compile OK | DONE |
| 2 | scenarios/.../RatingScenario.java | — | NEXT |
```

Не помечай DONE без вывода gate.

---

## Чего не делать (типичные ошибки Flash)

- `weights_codemod.py` на **весь каталог** scenarios без явного разрешения пользователя.
- Править веса regex-ом вручную вместо codemod.
- Менять `profile.yaml` и Java-сценарий в одной итерации.
- «Собрал profile для всех 17 сценариев» — только **один SCN за итерацию**.
- Сказать «готово» без `mvn test-compile` или `profile_to_props`.

---

## Связь с другими скиллами

| Задача | Сначала | Затем операция |
|--------|---------|----------------|
| Выбор задачи | [task-router-gigacode](../task-router-gigacode/SKILL.md) | один скилл из таблицы |
| Перед правкой | [extract-before-edit](../extract-before-edit/SKILL.md) | dump_scenario_meta.py |
| Массовый вынос весов | **one-file-at-a-time** | gatling-weights-to-profileconfig (на 1 файл) |
| Профиль из RPS | **one-file-at-a-time** | build-profile-from-simulation (на 1 SCN) |
| count по логу | **one-file-at-a-time** | align-count-from-simulation-log (1 ключ) |
| injectOpen | **one-file-at-a-time** | injectopen-from-profile + inject_codemod (--limit 1) |
| Ответ пользователю | [session-handoff-template](../session-handoff-template/SKILL.md) | gate + «жду далее» |
| Любая правка | gatling-profile-conventions | one-file-at-a-time |

Стартовая точка репозитория: [AI_INSTRUCTION_GIGACODE.md](../../../docs/ai/AI_INSTRUCTION_GIGACODE.md).
