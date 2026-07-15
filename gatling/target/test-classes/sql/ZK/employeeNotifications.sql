-- employeeNotifications <- cmpl.employee_notification
SELECT
    t.employee_number AS "employeeNotificationsEmployeeNumber",
    t.id::text AS "employeeNotificationsId",
    t.report_id::text AS "employeeNotificationsReportId"
FROM cmpl.employee_notification t
WHERE t.id IS NOT NULL
LIMIT ${limit}