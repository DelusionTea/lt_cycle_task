# Требования к коду Gatling

Документ определяет стандарты разработки Gatling-тестов для проекта.

---

## 1. Архитектура

### 1.1 Структура файлов

```
src/test/java/
├── cases/          # HTTP-запросы (HttpRequestActionBuilder)
│   ├── {module}/   # Например: pprbSberrating, pprbComplianceRequests, ZK
│   │   ├── *Case.java
│   │   └── *Cases.java
├── scenarios/      # Сценарии (ScenarioBuilder/ChainBuilder)
│   ├── {module}/
│   │   ├── *Scenario.java
│   │   └── *FlowsScenario.java
├── simulations/    # Симуляции (Simulation)
│   ├── {module}/
│   │   ├── {task}/
│   │   │   ├── *debug.java
│   │   │   └── *stub.java
│   │   └── All/
│   │       ├── *scripts.java
│   │       └── *debug.java
└── feeders/        # Данные для тестов
    ├── {module}/
    │   ├── *Feeder.java
    │   ├── *Headers.java
    │   └── *Methods.java
```

### 1.2 Именование классов

| Тип файла | Шаблон | Пример |
|-----------|--------|--------|
| Case | `{Name}Case.java` | `ArbitrCase.java`, `Auth.java` |
| Scenario | `{Name}Scenario.java` | `SuspiciousFlowsScenario.java` |
| Simulation | `{Task}_{Name}.java` | `OTT_Sus_B2B_Stub.java` |

### 1.3 Пакеты

- **Cases**: `cases.{module}`
- **Scenarios**: `scenarios.{module}`
- **Simulations**: `simulations.{module}.{task}`
- **Feeders**: `feeders.{module}`

---

## 2. Классы Cases (HTTP-запросы)

### 2.1 Структура метода

```java
public class NameCase extends Methods {
    private static final String JSONS_PATH = "JSONs/{module}/Path/";

    public static HttpRequestActionBuilder UC01_POST_Name =
        http("UC01_POST_/endpoint")
            .post("/api/v{version}/endpoint")
            .body(ElFileBody(JSONS_PATH + "UC01.json"))
            .headers(Headers.getCommonHeaders())
            .check(status().is(200));
}
```

### 2.2 Именование запросов

- Формат: `{UC номер}_{HTTP метод}_{Описание}`
- Примеры: `UC01_POST_Arbitrage_Metrics`, `UC02_GET_Case_List`

### 2.3 Правила

- Все Cases **обязаны** расширять `Methods` (для доступа к общим функциям)
- JSON-файлы должны лежать в `src/test/resources/JSONs/{module}/{path}/`
- Указывать тип HTTP-метода (GET, POST, PUT, PATCH, DELETE)
- Обязательно проверка статуса ответа (`.check(status().is(...))`)
- Для POST/PUT/PATCH обязательный body (ElFileBody или StringBody)
- Для GET Optional: проверка наличия данных в ответе

### 2.4 Модульная организация

Каждый модуль (pprbSberrating, pprbComplianceRequests, ZK и т.д.) имеет:
- Отдельную папку в `cases/`, `scenarios/`, `simulations/`, `feeders/`
- Свои `Headers.java` и `Methods.java` (или наследование от общих)

### 2.5 Особенности модуля pprbComplianceRequests

Модуль `pprbComplianceRequests` имеет вложенную структуру `finmon/` с особыми правилами:

**Структура файлов:**
```
src/test/java/cases/pprbComplianceRequests/
├── *Case.java           # Базовые cases (должны расширять Methods)
├── finmon/
│   └── *Cases.java      # Cases для FinMon API (расширяют Methods)
```

**Особенности:**
- Вложенный пакет `cases.pprbComplianceRequests.finmon`
- Используются `Headers.java` и `Methods.java` из `feeders.pprbComplianceRequests`
- **Важно:** Все Cases (включая finmon/) должны расширять `Methods`
- JSONS_PATH должен указывать на правильный путь: `JSONs/pprbComplianceRequests/finmon/{subfolder}/`
- Пример: `ComplianceProfileCases.java` использует `JSONs/pprbComplianceRequests/finmon/ComplianceProfile/`

**Пример корректного Case в finmon:**
```java
package cases.pprbComplianceRequests.finmon;

import feeders.pprbComplianceRequests.Headers;
import feeders.pprbComplianceRequests.Methods;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ComplianceProfileCases extends Methods {
    private static final String JSONS_PATH = "JSONs/pprbComplianceRequests/finmon/ComplianceProfile/";

    public static HttpRequestActionBuilder UC12_GET_SbbolComplianceRequestsComplianceProfile =
        http("UC12_GET_SbbolComplianceRequestsComplianceProfile")
            .get("/finmon-api-gateway/v2/sbbol/compliance-requests/compliance-profile")
            .queryParam("request", "...")  // Для FinMon API используется queryParam вместо body
            .headers(Headers.getCommonHeaders())
            .check(status().is(200));
}
```

**ВАЖНО:** Для Cases в `finmon/`:
- Запросы часто используют `queryParam("request", "...")` с JSON в параметре
- Body может быть отсутствовать (когда данные передаются через queryParam)
- JSONS_PATH все равно должен быть определен даже если body не используется

---

## 3. Классы Scenarios (Цепочки вызовов)

### 3.1 Структура

```java
public class SuspiciousFlowsScenario {
    public static ChainBuilder UC01_POST_SuspiciousFlows =
        group("UC01_POST_SuspiciousFlows_Name").on(
            exec(SuspiciousFlowsCaseOTT.UC01_OTT)
                .exec(SuspiciousFlowsCaseOTT.UC01_POST_SuspiciousFlows_Counteragents_Statistic)
        );

    public static ScenarioBuilder scn = scenario("SuspiciousFlows Name")
        .feed(Feeder.UC01_DB)
        .feed(Methods.rqUidsFeeder)
        .exec(UC01_POST_SuspiciousFlows);
}
```

### 3.2 Правила

- Использовать `group()` для логической группировки шагов
- feeders обязательны для всех scenarios
- Должна быть хотя бы одна scenario с именем в формате `{Module}_{Description}`
- Для debug-версий использовать суффикс `_debug` или `OTT_DEBUG`
- Для production (stub) использовать суффикс `_stub` или без суффикса

### 3.3 OTT vs Stub

| Версия | Назначение | Постфикс |
|--------|------------|----------|
| OTT | Integration Testing (реальные внешние сервисы) | `OTT` |
| Stub | Unit Testing (mocked ответы) | без суффикса или `Stub` |

---

## 4. Классы Simulations (Запуск)

### 4.1 Структура

```java
public class OTT_Sus_B2B_Stub extends Simulation {
    private static final Duration STABILITY_DURATION = Duration.ofHours(24);
    private static final Duration MAX_DURATION = Duration.ofHours(25);

    {
        setUp(
            SuspiciousFlowsScenario.scn_ott
                .injectClosed(
                    rampConcurrentUsers(0).to(150).during(Duration.ofSeconds(1200)),
                    constantConcurrentUsers(150).during(STABILITY_DURATION)
                ).throttle(
                    reachRps(610).in(Duration.ofSeconds(1200)),
                    holdFor(STABILITY_DURATION)
                ).protocols(SberratingPPRB.b2bProtocol_balacovo)
        ).maxDuration(MAX_DURATION);
    }
}
```

### 4.2 Правила

- Имя файла simulations должно содержать Task ID (LTCP-xxx)
- Использовать `injectClosed` для задания нагрузки
- Обязательно указывать `throttle` для контроля RPS
- Указывать протоколы через `.protocols()`
- Ограничивать максимальное время выполнения `.maxDuration()`

---

## 5. Классы Feeders

### 5.1 Headers

```java
public class Headers {
    public static Map<String, String> getCommonHeaders() {
        return Map.of(
            "accept", "application/json",
            "X-Request-Id", Methods.generateUUID(),
            "X-Requestor", "efsSberbusiness",
            "Content-type", "application/json"
        );
    }
}
```

### 5.2 Methods

```java
public class Methods {
    public static final Feeder<String> rqUidsFeeder = 
        csv("feeders/{module}/rqUids.csv").random();

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
```

### 5.3 Правила

- CSV-файлы лежат в `src/test/resources/feeders/{module}/`
- Использовать `.random()`, `.circular()`, `.queue()` для распределения
- `generateUUID()` для динамического генерирования request-id
- Все feeders доступны через статические поля

---

## 6. Проверки и валидации

### 6.1 Обязательные check-ы для каждого запроса

```java
.check(status().is(200))           // Статус ответа
.check(jsonPath("$.field").exists) // Проверка поля в JSON
.check(jsonPath("$.field").saveAs("varName"))  // Сохранение в переменную
.check(headerRegex("header", "pattern").saveAs("varName"))  // Header
```

### 6.2 Ожидаемые статусы

| Статус | Метод |
|--------|-------|
| 200 | GET, POST (успех), PUT, PATCH |
| 201 | POST (создание) |
| 204 | PUT, PATCH, DELETE (без тела) |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |

---

## 7. Безопасность

### 7.1 Разрешено (с оговорками)

**Пароли БД в feeders:**
- Разрешены для локальной/отладочной работы
- Должны быть в отдельных feeders (не в Cases/Scenarios)
- Должны быть помечены комментарием `// DEBUG ONLY`
- Всегда указывать директорию модуля при описании проблемы

### 7.2 Запрещено

- Hardcoded пароли production в debug-файлах
- Hardcoded secret keys (API keys, JWT secret, encryption keys)
- Ссылки на production в debug-файлах
- Logging чувствительных данных (пароли, токены)

### 7.3 Рекомендации

- Использовать переменные окружения для production конфигурации
- Хранить credentials в отдельных feeders с доступом по RBAC
- Использовать `#{variableName}` для подстановки
- Для модулей `efs*` и `pprb*` пароли БД разрешены в feeders с пометкой `// DEBUG ONLY`

---

## 8. Логика и логгирование

### 8.1 Группы (Groups)

```java
group("UC01_Name").on(
    exec(...)
)
```

### 8.2 Условные конструкции

```java
// Цикл
.forever().on(exec(...))

// Случайный выбор
.randomSwitch().on(
    new Choice.WithWeight(33, chain1),
    new Choice.WithWeight(67, chain2)
)

// Условие
.doIf("#{condition}".equals("value"))(
    exec(...)
)
```

---

## 9. Пример полного цикла

### 9.1 Case

```java
package cases.pprbSberrating;

import feeders.pprbSberrating.Headers;
import feeders.pprbSberrating.Methods;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ArbitrCase extends Methods {
    private static final String JSONS_PATH = "JSONs/pprbSberrating/Arbitr/";

    public static HttpRequestActionBuilder UC01_POST_Arbitrage_Metrics =
        http("UC01_POST_/arbitrage/metrics")
            .post("/arbitrage/v2/arbitrage/metrics")
            .body(ElFileBody(JSONS_PATH + "UC01.json"))
            .headers(Headers.getCommonHeaders())
            .check(status().is(200));
}
```

### 9.2 Scenario

```java
package scenarios.pprbSberrating;

import cases.pprbSberrating.ArbitrCase;
import io.gatling.javaapi.core.ChainBuilder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ArbitrScenario {
    public static ChainBuilder UC01_Arbitrage_Metrics =
        group("UC01_POST_Arbitrage_Metrics").on(
            exec(ArbitrCase.UC01_POST_Arbitrage_Metrics)
        );
}
```

### 9.3 Simulation

```java
package simulations.pprbSberrating.LTCP_1135;

import feeders.pprbSberrating.SberratingPPRB;
import scenarios.pprbSberrating.ArbitrScenario;
import io.gatling.javaapi.core.Simulation;
import java.time.Duration;
import static io.gatling.javaapi.core.CoreDsl.*;

public class OTT_Arbitr_Debug extends Simulation {
    private static final Duration STABILITY_DURATION = Duration.ofMinutes(30);

    {
        setUp(
            ArbitrScenario.UC01_Arbitrage_Metrics
                .injectOpen(rampUsers(10).during(Duration.ofSeconds(60)))
                .protocols(SberratingPPRB.riskProtocol_balacovo)
        ).maxDuration(Duration.ofHours(1));
    }
}
```

---

### 12.1 Особенности pprbComplianceRequests

**Примеры дублирования в модуле pprbComplianceRequests:**

**Плохо - использование чужих Headers/Methods:**
```java
// cases/pprbComplianceRequests/ProactiveCommunicationCase.java
import feeders.ZK.Headers;  // ❌ Используются Headers из ZK
import feeders.ZK.Methods;  // ❌ Используются Methods из ZK
```

**Хорошо - свои Headers/Methods:**
```java
// cases/pprbComplianceRequests/ComplianceRequestsCases.java
import feeders.pprbComplianceRequests.Headers;
import feeders.pprbComplianceRequests.Methods;
```

**Плохо - отсутствие JSONS_PATH:**
```java
// cases/pprbComplianceRequests/RefValuesCase.java
// ❌ Отсутствует JSONS_PATH константа
// ❌ Тела запросов закомментированы (body отсутствует)
```

**Хорошо - JSONS_PATH определен:**
```java
// cases/pprbComplianceRequests/finmon/ProactiveCases.java
private static final String JSONS_PATH = "JSONs/pprbComplianceRequests/finmon/";
// Даже если body не используется, JSONS_PATH должен быть определен
```

**Плохо - дублирование queryParam:**
```java
// cases/pprbComplianceRequests/finmon/ProactiveCases.java
// UC01 и UC02 имеют идентичный queryParam (только id отличается)
.queryParam("request","{\n" +
    "  \"id\": \"#{pprb_id}\",\n" +
    "  \"channel\": \"WEB\",\n" +
    "  \"templateNames\": [\n" +
    "    \"COMPLIANCE_DOCUMENTS_RECEIVED\"\n" +
    "  ]\n" +
    "}")
```

**Хорошо - вынести шаблон в base Method:**
```java
// feeders/pprbComplianceRequests/Methods.java
public static String generateProactiveRequest(String id, String... templateNames) {
    return String.format(
        "{\\n\"id\": \\\"%s\\\",\\n\"channel\": \\\"WEB\\\",\\n\"templateNames\\\": %s\\n}",
        id, Arrays.toString(templateNames)
    );
}
```

---

## 11. Дублирование кода

### 11.1 Общие принципы

Код следует оптимизировать, чтобы избежать дублирования:

| Тип дублирования | Рекомендация |
|------------------|--------------|
| Одинаковые хедеры | Вынести в `Headers.getCommonHeaders()` |
| Одинаковые тела запросов | Вынести в общий JSON файл или base Case |
| Одинаковые проверки | Вынести в base Methods с переопределением |
| Одинаковые цепочки вызовов | Вынести в base Scenario |

### 11.2 Примеры дублирования

**Плохо:**
```java
// Cases pprbSberrating/ArbitrCase.java
.headers(Map.of("accept", "application/json", "X-Request-Id", "xxx", ...))

// Cases pprbSberrating/FinanceCase.java
.headers(Map.of("accept", "application/json", "X-Request-Id", "xxx", ...))  // Повтор!
```

**Хорошо:**
```java
// feeders/pprbSberrating/Headers.java
public static Map<String, String> getCommonHeaders() {
    return Map.of(
        "accept", "application/json",
        "X-Request-Id", Methods.generateUUID(),
        "X-Requestor", "efsSberbusiness",
        "Content-type", "application/json"
    );
}

// Cases
.headers(Headers.getCommonHeaders())
```

**Плохо:**
```java
// Cases pprbSberrating/ArbitrCase.java
.body(ElFileBody(JSONS_PATH + "UC01.json"))
.check(status().is(200))
.check(jsonPath("$.id").exists)

// Cases pprbSberrating/FinanceCase.java - то же самое!
.body(ElFileBody(JSONS_PATH + "UC01.json"))
.check(status().is(200))
.check(jsonPath("$.id").exists)
```

**Хорошо:**
```java
// feeders/pprbSberrating/BaseCase.java (или в Methods)
public static ChainBuilder withCommonChecks(HttpRequestActionBuilder request) {
    return exec(request)
        .check(status().is(200))
        .check(jsonPath("$.id").exists);
}
```

---

## 12. Чек-лист перед коммитом

- [ ] Все классы Cases расширяют `Methods`
- [ ] У каждого запроса есть `.check(status().is(...))`
- [ ] JSON-файлы существуют и лежат в правильной папке
- [ ] Имена методов соответствуют формату `UC{NN}_{METHOD}_{Description}`
- [ ] В scenarios есть feeders
- [ ] В simulation указаны `throttle` и `maxDuration`
- [ ] Нет hardcoded credentials
- [ ] Debug-версии имеют суффикс `_debug` или `OTT`
- [ ] Использованы group() для логических блоков

---

*Последнее обновление: 2026-06-03*
