## FinmonMob (efsFinmonMob)

### Бизнес-поток
1. Пользователь авторизуется в мобильном приложении (loginMob).
2. Пользователь загружает файл (комплаенс-анкета) — `UC01_POST_UPLOAD_V1`.
3. Основной цикл — бесконечная проверка статуса запроса (`UC15_GET_v2_request_state`) с вероятностью ~93%.
4. Периодически пользователь выполняет действия по комплаенс-профилю (виджет, схема бизнеса, партнёры, МТБ, реабилитация, информирование) и работу со списками запросов/отказов.

### Данные и зависимости
- **finmonMobAuthFeeder** — feeder из `feeders.efsSberbusiness.EfsSberbussinesFeeder`. Обязательные поля: `csrftoken`, `sessionId`.
- **finmonMobPPRBFeeder** — feeder из `feeders.efsSberbusiness.EfsSberbussinesFeeder`. Обязательные поля: `pprbId`, `crmId`.
- **uuid** — генерируется через `CommonMethods.generateUUID()`.
- **currentTimestamp** — генерируется через `CommonMethods.generateCurrentTimestamp()`.
- **randIP** — генерируется через `CommonMethods.generateRandomIP()`.
- Тела запросов POST: JSON-файлы в `JSONs/efsFinmonMob/` (UC02.json, UC05.json, UC14.json, UC16.json, UC25.json, UC26.json, UC27.json, UC28.json, UC29.json, UC30.json, UC31.json, UC34.json).
- Файл для загрузки: `JSONs/efsFinmonMob/test.finmonmobxls1.xlsx` (multipart).

### Веса запросов (из Scenario)
- **groupUc15** (93.30%): UC15 (v2/request/state) — доминирующий запрос.
- **groupMedium** (3.00%): UC05 (rejections/list), UC09 (support), UC33 (informing/list v2), UC16 (v2/request/list), UC20 (compliance-profile/widget).
- **groupTail** (2.70%): 13 "хвостовых" UC с равными весами (~7.69% внутри группы): UC03, UC04, UC11, UC18, UC19, UC22, UC24, UC27, UC28, UC29, UC30, UC34, UC35.
- **groupLoli** (1.00%): 13 UC с разными весами: UC01, UC06, UC08, UC10, UC12, UC13, UC17, UC21, UC23, UC25, UC26, UC31, UC32.

### Endpoints (30 активных)
| ID | Метод | Path |
|----|-------|------|
| UC01 | POST | /ufs/mobile/compliance/v1/files/upload |
| UC03 | GET | /ufs/mobile/compliance/v1/request/online/#{pprbId} |
| UC04 | GET | /ufs/mobile/compliance/v1/rejections/#{pprbId} |
| UC05 | POST | /ufs/mobile/compliance/v1/rejections/list |
| UC06 | GET | /ufs/mobile/compliance/v2/request |
| UC08 | GET | /ufs/mobile/compliance/v1/files/params |
| UC09 | GET | /ufs/mobile/compliance/v1/support |
| UC10 | GET | /ufs/mobile/compliance/v1/request/#{pprbId}/print |
| UC11 | GET | /ufs/mobile/compliance/v1/free-format-letter/#{pprbId} |
| UC12 | GET | /ufs/mobile/compliance/v1/free-format-letter/download/{ecmId} |
| UC13 | GET | /ufs/mobile/compliance/v1/informing/#{pprbId} |
| UC15 | GET | /ufs/mobile/compliance/v2/request/state |
| UC16 | POST | /ufs/mobile/compliance/v2/request/list |
| UC17 | GET | /ufs/mobile/compliance/v2/rejections/#{uuid} |
| UC18 | GET | /ufs/mobile/compliance/v1/rejections/startRehabilitation/#{uuid} |
| UC19 | GET | /ufs/mobile/compliance/v1/request/rehabilitation |
| UC20 | GET | /ufs/mobile/compliance/v1/compliance-profile/widget |
| UC21 | GET | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness |
| UC22 | GET | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/partners |
| UC23 | GET | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/mtb |
| UC24 | DELETE | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/detail/alert |
| UC25 | POST | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/detail |
| UC26 | POST | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/partners |
| UC27 | POST | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/mtb |
| UC28 | POST | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/other |
| UC29 | POST | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/aboutCompany |
| UC30 | POST | /ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/contacts |
| UC31 | GET | /ufs/mobile/compliance/v1/proactive/mop2/#{pprbId} |
| UC32 | GET | /ufs/mobile/compliance/v1/proactive/onboarding/#{pprbId} |
| UC33 | POST | /ufs/mobile/compliance/v2/informing/list |
| UC34 | POST | /ufs/mobile/compliance/v2/request/#{uuid}/reply |
| UC35 | POST | /ufs/mobile/compliance/v2/request/#{uuid}/saveAttachments |

Также есть 3 deprecated endpoint'а (UC02, UC07, UC14) — не используются в активном сценарии, но сохранены в Case-классе.

### Ограничения и допущения
- **TODO:** Профиль нагрузки (`profiles/efsFinmonMob/`) не найден. Числовые значения `target_percent`, `rampup`, `duration`, `users`, а также пороги `95pct`, `50pct`, `rps`, `error_count` не указаны — требуют уточнения у владельца АС.
- **TODO:** `profiles/grafana.yaml` не найден. Дашборды для скриншотов не определены.
- **TODO:** `meta.service` (`efsFinmonMob`) необходимо сверить с ключом в `profiles/grafana.yaml` и Jenkins `APPLICATION` после добавления этих файлов.
- **Примечание:** UC12 (GET /download/{ecmId}) содержит хардкод ECM ID в пути — требуется уточнить, должен ли он параметризоваться.