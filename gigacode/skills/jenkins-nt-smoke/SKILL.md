---
name: jenkins-nt-smoke
description: >-
  Smoke-чек-лист ночного цикла НТ: Jenkinsfile_NT_Start и Jenkinsfile_NT_Analyze_Report.
  Проверка путей GATLING_DIR, APPLICATION, PROFILE_YAML без переписывания pipeline.
---

# Jenkins NT: smoke-чек-лист

ИИ **не переписывает** Jenkinsfile без явной задачи. Только проверяет соответствие путям и параметрам.

Файлы (корень репо или `gatlingJenkins/`):
- `Jenkinsfile_NT_Start` — старт прогона (nohup на генераторе)
- `Jenkinsfile_NT_Analyze_Report` — парс, Grafana, history, Confluence, mail

## Environment (обе джобы)

| Переменная | Ожидание |
|------------|----------|
| `GATLING_DIR` | `gatling/gatlingScripts` |
| `PROFILE_YAML` (runtime) | пустой param → `profiles/${APPLICATION}/profile.yaml` |

## Start-джоба: параметры

| Param | Назначение |
|-------|------------|
| `APPLICATION` | имя АС = `profiles/<APPLICATION>/`, `cases/<APPLICATION>/` |
| `PROFILE_YAML` | override пути к yaml; **пусто = авто** |
| `TARGET_PERCENT` | масштаб users (веса не меняются) |
| `ACTION` / simulation class | класс Gatling Simulation |
| `CREDS` | SSH генератор |

**Smoke:** в логе stage должно быть:
```text
PROFILE_YAML=profiles/<APPLICATION>/profile.yaml
python3 ${GATLING_DIR}/ltAuto/profile_to_props.py --profile '${GATLING_DIR}/${PROFILE_YAML}' ...
```

## Analyze-джоба: параметры

| Param | Назначение |
|-------|------------|
| `APPLICATION` | история Bitbucket, отчёт |
| `PROFILE_YAML` | SLA/count для gatling_parser |
| `ERROR_THRESHOLD` | compare_runs «перестал отрабатывать» |
| `ENABLE_GRAFANA` | render_export |
| `GRAFANA_CONFIG` | `profiles/grafana.yaml` |
| `GRAFANA_APPLICATION` | ключ в grafana.yaml |
| `CONF_*` | Confluence |
| `MAIL_TO` | письмо |

**Smoke-порядок стадий:**
1. Fetch completed run → `simulation.log` из tarball
2. `gatling_parser.py` с `${GATLING_DIR}/${PROFILE_YAML}`
3. (opt) `render_export.py`
4. Bitbucket history → `compare_runs.py`
5. `summary_to_confluence.py`
6. mail

## Чек-лист для ИИ (одна итерация = один Jenkinsfile)

```
- [ ] GATLING_DIR указывает на каталог с pom.xml, ltAuto/, profiles/
- [ ] PROFILE_YAML резолвится в profiles/<APPLICATION>/profile.yaml
- [ ] Пути ltAuto/*.py через ${GATLING_DIR}/ltAuto/
- [ ] Нет profiles/profile.yaml (устаревший flat-путь)
- [ ] APPLICATION согласован между Start и Analyze
```

## Локальная симуляция без Jenkins

```bash
cd gatling/gatlingScripts
python3 ltAuto/profile_to_props.py \
  --profile profiles/efsFinmonWeb/profile.yaml \
  --target_percent 10 \
  --output /tmp/profile.properties
```

Затем [parse-gatling-run.md](../parse-gatling-run/SKILL.md) на сохранённом log.

## Запреты

- Не менять `credentials()` и remote host без задачи.
- Не править обе джобы в одной итерации (one-file-at-a-time).
- Не коммитить токены Grafana/Confluence.

## Связанные скиллы

- [parse-gatling-run.md](../parse-gatling-run/SKILL.md)
- [grafana-render-for-run.md](../grafana-render-for-run/SKILL.md)
- [confluence-summary-publish.md](../confluence-summary-publish/SKILL.md)
- [onboard-new-as-domain.md](../onboard-new-as-domain/SKILL.md)
