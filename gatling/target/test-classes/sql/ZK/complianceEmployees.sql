-- complianceEmployees <- cmpl.compliance_employee
SELECT
    t.employee_number AS "complianceEmployeesEmployeeNumber",
    t.id::text AS "complianceEmployeesId",
    t.individual_request_id::text AS "complianceEmployeesIndividualRequestId"
FROM cmpl.compliance_employee t
WHERE t.id IS NOT NULL
LIMIT ${limit}