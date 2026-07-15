-- complianceRequests <- cmpl.compliance_request
SELECT
    t.employee_number AS "complianceRequestsEmployeeNumber",
    t.id::text AS "complianceRequestsId"
FROM cmpl.compliance_request t
WHERE t.id IS NOT NULL
LIMIT ${limit}