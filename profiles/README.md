# Профили НТ по АС (имена подкаталогов = домены Case-классов)

```text
profiles/
├── README.md
├── grafana.example.yaml          # общий конфиг Grafana (не привязан к одной АС)
├── efsFinmonWeb/
│   └── profile.yaml              # профиль АС efsFinmonWeb
├── pprbSberrating/
│   └── profile.example.yaml      # шаблон для pprbSberrating
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

## Команды

```bash
cd gatling/gatlingScripts

python3 ltAuto/profile_to_props.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --target_percent 100 --output profile.properties

python3 ltAuto/verify_profile.py --profile profiles/efsFinmonWeb/profile.yaml
```
