-- complianceDeputyEmployees <- cmpl.deputy_employee
SELECT
    (SELECT employee_number FROM cmpl.compliance_employee LIMIT 1) AS "complianceDeputyEmployeesEmployeeNumber",
    t.id::text AS "complianceDeputyEmployeesId"
FROM cmpl.deputy_employee t
WHERE t.id IS NOT NULL
LIMIT ${limit}