# Профили НТ домена ZK

По одному профилю на **контроллер swagger** (= один `*Case.java` / `*Scenario.java`):

```text
profiles/ZK/
├── AdClientMarkings/profile.yaml   → cases/ZK/AdClientMarkingsCase.java
├── FinOperations/profile.yaml      → cases/ZK/FinOperationsCase.java
└── …                               (83 контроллера)
```

## Именование

| Сущность | Пример |
|----------|--------|
| Каталог профиля | `profiles/ZK/<ScnName>/` |
| SCN / ключ `injection.scenarios` | `AdClientMarkings` (из `scenario("AdClientMarkings")`) |
| Case-класс | `src/test/java/cases/ZK/AdClientMarkingsCase.java` |
| Ключи `count` | имена переменных UC в Case (`UC01_GET_v1_ad_client_markings`, …) |

## Debug-симуляция (прогон всех UC)

```bash
python3 ltAuto/generate_zk_all_debug.py   # после добавления новых Scenario

cd gatling
mvn test-compile
mvn gatling:test -Dgatling.simulationClass=simulations.ZK.All.ZK_all_debug \
    -DzkBaseUrl=https://host:port
```

Файл: `gatling/src/test/java/simulations/ZK/All/ZK_all_debug.java` — по одному `Debug`-сценарию на каждый контроллер (83 шт.), как `resources/OTT_all_debug.java`.

## Генерация из profile.html (реальная нагрузка)

```bash
python3 ltAuto/apply_profile_html_zk.py
```

Читает `docs/profile.html` (колонка **tpm**, запросов/мин):
- суммирует v1/v2 и `/redirect/...` для одного method+path;
- обновляет `count` в profile.yaml (`round(tpm * 60)`);
- помечает в Case-классах `// Низконагружен не попал в профиль` для Not found.

## Генерация заглушек (равномерные веса)

```bash
python3 ltAuto/generate_zk_profiles.py
python3 ltAuto/generate_zk_profiles.py --rps 10 --duration 3600
```

По умолчанию: `rps=1`, `duration=600`, равномерные веса из `randomSwitch` сценария.

## Проверка одного профиля

```bash
cd gatling
python3 ../ltAuto/verify_profile.py \
  --profile ../profiles/ZK/AdClientMarkings/profile.yaml \
  --scenario-name AdClientMarkings \
  --application ZK
```

## Связка со сценарием

После `weights_codemod` на `*Scenario.java`:

```bash
python3 ltAuto/verify_profile.py \
  --scenario src/test/java/scenarios/ZK/AdClientMarkingsScenario.java \
  --profile profiles/ZK/AdClientMarkings/profile.yaml \
  --scenario-name AdClientMarkings \
  --round-trip
```
