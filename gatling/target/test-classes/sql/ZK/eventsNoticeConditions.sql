-- eventsNoticeConditions <- cmpl.adm_condition
SELECT
    t.id::text AS "eventsNoticeConditionsId"
FROM cmpl.adm_condition t
WHERE t.id IS NOT NULL
LIMIT ${limit}