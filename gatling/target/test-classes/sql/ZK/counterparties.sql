-- counterparties <- cmpl.counterparty
SELECT
    t.id::text AS "counterpartiesId",
    ('[' || quote_literal(t.id::text) || ']') AS "counterpartiesIds",
    t.inn AS "counterpartiesInn",
    t.organization_id::text AS "counterpartiesOrganizationId",
    t.ucp_id::text AS "counterpartiesUcpId"
FROM cmpl.counterparty t
WHERE t.id IS NOT NULL
LIMIT ${limit}