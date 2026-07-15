-- reports <- cmpl.report
SELECT
    t.file_ecm_id AS "reportsFileEcmId",
    t.id::text AS "reportsId"
FROM cmpl.report t
WHERE t.id IS NOT NULL
LIMIT ${limit}