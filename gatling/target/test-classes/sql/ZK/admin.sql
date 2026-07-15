-- admin <- cmpl.compliance_organization
SELECT
    (SELECT id::text FROM cmpl.compliance_organization LIMIT 1) AS "adminComplianceOrganizationId",
    (SELECT employee_number FROM cmpl.compliance_employee LIMIT 1) AS "adminEmployeeNumber",
    t.id::text AS "adminId",
    t.inn AS "adminInn",
    t.ucp_id::text AS "adminUcpId"
FROM cmpl.compliance_organization t
WHERE t.id IS NOT NULL
LIMIT ${limit}