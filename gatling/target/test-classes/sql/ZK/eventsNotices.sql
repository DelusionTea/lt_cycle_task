-- eventsNotices <- cmpl.adm_notice
SELECT
    t.id::text AS "eventsNoticesId",
    t.template_id::text AS "eventsNoticesTemplateId"
FROM cmpl.adm_notice t
WHERE t.id IS NOT NULL
LIMIT ${limit}