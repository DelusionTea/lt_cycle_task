-- individualRequests <- cmpl.individual_request
SELECT
    t.cib_ucp_sfl_id::text AS "individualRequestsCibUcpSflId",
    t.ck_case_id AS "individualRequestsCkCaseId",
    t.ck_request_id AS "individualRequestsCkRequestId",
    t.employee_number AS "individualRequestsEmployeeNumber",
    t.id::text AS "individualRequestsId",
    t.parent_id::text AS "individualRequestsParentId",
    (SELECT ucp_id::text FROM cmpl.compliance_organization LIMIT 1) AS "individualRequestsUcpId",
    t.ucp_sfl_id AS "individualRequestsUcpSflId",
    t.uvsk_id::text AS "individualRequestsUvskId",
    t.uvsk_public_id AS "individualRequestsUvskPublicId"
FROM cmpl.individual_request t
WHERE t.id IS NOT NULL
LIMIT ${limit}