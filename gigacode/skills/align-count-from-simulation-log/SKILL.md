---
name: align-count-from-simulation-log
description: >-
  Заполнить или исправить count в profiles/<АС>/profile.yaml по simulation.log.
  Через log_labels_to_profile.py и verify_profile.py. Одна строка count за итерацию.
  Не угадывать имена — var↔log через case_parser.
---

# Count в profile.yaml по simulation.log

**Не заполняй count из головы.** Лог-имена ≠ имена переменных Case — путает Flash.

Контракт: [gatling-profile-conventions.md](../gatling-profile-conventions/SKILL.md).  
Режим: [one-file-at-a-time.md](../one-file-at-a-time/SKILL.md) — **один ключ count за итерацию**.

## Алгоритм

1. Запусти `log_labels_to_profile.py` — diff лог ↔ profile.
2. Добавь/исправь **один** ключ в `profiles/<APPLICATION>/profile.yaml`.
3. Gate: `verify_profile.py --profile ... --application <APPLICATION>`.
4. Стоп. Следующий ключ — только после «далее».

## Шаг 1: сопоставление

```bash
cd gatling/gatlingScripts

python3 ltAuto/log_labels_to_profile.py \
  --simulation_log /path/to/simulation.log \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --application efsFinmonWeb
```

С `--json` — для handoff в CI/ответе.

Опционально `--rampup 60` — как в `gatling_parser.py` (отрезать разгон).

**Интерпретация:**
- `missing in profile` — label есть в логе, ключа count нет → добавить (предпочитай `suggested_count_key` = **имя переменной**).
- `orphan in profile` — ключ в yaml, запроса нет в логе → проверить опечатку или устаревший UC (не удаляй пачкой без подтверждения).

## Шаг 2: одна правка yaml

Пример (одна строка за итерацию):
```yaml
count:
  UC10_POST_Mop_1_0: 9144    # suggested_count_key из скрипта, значение — из бизнес-цели или масштаба прогона
```

Правила:
- Ключ = **имя переменной Case**, если Case есть в `request_classes` (скрипт подскажет).
- Если Case недоступен — можно лог-имя, но тогда verify может WARN; лучше сначала добавить `request_classes`.
- **Не правь веса** — их считает `profile_to_props.py` из count.
- Абсолютные count — на **100% профиля** (`target_percent` масштабирует при прогоне).

## Шаг 3: gate

```bash
python3 ltAuto/verify_profile.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --application efsFinmonWeb
```

Дополнительно (после всех ключей SCN):
```bash
python3 ltAuto/profile_to_props.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --target_percent 100 \
  --output /tmp/check.properties
```

**RESULT: PASS** — единственный критерий OK итерации. См. [verify-gatling-profile.md](../verify-gatling-profile/SKILL.md).

## Откуда брать число count

| Источник | Когда |
|----------|-------|
| Бизнес-цель / старый профиль | перенос существующего значения |
| `sim_to_profile.py` | есть веса + RPS/throttle |
| `ok_requests` из log_labels | ориентир с прогона (масштабировать на 100% и длительность) |

Не выводи count арифметикой в чате — используй `sim_to_profile.py` или явное число от пользователя.

## Типичные ошибки Flash

- Ключ `UC10_POST_/mop-1-0` (лог) вместо имени переменной Case.
- Добавить 20 ключей за раз — **запрещено**.
- Править count и Scenario.java в одной итерации — **запрещено**.
- Забыть `--application` при verify.

## Связанные скиллы

- Перед правкой: [extract-before-edit.md](../extract-before-edit/SKILL.md)
- Профиль из RPS: [build-profile-from-simulation.md](../build-profile-from-simulation/SKILL.md)
- Ответ: [session-handoff-template.md](../session-handoff-template/SKILL.md)
