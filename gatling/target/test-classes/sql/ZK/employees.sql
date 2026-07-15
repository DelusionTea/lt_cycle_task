-- employees <- cmpl.compliance_employee
SELECT
    t.employee_number AS "employeesEmployeeNumber"
FROM cmpl.compliance_employee t
WHERE t.id IS NOT NULL
LIMIT ${limit}