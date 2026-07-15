-- proactiveTasks <- cmpl.proactive_task
SELECT
    t.organization_id::text AS "proactiveTasksComplianceOrganizationId",
    t.employee_number AS "proactiveTasksEmployeeNumber",
    t.id::text AS "proactiveTasksId",
    t.proactive_onboarding_id::text AS "proactiveTasksProactiveOnboardingId",
    t.ucp_id::text AS "proactiveTasksUcpId"
FROM cmpl.proactive_task t
WHERE t.id IS NOT NULL
LIMIT ${limit}