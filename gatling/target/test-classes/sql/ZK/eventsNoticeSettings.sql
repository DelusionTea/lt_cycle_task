-- eventsNoticeSettings <- cmpl.adm_notice
SELECT
    t.id::text AS "eventsNoticeSettingsId"
FROM cmpl.adm_notice t
WHERE t.id IS NOT NULL
LIMIT ${limit}