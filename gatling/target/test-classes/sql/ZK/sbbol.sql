-- sbbol <- cmpl.compliance_request
SELECT
    t.employee_number AS "sbbolEmployeeNumber",
    t.id::text AS "sbbolId",
    t.ucp_id::text AS "sbbolUcpId"
FROM cmpl.compliance_request t
WHERE t.id IS NOT NULL
LIMIT ${limit}