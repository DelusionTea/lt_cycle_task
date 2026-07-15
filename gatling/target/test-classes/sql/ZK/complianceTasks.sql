-- complianceTasks <- cmpl.compliance_task
SELECT
    t.compliance_request_id::text AS "complianceTasksComplianceRequestId",
    t.employee_number AS "complianceTasksEmployeeNumber",
    t.id::text AS "complianceTasksId"
FROM cmpl.compliance_task t
WHERE t.id IS NOT NULL
LIMIT ${limit}