-- ckr <- cmpl.individual_request
SELECT
    (SELECT ucp_id::text FROM cmpl.compliance_organization LIMIT 1) AS "ckrUcpId"
FROM cmpl.individual_request t
WHERE t.id IS NOT NULL
LIMIT ${limit}