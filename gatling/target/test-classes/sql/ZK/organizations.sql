-- organizations <- cmpl.compliance_organization
SELECT
    t.inn AS "organizationsInn",
    t.ucp_id::text AS "organizationsUcpId"
FROM cmpl.compliance_organization t
WHERE t.id IS NOT NULL
LIMIT ${limit}