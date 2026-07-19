# Задача: внедрить и проверить пайплайн ночного НТ (SDD‑first)

**Для кого:** человек без опыта в Gatling и Jenkins.  
**Цель:** шаг‑за‑шагом создать SDD‑спеку, проверить её и запустить smoke‑прогон.

**Важно:** инструкцию можно выполнять буквально, без дополнительных знаний.

> Техкарта для ИИ: `docs/NT_PIPELINE_AI_CONTEXT.md`  
> Скиллы GigaCode: `gigacode/skills/` (см. `gigacode/README.md`)  
> Инструкция агенту: `docs/ai/AI_INSTRUCTION_GIGACODE.md`

---

## 0. Что вы получите в итоге

- **SDD‑спека**: `SDD/specs/<АС>/<Component>/spec.yaml`
- **Проверка спеки**: `spec_validate.py` PASS
- **Профиль из спеки**: `SDD/profiles/<АС>/<Component>/profile.spec.yaml`
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

## 2. Создание SDD‑спеки (через скилл)

### 2.1 Запустите скилл GigaCode

В чате с агентом GigaCode отправьте:

```text
Прочитай docs/ai/AI_INSTRUCTION_GIGACODE.md.
Используй skill: sdd-spec-writer
АС: <АС>
Компонент: <Component>
```

### 2.2 Что агент должен сделать

Агент **должен** создать два файла:
- `SDD/specs/<АС>/<Component>/spec.yaml`
- `SDD/specs/<АС>/<Component>/spec.md`

**Если агент не создал оба файла — значит, работа не завершена.**

### 2.3 Что заполнить руками (если агент попросит)

В `spec.md` допишите:
- бизнес‑поток (2–4 пункта)
- зависимости по данным/feeder
- допущения

Ничего не придумывайте — только факты.

---

## 3. Проверка валидности спеки

Выполните:

```bash
python3 SDD/ltAuto/spec_validate.py \
  --spec SDD/specs/<АС>/<Component>/spec.yaml
```

### Если команда упала

Сверьте:
- `endpoints.id` = имя переменной в Case‑классе
- `endpoints.label` = строка в `http("...")`
- ключи `profile.count` есть в Case‑классе

Исправьте и запустите повторно.

---

## 4. Генерация профиля из спеки

```bash
python3 SDD/ltAuto/spec_to_profile.py \
  --spec SDD/specs/<АС>/<Component>/spec.yaml
```

Файл появится здесь:
`SDD/profiles/<АС>/<Component>/profile.spec.yaml`

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
`PROFILE_YAML=../SDD/profiles/.../profile.spec.yaml`

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
