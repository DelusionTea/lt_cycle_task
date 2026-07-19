## Spec-driven НТ: ручные спецификации

Этот каталог хранит **ручные спеки** для систем без Swagger/OpenAPI.
Спека является источником правды для:

- списка эндпоинтов и их `label` (имя запроса в Gatling);
- SLA и нагрузочных целей `count`;
- связи `scenario → request_classes`;
- генерации `profile.yaml` и валидации соответствия `spec → Gatling`.

### Структура каталогов

```text
specs/
└── <АС>/
    └── <Компонент>/
        ├── spec.yaml     # машинно-читаемая спек
        └── spec.md       # пояснения: бизнес поток, ограничения
```

### Формат spec.yaml (минимально обязательный)

```yaml
meta:
  service: ZK
  component: ComplianceRequests
  scenario: ComplianceRequests
  source: manual
  swagger: false
  description: "НТ для контроллера compliance-requests"

request_classes:
  - src/test/java/cases/ZK/ComplianceRequestsCase.java

profile:
  target_percent: 100
  rampup: 0
  thresholds:
    pct95: 1000
    pct50: 500
    rps: 24
    error_count: 100
  count:
    UC01_GET_v3_compliance_requests: 82248

injection:
  duration: 600
  rampup: 60
  scenarios:
    ComplianceRequests:
      users: 1457
      request_classes:
        - src/test/java/cases/ZK/ComplianceRequestsCase.java

endpoints:
  - id: UC01_GET_v3_compliance_requests
    label: UC01_GET_/api/v3/compliance-requests
    method: GET
    path: /api/v3/compliance-requests
    query:
      filter: JSONs/ZK/shared/GetListCmplRequestsRqFilter_filter.json
    checks:
      status: 200
```

### Соглашения

- `label` должен совпадать с именем запроса в Gatling `http("...")`.
- `id` соответствует имени переменной в Case-классе (ключ для `count`).
- Для систем без БД: указывать источники данных в `spec.md` и использовать
  stub-feeders в сценариях (в проекте уже есть примеры с `ZKDbConfig.useJdbc()`).
