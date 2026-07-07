---
name: gatling-profile-conventions
description: >-
  Справочный контракт имён и параметров профиля Gatling (var <-> log <-> yaml <->
  properties). Читать перед задачами по весам randomSwitch, profile.yaml и
  ProfileConfig. Используется скиллами gatling-weights-to-profileconfig и
  build-profile-from-simulation.
---

# Контракт профиля Gatling (обязателен к соблюдению)

Единый мостик имён — Java Case-класс. Не выдумывай имена: бери их из кода.

## Три уровня имён одного запроса

| Где | Что | Пример |
|-----|-----|--------|
| Case-класс (`src/test/java/cases/...`) | `ПЕРЕМЕННАЯ = http("ЛОГ_ИМЯ")` | `UC01_POST_Licenses_Summary = http("UC01_POST_/licenses/summary")` |
| Сценарий (`scenarios/...`) | choice-ключ = **имя переменной** | `getWeight(SCN, "UC01_POST_Licenses_Summary", 25)` |
| `simulation.log` | **ЛОГ_ИМЯ** из `http(...)` | `UC01_POST_/licenses/summary` |

Правило: **choice-ключ в `getWeight` = имя переменной Case** (слева от `=` или
после точки в `exec(Case.ПЕРЕМЕННАЯ)`). Ключи `count:` в `profile.yaml` тоже
пишутся по имени переменной — инструменты сами резолвят их в лог-имя через
`case_parser.py`.

## Контракт properties (profile.yaml -> profile.properties -> ProfileConfig.java)

| profile.properties | Источник | Читает в Java |
|--------------------|----------|---------------|
| `weight.<SCN>.<ПЕРЕМЕННАЯ>` | авто из долей `count` | `ProfileConfig.getWeight(SCN, "ПЕРЕМЕННАЯ", def)` → double (%) |
| `inject.<SCN>.users` | `injection.scenarios.<SCN>.users × target%` | `ProfileConfig.getInjectUsers(SCN, def)` |
| `injection.duration` / `injection.rampup` | одноимённые в yaml | `getDuration` / `getRampup` |
| `target_percent` | CLI/yaml | `getTargetPercent` |

- `<SCN>` — имя сценария; в Java это `private static final String SCN = "<...>"`,
  в yaml — ключ `injection.scenarios.<SCN>`. Должны совпадать буква-в-букву.
- Веса **не задаются вручную**: `profile_to_props.py` считает их из долей `count`.
- `target_percent` масштабирует только `users`; соотношение весов не меняется.

## Формулы (для проверки себя)

- Вес: `weight_i = count_i / Σcount(запросов сценария) × 100%` (сумма ≈ 100).
- Обратно (из RPS): `count_i = round(RPS_total × weight_i / Σweight × duration_сек)`.

## Частые ошибки (не допускай)

- choice-ключ ≠ имя переменной Case → вес не применится (упадёт в дефолт).
- Разное написание `<SCN>` в Java и yaml → веса/интенсивность из дефолтов.
- Лог-имя из Case отсутствует в `count` → вес 0 для этого запроса.
- Ручное редактирование весов вместо запуска `profile_to_props.py`.
- Правка закомментированных строк `//` — их трогать нельзя.

## Раскладка profiles/ (по АС, как cases/)

```text
profiles/
├── README.md
├── grafana.example.yaml              # общий Grafana-конфиг
├── efsFinmonWeb/profile.yaml         # АС = имя каталога
├── pprbSberrating/profile.yaml
└── <имя_АС>/profile.yaml             # <имя_АС> = src/test/java/cases/<имя_АС>/
```

| Сущность | Путь |
|----------|------|
| Профиль АС | `profiles/<имя_АС>/profile.yaml` |
| Case той же АС | `src/test/java/cases/<имя_АС>/` |
| Jenkins | `APPLICATION=<имя_АС>`, `PROFILE_YAML` пусто → `profiles/<APPLICATION>/profile.yaml` |

`request_classes` в yaml — пути **относительно** `gatling/gatlingScripts`:
`src/test/java/cases/<имя_АС>` (обычно совпадает с именем каталога profiles/).

Скрипты: `ltAuto/profile_paths.py` (разрешение путей), `verify_profile.py --application <имя_АС>`.
