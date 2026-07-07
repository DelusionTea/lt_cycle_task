---
name: session-handoff-template
description: >-
  Жёсткий формат ответа ИИ после каждой итерации (handoff): файл, скилл, команды,
  gate, следующий шаг. Для слабых моделей — доказательство работы, не «я всё сделал».
---

# Шаблон handoff после итерации

После **каждой** итерации (one-file-at-a-time) ответ **только** в этом формате.
Пропуск секций = итерация не засчитывается.

## Шаблон (копируй и заполняй)

```text
## Итерация N

**Скилл:** <имя из task-router-gigacode>
**Файл:** <единственный изменённый путь или «без изменений, только анализ»>

### Команды (фактический вывод)
```bash
<команды, которые реально выполнялись>
```
<краткий вывод: exit code, ключевые строки, RESULT: PASS/FAIL>

### Что сделано
- <1–3 пункта, без воды>

### Gate
- Команда: `<verify / mvn / log_labels ...>`
- Результат: **PASS** | **FAIL**
- При FAIL: <что чинить в ЭТОМ же файле>

### Следующий кандидат (НЕ ТРОГАТЬ до «далее»)
- <путь или «ожидаю команду пользователя»>

### Блокер (если есть)
- <нет | описание>
```

## Правила

1. **Gate обязателен** — без `RESULT: PASS` (или явного OK compile) статус итерации = **BLOCKED**.
2. **Не объявляй DONE** для нескольких файлов в одном ответе.
3. **Не пиши «проверил логически»** вместо запуска скрипта.
4. Если только анализ (`dump_scenario_meta`, `log_labels_to_profile`) — gate = exit code скрипта + краткая интерпретация.
5. В конце: **«Жду „далее“ для следующей итерации.»** — одной строкой.

## Пример (успех)

```text
## Итерация 2

**Скилл:** gatling-weights-to-profileconfig
**Файл:** src/test/java/scenarios/pprbSberrating/RatingScenario.java

### Команды
python3 ltAuto/weights_codemod.py .../RatingScenario.java
python3 ltAuto/verify_profile.py --scenario .../RatingScenario.java --compile
→ RESULT: PASS (12 ok, 0 fail, 0 warn)

### Gate
- **PASS**

### Следующий кандидат (НЕ ТРОГАТЬ)
- scenarios/pprbSberrating/RiskScenario.java

Жду «далее».
```

## Пример (FAIL)

```text
## Итерация 3

**Скилл:** align-count-from-simulation-log
**Файл:** profiles/efsFinmonWeb/profile.yaml

### Gate
- verify_profile.py → RESULT: FAIL — ключ count "UC99_..." не резолвится

### Блокер
- Исправить только ключ UC99 в этом yaml, повторить verify. Следующий файл не открывать.

Жду «далее» после PASS или указания пользователя.
```

Связанные скиллы: [one-file-at-a-time.md](../one-file-at-a-time/SKILL.md), [task-router-gigacode.md](../task-router-gigacode/SKILL.md).
