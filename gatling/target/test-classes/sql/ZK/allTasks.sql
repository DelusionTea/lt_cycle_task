-- allTasks <- cmpl.compliance_task
SELECT
    t.ucp_id::text AS "allTasksUcpId"
FROM cmpl.compliance_task t
WHERE t.id IS NOT NULL
LIMIT ${limit}