# Задача: внедрить и проверить пайплайн ночного НТ (SDD‑first)

**Для кого:** человек без опыта в Gatling и Jenkins.  
**Цель:** параметризовать сценарии под SDD‑спеки и запустить первые Jenkins‑джобы.

**Текущий этап:** все спеки уже созданы, **сценарии ещё не параметризованы** и
**джобы не работают**.

**Важно:** инструкцию можно выполнять буквально, без дополнительных знаний.

> Техкарта для ИИ: `docs/NT_PIPELINE_AI_CONTEXT.md`  
> Скиллы GigaCode: `gigacode/skills/` (см. `gigacode/README.md`)  
> Инструкция агенту: `docs/ai/AI_INSTRUCTION_GIGACODE.md`

---

## 0. Что вы получите в итоге

- **Параметризованные сценарии** (используют ProfileConfig)
- **Валидные спеки** (`spec_validate.py` PASS)
- **Smoke‑прогон** в Jenkins: Start → Analyze

---

## 1. Подготовка (один раз)

### 1.1 Проверьте, что вы в корне репозитория

Путь должен быть:
`/Users/svetlanachernysheva/Documents/LT`

### 1.2 Установите Python‑зависимости

```bash
python3 -m venv .venv
. .venv/bin/activate
pip install pyyaml requests pandas
```

---

## 1.1 Список Jenkins‑джоб для проверки

Проверьте, что в Jenkins доступны и запускаются:
- `Jenkinsfile_NT_Start` (старт прогона)
- `Jenkinsfile_NT_Analyze_Report` (анализ и отчёт)
- `Jenkinsfile_Gatling_Foreground_Analyze_Mail` (foreground запуск, опционально)
- `Jenkinsfile_Grafana_Screenshots_to_Confluence` (скриншоты Grafana, опционально)

---

## 1.2 Какие файлы нужно скопировать

### В репозиторий с Gatling‑скриптами

- `gatling/gatlingScripts/` целиком:
  - `pom.xml`
  - `ltAuto/` (все python‑скрипты)
  - `profiles/` (включая `grafana.yaml`)
  - `src/test/java/...` (cases, scenarios, simulations, config)

### В Jenkins‑репозиторий

- `Jenkinsfile_NT_Start`
- `Jenkinsfile_NT_Analyze_Report`
- `Jenkinsfile_Gatling_Foreground_Analyze_Mail` (если нужен foreground)
- `Jenkinsfile_Grafana_Screenshots_to_Confluence` (если нужны PNG)

---

## 1.3 Подробные этапы прохождения (чек‑лист)

### Этап 1 — Проверка спек
1) Проверить наличие спеки:
   - `SDD/specs/<АС>/<Component>/spec.yaml`
2) Запустить валидатор:
   - `python3 SDD/ltAuto/spec_validate.py --spec SDD/specs/<АС>/<Component>/spec.yaml`
3) Если FAIL — исправить `id/label/count` по Case‑классам.

### Этап 2 — Параметризация сценариев
1) В `Scenario.java` заменить веса на `ProfileConfig.getWeight(...)`.
2) Проверить:
   - `python3 ltAuto/verify_profile.py --scenario src/test/java/scenarios/<АС>/<Scenario>.java --compile`

### Этап 3 — Параметризация симуляций
1) В `Simulation.java` заменить `injectOpen(...)` на `ProfileConfig.getInjectUsers(...)`.
2) Использовать `ltAuto/inject_codemod.py` при необходимости.
3) Проверить компиляцию:
   - `mvn -q -DskipTests test-compile`

### Этап 4 — Генерация properties из спеки
1) Запустить:
   - `python3 SDD/ltAuto/spec_to_props.py --spec SDD/specs/<АС>/<Component>/spec.yaml --output profile.properties`
2) Убедиться, что файл `profile.properties` создан.

### Этап 5 — Jenkins Start
1) Запустить `Jenkinsfile_NT_Start` с параметрами из раздела 6.
2) Проверить в логе:
   - `PROFILE_YAML=(from spec)`
   - `profile.properties` сформирован

### Этап 6 — Jenkins Analyze
1) Запустить `Jenkinsfile_NT_Analyze_Report` с параметрами из раздела 7.
2) Дождаться стадий: Fetch → Parse → Compare → Confluence → Mail.

### Этап 7 — Повторный прогон
1) Запустить второй прогон.
2) Проверить дельту в `compare_runs` и Confluence.

---

## 2. Проверка готовых спек

### 2.1 Убедитесь, что спекы существуют

- `SDD/specs/<АС>/<Component>/spec.yaml`
- `SDD/specs/<АС>/<Component>/spec.md`

### 2.2 Проверьте валидность спеки

```bash
python3 SDD/ltAuto/spec_validate.py \
  --spec SDD/specs/<АС>/<Component>/spec.yaml
```

Если FAIL — правим `id/label/count` по Case‑классам.

---

## 3. Параметризация сценариев и симуляций

### 3.1 Сценарии (Scenario.java)

Нужно заменить все веса на `ProfileConfig.getWeight(...)`.

Проверка:
```bash
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/<АС>/<Scenario>.java \
  --compile
```

### 3.2 Симуляции (Simulation.java)

Нужно подключить:
- `ProfileConfig.getInjectUsers(...)`
- `ProfileConfig.getDuration/getRampup`

Используйте:
- `ltAuto/weights_codemod.py`
- `ltAuto/inject_codemod.py`

Проверка: `mvn -q -DskipTests test-compile`

---

## 4. Генерация properties из спеки

```bash
python3 SDD/ltAuto/spec_to_props.py \
  --spec SDD/specs/<АС>/<Component>/spec.yaml \
  --output profile.properties
```

---

## 5. Smoke‑проверка локально (минимум)

```bash
# Gate спеки
python3 SDD/ltAuto/spec_validate.py \
  --spec SDD/specs/<АС>/<Component>/spec.yaml

# Gate legacy‑профиля (если нужен)
python3 ltAuto/verify_profile.py \
  --profile profiles/<АС>/profile.yaml \
  --application <АС>
```

---

## 6. Jenkins Start (smoke)

В `Jenkinsfile_NT_Start` используйте параметры:

| Параметр | Значение |
|----------|----------|
| `APPLICATION` | `<АС>` |
| `USE_SDD_SPEC` | `true` |
| `SPEC_PATH` | `SDD/specs/<АС>/<Component>/spec.yaml` |
| `PROFILE_YAML` | пусто |
| `TARGET_PERCENT` | `10` |
| `packageSimulation` | ваш debug‑класс |
| `ACTION` | `ЗАПУСТИТЬ ТЕСТ` |

Ожидаемо в логе:  
`PROFILE_YAML=(from spec)`

---

## 7. Jenkins Analyze (smoke)

В `Jenkinsfile_NT_Analyze_Report`:

| Параметр | Значение |
|----------|----------|
| `APPLICATION` | тот же `<АС>` |
| `USE_SDD_SPEC` | `true` |
| `SPEC_PATH` | `SDD/specs/<АС>/<Component>/spec.yaml` |
| `PROFILE_YAML` | пусто |
| `ENABLE_GRAFANA` | false |

---

## 8. Дальнейшая автоматизация (план по шагам)

1) **Gate для всех спек**  
   Прогнать `spec_validate.py` для каждой АС/компонента.

2) **Spec‑first по умолчанию**  
   Всегда использовать `USE_SDD_SPEC=true` в Jenkins.

3) **Генерация `profile.properties`**  
   Работает автоматически через `spec_to_profile.py` → `profile_to_props.py`.

4) **Синхронизация Grafana**  
   `meta.service` в спеках = ключ в `profiles/grafana.yaml`, с дашбордами.

---

## 9. Частые ошибки

| Ошибка | Что делать |
|--------|------------|
| `spec_validate.py FAIL` | исправить `id/label/count` по Case‑классу |
| `PROFILE_YAML` не тот | проверить `USE_SDD_SPEC` и `SPEC_PATH` |
| Jenkins не видит спеку | проверить путь `SDD/specs/.../spec.yaml` |
| Grafana пусто | нет дашбордов в `profiles/grafana.yaml` |

---

## 10. Где читать дальше

- `docs/NT_PIPELINE_AI_CONTEXT.md` — полный технический контекст
- `docs/SDD_PILOT_GUIDE.md` — подробный гайд по пилоту
- `docs/SDD_AUTOMATION_REVIEW.md` — план автоматизации и улучшений
