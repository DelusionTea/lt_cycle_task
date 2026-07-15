-- proactiveOnboardings <- cmpl.proactive_onboarding
SELECT
    t.organization_id::text AS "proactiveOnboardingsComplianceOrganizationId",
    t.employee_number AS "proactiveOnboardingsEmployeeNumber",
    t.id::text AS "proactiveOnboardingsId",
    t.ucp_id::text AS "proactiveOnboardingsUcpId"
FROM cmpl.proactive_onboarding t
WHERE t.id IS NOT NULL
LIMIT ${limit}