## Spec-driven НТ: пошаговая инструкция (пилот и дальнейшее развитие)

Ниже инструкция для пользователя, который хочет выполнить **пилот** и дальше
масштабировать spec-driven подход. Все шаги выполняются локально из корня
репозитория: `/Users/svetlanachernysheva/Documents/LT`.

### 0) Проверка окружения

1) Проверьте версии Python и pip:
   - `python3 --version`
   - `python3 -m pip --version`

2) Проверьте, что доступна библиотека `PyYAML`:
   - `python3 -c "import yaml; print(yaml.__version__)"`

3) Если `PyYAML` не установлен:
   - `python3 -m pip install PyYAML`
   - если нужна установка в venv:
     - `python3 -m venv .venv`
     - `source .venv/bin/activate`
     - `python3 -m pip install PyYAML`

4) Если установленный pip не может писать в системные каталоги:
   - `python3 -m pip install --user PyYAML`

### 1) Пилот: подготовка спеки

1) Создайте папку для пилотного контроллера:
   - пример уже есть: `SDD/specs/ZK/ComplianceRequests/`

2) Заполните `spec.yaml`:
   - `meta` (service, component, scenario, source, swagger)
   - `request_classes` (пути к Case-классам)
   - `profile` (count, SLA, thresholds)
   - `injection` (duration, rampup, scenarios)
   - `endpoints` (id, label, method, path, checks)

3) Заполните `spec.md`:
   - бизнес-поток
   - зависимости по данным/feeder
   - допущения (например, stub при отсутствии БД)

### 2) Пилот: валидация спеки

1) Запустите проверку соответствия:
   - `python3 SDD/ltAuto/spec_validate.py --spec SDD/specs/ZK/ComplianceRequests/spec.yaml`

2) Если проверка упала:
   - проверьте, что `endpoints.id` совпадают с именами переменных в Case-классах
   - проверьте, что `endpoints.label` совпадают с `http("...")` в Case-классах
   - проверьте, что ключи `profile.count` есть в Case-классах

### 3) Пилот: генерация профиля

1) Сгенерируйте профиль из спеки:
   - `python3 SDD/ltAuto/spec_to_profile.py --spec SDD/specs/ZK/ComplianceRequests/spec.yaml`

2) Проверить результат:
   - файл будет в `SDD/profiles/<service>/<component>/profile.spec.yaml`
   - сверить с текущими профилями в `profiles/` (по count и SLA)

### 4) Пилот: проверка сценариев

1) Проверить соответствие профиля и Case-классов:
   - `python3 ltAuto/verify_profile.py --profile profiles/ZK/ComplianceRequests/profile.yaml`

2) Если `verify_profile.py` недоступен:
   - проверьте наличие каталога `ltAuto/` в корне проекта
   - убедитесь, что запускаете команду из `/Users/svetlanachernysheva/Documents/LT`

### 5) Что делать, если чего-то не хватает

- Нет `PyYAML`:
  - `python3 -m pip install PyYAML` (или `--user` / venv)
- Нет `python3`:
  - установить Python 3.x через системный пакетный менеджер
- Ошибка `ModuleNotFoundError: case_parser`:
  - запускайте команды из корня репозитория
  - проверьте, что есть `ltAuto/case_parser.py`
- Ошибка доступа к БД в feeders:
  - используйте stub-режим в сценариях (пример в `ZKFeeder.defaultFeeder`)
  - не блокируйте пилот, если БД недоступна

### 6) Дальнейшее масштабирование

1) Создавайте спеки для каждого компонента:
   - `SDD/specs/<АС>/<Component>/spec.yaml` + `spec.md`

2) Встраивайте в CI:
   - добавить шаг запуска `spec_validate.py` в Jenkins
   - падать при расхождении `spec → labels`

3) Перевести профили на генерацию из спеки:
   - проверять `profile.spec.yaml` против `profiles/.../profile.yaml`
   - затем заменить ручной профиль на сгенерированный

### 7) Что и как проверить локально в `docs/`

1) Убедиться, что документация читается:
   - открыть `docs/SDD_PILOT_GUIDE.md`
   - проверить корректность путей в командах

2) Если нужен обзор остальной документации:
   - `docs/LT_TO_SRE_ROADMAP.md` (общий roadmap)
   - `docs/NT_PIPELINE_TASK.md` (pipeline задачи)
   - `docs/GATLING_FOREGROUND_JENKINS.md` (Jenkins запуск)
