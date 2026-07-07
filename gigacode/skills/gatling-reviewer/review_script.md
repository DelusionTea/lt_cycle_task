# Инструкция по ревью кода Gatling

> **Жёсткие gates (обязательно перед чек-листом):**  
> `python3 gigacode/skills/gatling-reviewer/scripts/review_gates.py --module <АС> --strict`  
> См. [SKILL.md](SKILL.md) — полный workflow для коллег с GigaCode.

## Вводная

При получении запроса на ревью `skill: gatling-reviewer`, выполните проверку по следующему чек-листу.

## Архитектура проекта

Проект состоит из нескольких **модулей (АС)** - систем, каждая со своей директорией:

| Префикс | Название АС | Примеры директорий |
|---------|-------------|-------------------|
| `pprb*` | PPRB системы | `pprbSberrating`, `pprbComplianceRequests`, `ZK` |
| `efs*` | EFS системы | `efsSberbusinessAuth`, `efsFinmonMob`, `efsSberratingWeb` |

Каждая АС имеет структуру:
```
src/test/java/{cases,scenarios,simulations,feeders}/{module}/
src/test/resources/JSONs/{module}/{path}/
src/test/resources/feeders/{module}/
```

## Этапы ревью

### 1. Проверка структуры

#### 1.1 Файловая структура
```
✓ src/test/java/cases/{module}/{Name}Case.java
✓ src/test/java/scenarios/{module}/{Name}Scenario.java
✓ src/test/java/simulations/{module}/{task}/{Name}.java
✓ src/test/java/feeders/{module}/{Name}.java
```

**Правила:**
- Каждый модуль (pprbSberrating, pprbComplianceRequests, ZK) имеет свою папку
- Файлы лежат в соответствующих директориях (cases, scenarios, simulations, feeders)
- Имена файлов соответствуют утвержденному шаблону

#### 1.2 Пакеты
```java
// Проверить, что пакеты совпадают с расположением:
package cases.pprbSberrating;     // → cases/pprbSberrating/...
package scenarios.pprbSberrating; // → scenarios/pprbSberrating/...
package simulations.pprbSberrating.LTCP_1135; // → simulations/pprbSberrating/LTCP_1135/...
package feeders.pprbSberrating;   // → feeders/pprbSberrating/...
```

---

### 2. Проверка Cases (HttpRequestActionBuilder)

#### 2.1 Особенности модуля pprbComplianceRequests

**Вложенная структура `finmon/`:**
```
src/test/java/cases/pprbComplianceRequests/
├── *Case.java              # Базовые cases (Cases.java - без 's')
└── finmon/
    └── *Cases.java         # Cases для FinMon API (Cases.java - с 's')
```

**Особенности проверки:**

1. **Импорты Headers/Methods:**
   - ❌ `import feeders.ZK.Headers;` - использование чужих Headers
   - ❌ `import feeders.ZK.Methods;` - использование чужих Methods
   - ✅ `import feeders.pprbComplianceRequests.Headers;`
   - ✅ `import feeders.pprbComplianceRequests.Methods;`

2. **JSONS_PATH:**
   - ❌ Отсутствует (как в RefValuesCase.java)
   - ❌ Неверный путь: `"JSONs/pprbComplianceRequests/"` вместо `"JSONs/pprbComplianceRequests/finmon/ComplianceProfile/"`
   - ✅ `"JSONs/pprbComplianceRequests/finmon/{subfolder}/"` - должен указывать на подпапку

3. **Типы запросов:**
   - FinMon API часто использует `queryParam("request", "...")` вместо body
   - Даже если body отсутствует, JSONS_PATH должен быть определен
   - Пример:
   ```java
   .queryParam("request", "{\n  \"id\": \"#{pprb_id}\",\n  ...")
   ```

4. **Проверка тел запросов:**
   - Если JSONS_PATH определен, проверить существование JSON-файлов в директории
   - Закомментированные тела запросов - это WARNING, а не ERROR (но требует пояснения)

#### 2.2 Обязательные элементы
- [ ] Расширяет `Methods`: `public class NameCase extends Methods`
- [ ] Константа `JSONS_PATH` с правильным путем
- [ ] Методы имеют публичный модификатор доступа
- [ ] Имя метода в формате `UC{NN}_{METHOD}_{Description}`

#### 2.2 HTTP-запросы
- [ ] Указан тип метода (`.get()`, `.post()`, `.put()`, `.patch()`, `.delete()`)
- [ ] Путь к эндпоинту верный
- [ ] `.body(ElFileBody(JSONS_PATH + "..."))` для POST/PUT/PATCH
- [ ] `.headers(Headers.getCommonHeaders())`
- [ ] `.check(status().is(...))` с правильным статусом

#### 2.3 Проверки ответов
- [ ] Статус 200 для GET/POST/PUT/PATCH
- [ ] Статус 201 для POST (создание)
- [ ] Статус 204 для PUT/PATCH/DELETE
- [ ] Дополнительные check-ы (jsonPath, headerRegex) при необходимости

#### 2.4 Пример проблем:
```
❌ public class NameCase (нет extends Methods)
❌ .check(status().is(200)) отсутствует
❌ .body() для POST без ElFileBody
❌ wrong JSONS_PATH: "JSONs/pprbSberrating/Arbitr/" → должен совпадать с файловой структурой
```

---

### 3. Проверка Scenarios (ScenarioBuilder/ChainBuilder)

#### 3.1 Обязательные элементы
- [ ] Использует `group()` для логических блоков
- [ ] Использует `exec()` для вызова Case-методов
- [ ] Есть `feed()` с нужными feeders
- [ ] Scenario-методы имеют правильный модификатор (public static)

#### 3.2 Структура
- [ ] `UC{NN}_Name` для цепочек (ChainBuilder)
- [ ] `scn_{env}` или `scn` для scenario (ScenarioBuilder)
- [ ] OTT-версии имеют суффикс `OTT`
- [ ] Debug-версии имеют суффикс `_debug` или `OTT`

#### 3.3 Пример проблем:
```
❌ Отсутствует .feed() с данными
❌ group() не используется для логических блоков
❌ UC01 и UC02 перепутаны местами
❌ Нет различия между OTT и Stub версиями
```

---

### 4. Проверка Simulations (Simulation)

#### 4.1 Обязательные элементы
- [ ] Расширяет `Simulation`
- [ ] Имя файла содержит Task ID (LTCP-xxx)
- [ ] Использует `setUp()` с scenario
- [ ] Указан `injectClosed()` или `injectOpen()`
- [ ] Указан `throttle()` с RPS и holdFor
- [ ] Указаны протоколы через `.protocols()`
- [ ] Указано `maxDuration()`

#### 4.2 Пример проблемы:
```
❌ Отсутствует .throttle()
❌ Нет .maxDuration()
❌ Протоколы не указаны
❌ injectClosed/injectOpen не используется
```

---

### 5. Проверка Feeders

#### 5.1 Headers
- [ ] Метод `getCommonHeaders()` возвращает Map
- [ ] Включает `accept`, `X-Request-Id`, `X-Requestor`, `Content-type`
- [ ] `X-Request-Id` генерируется через `Methods.generateUUID()`

#### 5.2 Methods
- [ ] Feeders объявлены как `public static final Feeder`
- [ ] CSV-файлы существуют в `src/test/resources/feeders/{module}/`
- [ ] Используется `.random()`, `.circular()`, `.queue()`

#### 5.3 Пример проблем:
```
❌ Headers не возвращает Map
❌ Отсутствует generateUUID()
❌ CSV-файл не найден
```

---

### 6. Проверка JSON-файлов

#### 6.1 Расположение
```
src/test/resources/JSONs/{module}/{path}/UC{NN}.json
```

#### 6.2 Проверки
- [ ] JSON-файлы существуют
- [ ] Имена JSON-файлов соответствуют UC-номерам
- [ ] JSON валиден (можно проверить через jsonlint)
- [ ] Пути в коде совпадают с реальным расположением

---

### 7. Безопасность

#### 7.1 Правила для паролей БД

**Разрешено (для модулей `pprb*` и `efs*`):**
```java
// feeders/pprbComplianceRequests/ComplianceRequestsFeeder.java
public static final String DB_URL = "jdbc:postgresql://localhost:5432/dbname";
public static final String DB_USER = "test_user";
public static final String DB_PASSWORD = "test_password"; // DEBUG ONLY
```

**Требования:**
- Пароли должны быть в feeders (не в Cases/Scenarios)
- Обязательна пометка `// DEBUG ONLY` в комментарии
- Используются только для локальной/отладочной работы

**Запрещено:**
- Пароли production в debug-файлах
- Секретные ключи (API keys, JWT secret, encryption keys)
- Пароли в Cases и Scenarios (только в feeders)

#### 7.2 Запрещено
- [ ] Hardcoded логины/пароли в коде
- [ ] Hardcoded API-токены
- [ ] Ссылки на production в debug-файлах
- [ ] Logging чувствительных данных (пароли, токены)

#### 7.3 Проверка
```
❌ "password": "123456" в коде (без пометки DEBUG ONLY)
❌ .check(jsonPath("$.password").saveAs("pass"))
❌ url = "https://production.api.com" в debug simulation
```

#### 7.4 Формат описания проблем с директориями

При нахождении проблемы **обязательно** указывайте:
1. Директорию модуля (АС): `pprbSberrating`, `pprbComplianceRequests`, `ZK`, `efsSberbusinessAuth` и т.д.
2. Полный путь к файлу
3. Строку с проблемой

**Примеры:**
```
❌ [pprbComplianceRequests:Feeders/ComplianceRequestsFeeder.java:15] Hardcoded пароль БД без пометки DEBUG ONLY
❌ [pprbSberrating:Cases/ArbitrCase.java:25] Отсутствует .check(status().is(200))
❌ [efsSberbusinessAuth:Scenarios/LoginWeb.java:10] group имеет неправильное имя
```

---

### 7.5 Особенности pprbComplianceRequests

**Проверка импортов Headers/Methods:**
```
❌ [pprbComplianceRequests:Cases/ProactiveCommunicationCase.java:5] Используются Headers из ZK (feeders.ZK.Headers)
❌ [pprbComplianceRequests:Cases/ProactiveCommunicationCase.java:6] Используются Methods из ZK (feeders.ZK.Methods)
```

**Проверка JSONS_PATH:**
```
❌ [pprbComplianceRequests:Cases/RefValuesCase.java:7] Отсутствует JSONS_PATH константа
❌ [pprbComplianceRequests:Cases/ProactiveCommunicationCase.java:11] Неверный JSONS_PATH: "JSONs/pprbComplianceRequests/" вместо "JSONs/pprbComplianceRequests/"
```

**Проверка тел запросов (закомментированные):**
```
⚠️ [pprbComplianceRequests:Cases/RefValuesCase.java:31] Тело запроса закомментировано без пояснения
```

**Проверка FinMon API:**
```
⚠️ [pprbComplianceRequests:Cases/finmon/ProactiveCases.java] UC01 и UC02 имеют идентичный queryParam (дублирование)
```

---

### 8. Best Practices

#### 8.1 Именование
- [ ] UC-номера уникальны в рамках одного Case-файла
- [ ] Имена scenarios отражают их назначение
- [ ] group-имена соответствуют UC-номеру

#### 8.2 Дублирование
- [ ] Одинаковые цепочки не дублируются
- [ ] Общие хедеры вынесены в feeders
- [ ] Общие методы вынесены в Methods.java

#### 8.3 Производительность
- [ ] Throttle соответствует требованиям
- [ ] Конкурентность (concurrent users) адекватна нагрузке
- [ ] Duration не превышает необходимого

---

## 9. Проверка дублирования кода

### 9.1 Общие принципы

Проверь на наличие дублирующегося кода, который можно оптимизировать:

**Одинаковые хедеры в разных Cases:**
```java
// Если во многих Cases встречается:
.headers(Map.of("accept", "application/json", "X-Request-Id", "..."))

// → Вынести в Headers.getCommonHeaders()
```

**Одинаковые тела запросов:**
- Проверь, нет ли одинаковых JSON тел в разных Cases
- Если тело повторяется - вынести в общий JSON файл

**Одинаковые check-ы:**
```java
// Если во многих Cases:
.check(status().is(200))
.check(jsonPath("$.id").exists)

// → Вынести в base Methods
```

**Одинаковые цепочки вызовов в Scenarios:**
- Проверь на повторяющиеся group-ы
- Если одна и та же последовательность exec повторяется - вынести в отдельный метод

### 9.2 Особенности pprbComplianceRequests

**Проверка дублирования для pprbComplianceRequests:**

```
⚠️ [pprbComplianceRequests:Cases/ProactiveCommunicationCase.java:5] Используются Headers из ZK вместо собственных
⚠️ [pprbComplianceRequests:Cases/ProactiveCommunicationCase.java:6] Используются Methods из ZK вместо собственных

⚠️ [pprbComplianceRequests:Cases/finmon/ProactiveCases.java:UC01] и [pprbComplianceRequests:Cases/finmon/ProactiveCases.java:UC02] имеют идентичный queryParam
⚠️ [pprbComplianceRequests:Cases/finmon/ProactiveCases.java] UC01 и UC02 различаются только в id - можно вынести в base Method

⚠️ [pprbComplianceRequests:Scenarios/finmon.java] Дублируются имена сценариев (scn_ott_debug_1 и scn_ott_debug_2)
```

### 9.3 Примеры проблем

```
⚠️ [pprbSberrating:Cases/ArbitrCase.java:20]Headers дублируют Headers.getCommonHeaders()
⚠️ [pprbSberrating:Cases/FinanceCase.java:18] Headers дублируют Headers.getCommonHeaders()
⚠️ [pprbSberrating:Cases/ArbitrCase.java:25] и [pprbSberrating:Cases/FinanceCase.java:22] имеют одинаковые check-ы
⚠️ [pprbComplianceRequests:Scenarios/finmon.java] и [pprbComplianceRequests:Scenarios/FileLoadingScenario.java] имеют повторяющиеся group-ы
```

---

## Формат отчета о ревью

```
## Ревью кода Gatling

### ✅ Пройдено
- Архитектура: структура файлов и пакетов корректна
- Cases: все проверки статуса присутствуют
- Scenarios: feeders и group-ы используются правильно
- Simulations: throttle и maxDuration на месте
- Feeders: Headers и Methods реализованы верно
- JSON-файлы: все файлы существуют
- Безопасность: нет hardcoded credentials
- Нет дублирования кода

### ❌ Проблемы
1. [Cases/ArbitrCase.java:15] Отсутствует .check(status().is(200))
2. [Scenarios/SuspiciousFlowsScenario.java:23] UC01 и UC02 перепутаны местами
3. [Simulations/OTT_123_Debug.java:12] Отсутствует .throttle()

### ⚠️ Рекомендации
- Рассмотреть вынесение общих check-ов в base class
- Добавить проверку временных меток в ответе
- Уточнить требования к RPS в simulation
- Исправить дублирование кода ( Headers, check-ы, цепочки)

### Статус: [К ИСПРАВЛЕНИЮ / ОК]
```

---

## Сроки ревью

- **Критичные проблемы**: немедленно
- **Нормальные проблемы**: 1 рабочий день
- **Рекомендации**: в течение спринта

---

*Последнее обновление: 2026-06-03*
