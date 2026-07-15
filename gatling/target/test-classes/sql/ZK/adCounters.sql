-- adCounters <- cmpl.ad_counter
SELECT
    t.employee_number AS "adCountersEmployeeNumber",
    t.id::text AS "adCountersId"
FROM cmpl.ad_counter t
WHERE t.id IS NOT NULL
LIMIT ${limit}