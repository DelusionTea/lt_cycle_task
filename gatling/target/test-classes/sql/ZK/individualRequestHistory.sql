-- individualRequestHistory <- cmpl.individual_request_history
SELECT
    t.cib_ucp_sfl_id::text AS "individualRequestHistoryCibUcpSflId",
    t.ck_case_id AS "individualRequestHistoryCkCaseId",
    t.ck_request_id AS "individualRequestHistoryCkRequestId",
    t.employee_number AS "individualRequestHistoryEmployeeNumber",
    t.id::text AS "individualRequestHistoryId",
    t.individual_request_id::text AS "individualRequestHistoryIndividualRequestId",
    t.ucp_sfl_id AS "individualRequestHistoryUcpSflId",
    t.uvsk_id::text AS "individualRequestHistoryUvskId",
    t.uvsk_public_id AS "individualRequestHistoryUvskPublicId"
FROM cmpl.individual_request_history t
WHERE t.id IS NOT NULL
LIMIT ${limit}