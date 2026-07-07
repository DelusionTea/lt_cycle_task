# Инструкция для ИИ (GigaCode): доработка НТ-пайплайна Gatling

Этот файл — стартовая точка для ИИ-ассистента. Прочитай его целиком перед работой.
Он даёт контекст, правила и ссылки на **скиллы** (пошаговые инструкции) и
**скрипты** (детерминированная логика). Главный принцип ниже.

## Главный принцип: один файл + скрипты, не «всё сразу»

**Для слабых моделей (DeepSeek Flash и аналоги) обязателен режим
[one-file-at-a-time](../../gigacode/skills/one-file-at-a-time/SKILL.md):** один путь за итерацию,
gate-проверка, стоп до команды пользователя «далее». Не обрабатывай каталог
scenarios целиком без явного разрешения.

Задачи ниже — механические и требуют точности (сопоставление имён, арифметика).
Не редактируй веса и не считай count вручную: для этого есть готовые скрипты.
Твоя роль — **оркестратор + проверяющий**:

1. Определи задачу → открой нужный скилл.
2. Собери входные данные (пути, имена, RPS/длительность).
3. Запусти скрипт из скилла.
4. Выполни **verify_profile.py** (скилл verify-gatling-profile) — только RESULT: PASS.
5. Останавливайся и чини, если verify FAIL. Не продолжай на «авось».

## Где что лежит

Рабочий каталог для всех команд — `gatling/gatlingScripts/` (там `pom.xml`,
`ltAuto/`, `profiles/`, `src/`). Пути ниже — относительно него.

| Компонент | Путь | Назначение |
|-----------|------|------------|
| Скрипт codemod весов | `ltAuto/weights_codemod.py` | Хардкод весов → `ProfileConfig.getWeight` |
| Скрипт codemod inject | `ltAuto/inject_codemod.py` | `atOnceUsers(N)` → `ProfileConfig.getInjectUsers` |
| Скрипт профиля из RPS | `ltAuto/sim_to_profile.py` | Веса + RPS(throttle) → `profile.yaml` (count/injection) |
| Генератор properties | `ltAuto/profile_to_props.py` | `profile.yaml` → `profile.properties` (веса из count) |
| Gate (PASS/FAIL) | `ltAuto/verify_profile.py` | Единая проверка после каждой итерации |
| Метаданные сценария | `ltAuto/dump_scenario_meta.py` | SCN, веса, injectOpen-блоки → JSON |
| Лог ↔ count | `ltAuto/log_labels_to_profile.py` | label из simulation.log ↔ ключи profile.yaml |
| Парсер прогона | `ltAuto/gatling_parser.py` | simulation.log → CSV + window.json |
| Сравнение прогонов | `ltAuto/compare_runs.py` | delta_table, summary.json |
| Grafana PNG | `ltAuto/render_export.py` | системные метрики по window.json |
| Confluence | `ltAuto/summary_to_confluence.py` | саммари из summary.json |
| Парсер Case | `ltAuto/case_parser.py` | Переменная ↔ имя запроса в логе |
| Java-читатель профиля | `src/test/java/config/ProfileConfig.java` | Веса/интенсивность в Gatling |
| Профили | `profiles/<APPLICATION>/profile.yaml` | count, SLA, injection (имя каталога = АС = cases/) |

## Скиллы GigaCode (`gigacode/skills/<имя>/SKILL.md`)

Скиллы для агента GigaCode — в **`gigacode/skills/`**. Список: [gigacode/README.md](../../gigacode/README.md).

**Старт сессии:** [task-router-gigacode](../../gigacode/skills/task-router-gigacode/SKILL.md)  
**Формат ответа:** [session-handoff-template](../../gigacode/skills/session-handoff-template/SKILL.md)

| Скилл | Когда применять |
|-------|-----------------|
| [task-router-gigacode](../../gigacode/skills/task-router-gigacode/SKILL.md) | **В начале сессии** |
| [session-handoff-template](../../gigacode/skills/session-handoff-template/SKILL.md) | **После каждой итерации** |
| [one-file-at-a-time](../../gigacode/skills/one-file-at-a-time/SKILL.md) | Массовая работа — один файл + gate |
| [gatling-profile-conventions](../../gigacode/skills/gatling-profile-conventions/SKILL.md) | Контракт имён |
| [extract-before-edit](../../gigacode/skills/extract-before-edit/SKILL.md) | Перед правкой — dump_scenario_meta |
| [gatling-weights-to-profileconfig](../../gigacode/skills/gatling-weights-to-profileconfig/SKILL.md) | Codemod весов |
| [build-profile-from-simulation](../../gigacode/skills/build-profile-from-simulation/SKILL.md) | profile из RPS |
| [align-count-from-simulation-log](../../gigacode/skills/align-count-from-simulation-log/SKILL.md) | count по логу |
| [injectopen-from-profile](../../gigacode/skills/injectopen-from-profile/SKILL.md) | inject_codemod |
| [onboard-new-as-domain](../../gigacode/skills/onboard-new-as-domain/SKILL.md) | Новая АС |
| [verify-gatling-profile](../../gigacode/skills/verify-gatling-profile/SKILL.md) | Gate verify_profile |
| [parse-gatling-run](../../gigacode/skills/parse-gatling-run/SKILL.md) | Парс прогона |
| [grafana-render-for-run](../../gigacode/skills/grafana-render-for-run/SKILL.md) | Grafana PNG |
| [confluence-summary-publish](../../gigacode/skills/confluence-summary-publish/SKILL.md) | Confluence |
| [jenkins-nt-smoke](../../gigacode/skills/jenkins-nt-smoke/SKILL.md) | Jenkins smoke |
| [gatling-reviewer](../../gigacode/skills/gatling-reviewer/SKILL.md) | **Ревью PR** для коллег |

## Маршрутизатор задач

Полная таблица — [task-router-gigacode](../../gigacode/skills/task-router-gigacode/SKILL.md). Кратко:

- «**Замени веса** на ProfileConfig» → `gatling-weights-to-profileconfig` + `weights_codemod.py`.
- «**Составь профиль** из RPS/throttle» → `build-profile-from-simulation` + `sim_to_profile.py`.
- «**count по логу**» → `align-count-from-simulation-log` + `log_labels_to_profile.py`.
- «**injectOpen из профиля**» → `injectopen-from-profile` + `inject_codemod.py --limit 1`.
- «**Новая АС**» → `onboard-new-as-domain`.
- «**Разобрать прогон**» → `parse-gatling-run`.
- «**Ревью PR**» → `gatling-reviewer` + `review_gates.py --strict`.
- «Пересчитать веса из count» → `profile_to_props.py` (веса авто).

## Разбивай задачу на узкие шаги

Следуй [one-file-at-a-time](../../gigacode/skills/one-file-at-a-time/SKILL.md): перечисли очередь файлов,
обработай **только первый**, прогони gate, **остановись**. Следующий файл — только
после «далее» от пользователя.

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
- [ ] verify_profile.py → RESULT: PASS (скилл verify-gatling-profile).
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

# 0) Маршрут + extract (один файл!)
python3 ltAuto/dump_scenario_meta.py \
  --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
  --request-classes src/test/java/cases/pprbSberrating

# 1) Вынести веса (ОДИН файл за итерацию — см. one-file-at-a-time)
python3 ltAuto/weights_codemod.py src/test/java/scenarios/pprbSberrating/LicensesScenario.java
python3 ltAuto/verify_profile.py --scenario .../LicensesScenario.java --compile

# 2) Собрать профиль из весов и RPS throttle
python3 ltAuto/sim_to_profile.py \
    --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
    --rps 200 --duration 600 --scenario-name Licenses \
    --request-classes src/test/java/cases/pprbSberrating \
    --output profiles/pprbSberrating/profile.generated.yaml

# 3) Gate
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
  --profile profiles/pprbSberrating/profile.generated.yaml \
  --application pprbSberrating \
  --scenario-name Licenses --round-trip
```

Подробный контекст всего пайплайна — в `docs/NT_PIPELINE_AI_CONTEXT.md`,
задача для сотрудника — в `docs/NT_PIPELINE_TASK.md`.
