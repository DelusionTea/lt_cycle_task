-- processSettings <- cmpl.process_setting
SELECT
    t.id::text AS "processSettingsId"
FROM cmpl.process_setting t
WHERE t.id IS NOT NULL
LIMIT ${limit}