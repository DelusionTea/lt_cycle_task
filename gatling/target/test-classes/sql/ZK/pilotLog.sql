-- pilotLog <- cmpl.pilot_log
SELECT
    t.id::text AS "pilotLogId",
    t.process_setting_id::text AS "pilotLogProcessSettingId"
FROM cmpl.pilot_log t
WHERE t.id IS NOT NULL
LIMIT ${limit}