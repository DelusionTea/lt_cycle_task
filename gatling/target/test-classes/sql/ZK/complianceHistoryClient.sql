-- complianceHistoryClient <- cmpl.compliance_request_history
SELECT
    '1' AS "complianceHistoryClientPageNum",
    t.ucp_id::text AS "complianceHistoryClientUcpId"
FROM cmpl.compliance_request_history t
WHERE t.id IS NOT NULL
LIMIT ${limit}