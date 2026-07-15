-- jobHistories <- cmpl.job_history
SELECT
    t.execution_id::text AS "jobHistoriesExecutionId",
    t.id::text AS "jobHistoriesId"
FROM cmpl.job_history t
WHERE t.id IS NOT NULL
LIMIT ${limit}