-- adQueues <- cmpl.ad_queue
SELECT
    t.employee_number AS "adQueuesEmployeeNumber",
    t.id::text AS "adQueuesId"
FROM cmpl.ad_queue t
WHERE t.id IS NOT NULL
LIMIT ${limit}