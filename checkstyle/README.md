# Checkstyle для Gatling Java

Конфигурация стиля по эталонам `LicensesCase`, `LicensesScenario`, `OTT_all_debug`.

## Что проверяется

| Область | Правила |
|---------|---------|
| Файл | UTF-8, LF в конце, **без табов** |
| Отступы | 4 пробела (базовый offset) |
| Скобки | `{` на той же строке, что `class` / `setUp` |
| Строка | до 150 символов (длинные `http()` / import игнорируются) |
| Импорты | `cases` → `config` → `feeders` → `scenarios` → `io.gatling` → static |
| Имена | `JSONS_PATH`, `SCN`; поля `UC01_POST_*`, `scn_ott_debug` |
| Комментарии | `// текст` с пробелом после `//` |

## Что **не** проверяется (до завершения автоматизации)

- `ProfileConfig.getWeight` / `getInjectUsers`
- `extends Methods`, `.check(status())`
- архитектура Cases/Scenarios — см. `review_gates.py`, скилл `gatling-reviewer`

## Запуск

Из каталога с `pom.xml` (production: `gatling/gatlingScripts`, scaffold: `resources/`):

```bash
mvn -q checkstyle:check
```

Только тестовые исходники (`src/test/java`).

## Постепенное ужесточение

1. Сейчас: `failOnViolation=false` в pom — отчёт без падения сборки.
2. После чистки legacy: `failOnViolation=true`.
3. Удаляйте записи из `suppressions.xml` по файлам.

## Локальное отключение участка

```java
// CHECKSTYLE:OFF Indentation
    .post("/path")
        .body(...)
// CHECKSTYLE:ON
```
