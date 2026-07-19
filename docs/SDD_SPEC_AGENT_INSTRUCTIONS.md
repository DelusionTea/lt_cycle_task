## Инструкция для слабого ИИ-агента: подготовка SDD-спеки

Цель: подготовить корректную `spec.yaml` и `spec.md` в формате проекта так,
чтобы ничего не выдумывать и не ломать соответствие с текущими Gatling Case-классами.

### 0) Запрещено
- Нельзя выдумывать эндпоинты, параметры, SLA или данные.
- Нельзя менять Case-классы и сценарии.
- Нельзя придумывать новые поля в `spec.yaml`.
- Нельзя добавлять несуществующие файлы или пути.

### 1) Что нужно получить на вход
Попроси у человека или системы эти данные (если нет — остановись):
- Путь к Case-классу: `src/test/java/cases/<АС>/<Component>Case.java`
- Полный список методов из Case-класса:
  - имена переменных (например, `UC01_GET_v3_compliance_requests`)
  - значения `http("...")` (label из Gatling)
  - метод и path из `.get("/...")/.post("/...")/...`
- Данные о профиле (если уже есть `profiles/.../profile.yaml`):
  - блок `count`, `sla_per_label`, `injection`
  - `target_percent`, `rampup`, `95pct`, `50pct`, `rps`, `error_count`
- Grafana‑связка (обязательно):
  - `profiles/grafana.yaml` с ключом АС (`applications.<АС>`)
  - список дашбордов для скриншотов по этой АС

Если чего-то нет — не заполняй и пометь как `TODO` в `spec.md`.

### 2) Где искать
1) Case-классы:
   - `gatling/src/test/java/cases/<АС>/<Component>Case.java`
2) Сценарий:
   - `gatling/src/test/java/scenarios/<АС>/<Component>Scenario.java`
3) Профиль:
   - `profiles/<АС>/<Component>/profile.yaml`

### 3) Формат spec.yaml (НЕ менять структуру)

```yaml
meta:
  service: <АС>
  component: <Component>
  scenario: <ScenarioName>
  source: manual
  swagger: false
  description: "<кратко>"

request_classes:
  - src/test/java/cases/<АС>/<Component>Case.java

data_sources:
  feeders:
    - name: <feeder_name>
      source: <класс/метод>
      required_fields: [<поле1>, <поле2>]
      fallback: stub

profile:
  target_percent: <из profile.yaml>
  rampup: <из profile.yaml>
  thresholds:
    pct95: <из profile.yaml>
    pct50: <из profile.yaml>
    rps: <из profile.yaml>
    error_count: <из profile.yaml>
  count:
    <var_name>: <число>
  sla_per_label: {}

injection:
  duration: <из profile.yaml>
  rampup: <из profile.yaml>
  scenarios:
    <ScenarioName>:
      users: <из profile.yaml>
      request_classes:
        - src/test/java/cases/<АС>/<Component>Case.java

endpoints:
  - id: <var_name>
    label: <http("...")>
    method: <GET|POST|PUT|DELETE|PATCH>
    path: <"/api/...">
    query:
      <key>: <value or file>
    body: <file path if used>
    checks:
      status: 200
```

### 4) Как заполнять поля

**id**:
- это имя переменной в Case-классе: `public static HttpRequestActionBuilder <id> =`

**label**:
- это строка из `http("...")`
- должна совпадать на 100%

**method и path**:
- берутся из `.get("/...")`, `.post("/...")` и т.д.

**query/body**:
- если в Case-классе есть `.queryParam(...)` или `.body(ElFileBody("..."))`
- укажи ровно те же пути к файлам

**profile.count**:
- копировать ключи и значения из `profiles/<АС>/<Component>/profile.yaml`

### 5) spec.md (описание)

Шаблон:

```
## <Component> (<АС>)

### Бизнес-поток
- 2–4 пункта, что делает компонент.

### Данные и зависимости
- источники данных (feeder), обязательные поля.

### Ограничения и допущения
- что неизвестно или требует ручной проверки (TODO).
```

### 6) Проверка результата

1) Сохранить файлы:
   - `SDD/specs/<АС>/<Component>/spec.yaml`
   - `SDD/specs/<АС>/<Component>/spec.md`

2) Запустить валидацию:
   - `python3 SDD/ltAuto/spec_validate.py --spec SDD/specs/<АС>/<Component>/spec.yaml`

3) Если валидатор падает:
   - сверить `id` и `label` с Case-классом
   - сверить ключи `count` с Case-классом

4) Проверить Grafana‑связку:
   - `meta.service` должен совпадать с ключом АС в `profiles/grafana.yaml`
   - для этой АС должны быть перечислены дашборды
   - если нет — `TODO` в `spec.md` и стоп

### 7) Критическое правило

Если данных нет — **не заполняй**. Укажи `TODO` в `spec.md` и остановись.
