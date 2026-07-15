-- ecm <- cmpl.compliance_request
SELECT
    t.fccm_request_id AS "ecmFccmRequestId",
    t.ucp_id::text AS "ecmUcpId"
FROM cmpl.compliance_request t
WHERE t.id IS NOT NULL
LIMIT ${limit}