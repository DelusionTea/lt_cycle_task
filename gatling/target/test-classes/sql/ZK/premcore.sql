-- premcore: массив ucp_id
SELECT
    ('[' || quote_literal(ucp_id::text) || ']') AS "premcoreUcpIds"
FROM cmpl.compliance_organization
WHERE ucp_id IS NOT NULL
LIMIT ${limit}