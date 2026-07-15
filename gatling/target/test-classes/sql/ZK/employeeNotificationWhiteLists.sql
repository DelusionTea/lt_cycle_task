-- employeeNotificationWhiteLists <- cmpl.employee_notification_wl
SELECT
    t.employee_number AS "employeeNotificationWhiteListsEmployeeNumber",
    t.id::text AS "employeeNotificationWhiteListsId"
FROM cmpl.employee_notification_wl t
WHERE t.id IS NOT NULL
LIMIT ${limit}