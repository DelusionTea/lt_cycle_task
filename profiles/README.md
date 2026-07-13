# Профили НТ по АС (имена подкаталогов = домены Case-классов)

```text
profiles/
├── README.md
├── grafana.example.yaml          # общий конфиг Grafana (не привязан к одной АС)
├── efsFinmonWeb/
│   └── profile.yaml              # профиль АС efsFinmonWeb
├── pprbSberrating/
│   └── profile.example.yaml      # шаблон для pprbSberrating
├── ZK/
│   ├── README.md                 # один profile.yaml на контроллер
│   ├── AdClientMarkings/profile.yaml
│   └── …/profile.yaml            # 83 контроллера swagger
└── <имя_АС>/                     # имя = каталог в src/test/java/cases/<имя_АС>/
    └── profile.yaml
```

## Правило именования

| Что | Путь |
|-----|------|
| Профиль АС | `profiles/<имя_АС>/profile.yaml` |
| Case-классы той же АС | `src/test/java/cases/<имя_АС>/` |
| Jenkins `PROFILE_YAML` | `profiles/<APPLICATION>/profile.yaml` |
| Jenkins `APPLICATION` | то же `<имя_АС>` |

Пример: `APPLICATION=efsFinmonWeb` → профиль `profiles/efsFinmonWeb/profile.yaml`,
Case → `src/test/java/cases/efsFinmonWeb/`.

## request_classes в profile.yaml

Пути **относительно** `gatling/gatlingScripts` (корень с `pom.xml`):

```yaml
request_classes:
  - src/test/java/cases/efsFinmonWeb
```

Можно указать каталог, один `.java` или glob — см. `case_parser.py`.

## Семантика `count`

- **`count` — запросов в час на 100% профиля** (не «за окно holdFor»).
- Расчёт из кода: `count_i = round(RPS_total × weight_i / Σweight × 3600)` — `sim_to_profile.py`.
- **Веса** в properties считаются только из **долей** count; абсолютные единицы не важны.
- `injection.duration` — длительность hold из throttle (сек), отдельно от count.

## Команды

```bash
cd gatling/gatlingScripts

python3 ltAuto/profile_to_props.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --target_percent 100 --output profile.properties

python3 ltAuto/verify_profile.py --profile profiles/efsFinmonWeb/profile.yaml
```
