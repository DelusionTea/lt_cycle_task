---
name: verify-gatling-profile
description: >-
  Единый gate-проверки после правок profile.yaml или рефакторинга весов Gatling.
  Запускает ltAuto/verify_profile.py (PASS/FAIL). Использовать после каждой
  итерации one-file-at-a-time, перед сообщением «готово», после codemod или
  sim_to_profile.
---

# Gate: verify_profile.py

Одна команда вместо нескольких grep/mvn. **Exit code 0 = можно идти дальше.**

Рабочий каталог: `gatling/gatlingScripts/` (в scaffold — корень с `ltAuto/`).

## После codemod одного сценария

```bash
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
  --compile
```

Проверяет: нет числовых `WithWeight`, есть `SCN` и `import ProfileConfig`, `mvn test-compile`.

## После правки profile.yaml

```bash
python3 ltAuto/verify_profile.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --application efsFinmonWeb
```

Проверяет: YAML читается, каталог profiles совпадает с APPLICATION, ключи `count` резолвятся, веса ≈ 100%.

## После sim_to_profile (round-trip)

```bash
python3 ltAuto/sim_to_profile.py ... --output profiles/pprbSberrating/profile.generated.yaml
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/pprbSberrating/LicensesScenario.java \
  --profile profiles/pprbSberrating/profile.generated.yaml \
  --application pprbSberrating \
  --scenario-name Licenses \
  --round-trip --allow-numeric-weights
```
(флаг `--allow-numeric-weights` — только **до** codemod; после codemod не используй)

## Скан домена (перед массовой работой — только список, не правка)

```bash
python3 ltAuto/verify_profile.py --scenarios-dir src/test/java/scenarios/pprbSberrating
```

## Интерпретация вывода

```
[verify] OK: ...
[verify] FAIL: ...
---
RESULT: PASS (N ok, 0 fail, M warn)
```

- **RESULT: PASS** — итерация завершена, можно «далее» (режим one-file-at-a-time).
- **RESULT: FAIL** — исправь **только текущий файл**, повтори verify. Следующий файл запрещён.
- **WARN** — по умолчанию не блокирует; с `--strict` WARN = FAIL.

## Правило для ИИ

Не пиши «готово», пока не выполнил verify и не получил `RESULT: PASS`.
Не заменяй verify ручным «я проверил» без вывода команды.

Связанные скиллы: [one-file-at-a-time.md](../one-file-at-a-time/SKILL.md), [gatling-profile-conventions.md](../gatling-profile-conventions/SKILL.md).
