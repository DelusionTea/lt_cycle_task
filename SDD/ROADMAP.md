## Roadmap перехода к spec-driven НТ

### Этап 1. Пилот (1 сервис)
- Выбрать контроллер без Swagger (например, `ZK/ComplianceRequests`).
- Описать спеку (`SDD/specs/.../spec.yaml` + `spec.md`).
- Проверить соответствие спеки и Case-классов:
  - `python3 SDD/ltAuto/spec_validate.py --spec SDD/specs/ZK/ComplianceRequests/spec.yaml`
- Сгенерировать профиль и сравнить с текущим:
  - `python3 SDD/ltAuto/spec_to_profile.py --spec SDD/specs/ZK/ComplianceRequests/spec.yaml`

### Этап 2. Валидация как gate
- Включить `spec_validate.py` в CI (Jenkins pipeline) для выбранной АС.
- При расхождении `spec → Gatling labels` останавливать прогон.

### Этап 3. Масштабирование по доменам
- Переносить контроллеры в `SDD/specs/<АС>/<Component>/` по критичности.
- Для каждой АС хранить список покрытых компонентов в `SDD/specs/<АС>/`.

### Этап 4. Унификация SLA и профилей
- Перенести SLA/`count` в спеки для всех ключевых сценариев.
- Перейти на генерацию профилей из спеки как default.

### Этап 5. Улучшение данных
- Для систем без БД фиксировать источники данных в `spec.md`.
- Для систем с БД — формализовать feeders и критерии актуальности данных.
