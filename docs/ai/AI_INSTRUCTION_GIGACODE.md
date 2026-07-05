# Инструкция для ИИ (GigaCode): доработка НТ-пайплайна Gatling

Этот файл — стартовая точка для ИИ-ассистента. Прочитай его целиком перед работой.
Он даёт контекст, правила и ссылки на **скиллы** (пошаговые инструкции) и
**скрипты** (детерминированная логика). Главный принцип ниже.

## Главный принцип: запускай скрипты, не считай «в уме»

Задачи ниже — механические и требуют точности (сопоставление имён, арифметика).
Не редактируй веса и не считай count вручную: для этого есть готовые скрипты.
Твоя роль — **оркестратор + проверяющий**:

1. Определи задачу → открой нужный скилл.
2. Собери входные данные (пути, имена, RPS/длительность).
3. Запусти скрипт из скилла.
4. Выполни шаги верификации из скилла.
5. Останавливайся и чини, если проверка не прошла. Не продолжай на «авось».

## Где что лежит

Рабочий каталог для всех команд — `gatling/gatlingScripts/` (там `pom.xml`,
`ltAuto/`, `profiles/`, `src/`). Пути ниже — относительно него.

| Компонент | Путь | Назначение |
|-----------|------|------------|
| Скрипт codemod весов | `ltAuto/weights_codemod.py` | Хардкод весов → `ProfileConfig.getWeight` |
| Скрипт профиля из RPS | `ltAuto/sim_to_profile.py` | Веса + RPS(throttle) → `profile.yaml` (count/injection) |
| Генератор properties | `ltAuto/profile_to_props.py` | `profile.yaml` → `profile.properties` (веса из count) |
| Парсер Case | `ltAuto/case_parser.py` | Переменная ↔ имя запроса в логе |
| Java-читатель профиля | `src/test/java/config/ProfileConfig.java` | Веса/интенсивность в Gatling |
| Профили | `profiles/profile.yaml` | count, SLA, injection |

## Скиллы (читай нужный перед задачей)

| Скилл | Когда применять |
|-------|-----------------|
| [gatling-profile-conventions](skills/gatling-profile-conventions.md) | **Всегда** — контракт имён (var↔log↔yaml↔properties). |
| [gatling-weights-to-profileconfig](skills/gatling-weights-to-profileconfig.md) | «Вынести веса в параметры», рефакторинг `Choice.WithWeight`. |
| [build-profile-from-simulation](skills/build-profile-from-simulation.md) | «Собрать профиль по весам и RPS/throttle». |

## Маршрутизатор задач

- «Проверь инструкцию и **замени веса на подтягивание из профиля**» →
  скилл `gatling-weights-to-profileconfig`, скрипт `weights_codemod.py`,
  затем `mvn -q -DskipTests test-compile`.
- «**Составь профиль** на основе весов и RPS из throttle» →
  скилл `build-profile-from-simulation`, скрипт `sim_to_profile.py`,
  затем самопроверка через `profile_to_props.py`.
- «Пересчитай веса из count» → просто `profile_to_props.py` (веса считаются авто).

## Разбивай задачу на узкие шаги

Не пытайся сделать всё одним заходом. Дроби: «отрефакторь ОДИН сценарий по
скиллу и скомпилируй» → затем следующий. Так надёжнее и проще проверять.

## Подготовка окружения (один раз)

```bash
cd gatling/gatlingScripts
python3 -m venv .venv && . .venv/bin/activate
pip install pyyaml pandas requests
```

## Обязательная самопроверка (Definition of Done)

Прежде чем сказать «готово», убедись:

```
- [ ] Скрипт из скилла отработал без ошибок.
- [ ] Для весов: не осталось числовых Choice.WithWeight; проект компилируется
      (mvn -q -DskipTests test-compile); SCN совпадает с injection.scenarios.
- [ ] Для профиля: сумма count/duration ≈ RPS_total; все запросы на месте;
      profile_to_props.py читает профиль без ошибок.
- [ ] Ключи count/веса = ИМЕНА ПЕРЕМЕННЫХ Case (не выдуманные строки).
- [ ] Закомментированные // строки не тронуты.
```

## Чего не делать

- Не вписывать веса руками в код или в `profile.yaml` (веса — из count, авто).
- Не изобретать имена запросов — брать из Case-классов.
- Не менять remote-пути/креды и не коммитить секреты.
- Не пропускать шаг компиляции/самопроверки.

## Быстрый пример полного цикла

```bash
cd gatling/gatlingScripts && . .venv/bin/activate

# 1) Вынести веса в профиль (все сценарии домена)
python3 ltAuto/weights_codemod.py src/test/java/scenarios/pprbSberrating --dry-run
python3 ltAuto/weights_codemod.py src/test/java/scenarios/pprbSberrating
mvn -q -DskipTests test-compile

# 2) Собрать профиль из весов и RPS throttle
python3 ltAuto/sim_to_profile.py \
    --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
    --rps 200 --duration 600 --scenario-name Licenses \
    --request-classes src/test/java/cases/pprbSberrating \
    --output profiles/profile.generated.yaml

# 3) Самопроверка: профиль читается, веса совпадают по долям
python3 ltAuto/profile_to_props.py --profile profiles/profile.generated.yaml \
    --target_percent 100 --output /tmp/check.properties
```

Подробный контекст всего пайплайна — в `docs/NT_PIPELINE_AI_CONTEXT.md`,
задача для сотрудника — в `docs/NT_PIPELINE_TASK.md`.
