-- eventsNoticeConditionsGroups <- cmpl.adm_group_condition
SELECT
    t.id::text AS "eventsNoticeConditionsGroupsId"
FROM cmpl.adm_group_condition t
WHERE t.id IS NOT NULL
LIMIT ${limit}