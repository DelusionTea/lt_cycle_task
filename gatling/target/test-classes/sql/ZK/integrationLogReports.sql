-- integrationLogReports <- cmpl.integration_logging
SELECT
    t.id::text AS "integrationLogReportsId",
    t.message_id::text AS "integrationLogReportsMessageId"
FROM cmpl.integration_logging t
WHERE t.id IS NOT NULL
LIMIT ${limit}