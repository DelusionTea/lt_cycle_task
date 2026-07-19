# Jenkins: foreground Gatling + parser + mail

Эта джоба запускает Gatling **на генераторе по SSH без nohup**,
ждёт завершения теста, парсит `simulation.log` и отправляет письмо
о результате (PASS/FAIL по `output/test_result.csv`).

## 1. Что настроить в репозитории

1) **Профиль нагрузки** для нужной АС:
- `gatling/gatlingScripts/profiles/<APPLICATION>/profile.yaml`
- В профиле `count` задаётся **в запросах/час на 100%**.

2) **Скрипты Gatling** должны быть в `gatling/gatlingScripts/`:
- `pom.xml`
- `ltAuto/` (включая `gatling_parser.py`, `profile_to_props.py`)
- `src/test/java/...` со сценариями и Case-классами

## 2. Параметры Jenkins (минимум)

| Параметр | Обязателен | Значение |
|---|---|---|
| `ACTION` | да | `ЗАПУСТИТЬ ТЕСТ` |
| `BRANCH` | да | ветка со скриптами |
| `APPLICATION` | да | имя каталога профиля (например `efsFinmonWeb`) |
| `packageSimulation` | да | класс симуляции (`Domain.Class`) или оставить пустым для интерактивного выбора |
| `CREDS` | да | SSH учётка генератора |
| `MAIL_TO` | да | получатели письма |
| `TARGET_PERCENT` | да | масштаб нагрузки, % |
| `TARGET_DURATION` | да | длительность теста, сек |

Опционально:
- `PROFILE_YAML` — путь к profile.yaml, если не стандартный `profiles/<APPLICATION>/profile.yaml`.

## 3. Что должно быть на генераторе

Минимум для отладки на одной АС:

- установлен Maven по пути `/home/pprb_test/apache-maven-3.9.6/`
- директория `/home/pprb_test/gatling_job/` доступна по SSH
- доступ в `/home/pprb_test/.m2/` (репозиторий Maven)

Скрипты и библиотеки **копируются из Jenkins**:
- `gatling/gatlingScripts/` синхронизируется в `gatling_job/gatlingScripts/`
- `gatling/libs/` (если есть) синхронизируется в `gatling_job/libs/`

## 4. Как работает TARGET_PERCENT и TARGET_DURATION

1) Jenkins генерирует `profile.properties`:
   - масштабирует нагрузку (`TARGET_PERCENT`) через `profile_to_props.py`
   - добавляет `injection.duration=TARGET_DURATION`
2) `profile.properties` отправляется на генератор и подхватывается через
   `-DprofileProperties=...`
3) Парсер (`gatling_parser.py`) получает `--target_percent`
   и сравнивает факт с профилем, **учитывая длительность теста**
   (count в профиле задан в запросах/час).

## 5. Минимальная отладка на одной АС

1) Укажите:
   - `APPLICATION` = нужная АС
   - `packageSimulation` = конкретный класс
   - `TARGET_PERCENT` = 10 или 25
   - `TARGET_DURATION` = 300

2) Запустите джобу и проверьте:
   - на генераторе появился `gatling_job/gatlingScripts/results/<run>/simulation.log`
   - на Jenkins сформировался `output/test_result.csv`
   - пришло письмо (PASS/FAIL)

## 6. Куда что прописывать

- **Jenkins job**: Script Path = `Jenkinsfile_Gatling_Foreground_Analyze_Mail`
- **profile.yaml**: `gatling/gatlingScripts/profiles/<APPLICATION>/profile.yaml`
- **target settings**: `TARGET_PERCENT` / `TARGET_DURATION` в параметрах job
- **почта**: `MAIL_TO` в параметрах job

## 7. Типовые проблемы

| Симптом | Что проверить |
|---|---|
| Тест не стартует | `packageSimulation` корректен, класс существует |
| Нет simulation.log | Maven не дошёл до запуска или упал; проверьте лог SSH |
| test_result.csv не создан | парсер не нашёл log или профиль |
| FAIL в письме | проверка профиля/ошибок не прошла (count, SLA) |
