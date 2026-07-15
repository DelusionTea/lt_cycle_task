-- adClientMarkings <- cmpl.ad_client_marking
SELECT
    t.organization_id::text AS "adClientMarkingsComplianceOrganizationId",
    t.id::text AS "adClientMarkingsId",
    ('[' || quote_literal(t.id::text) || ']') AS "adClientMarkingsIds"
FROM cmpl.ad_client_marking t
WHERE t.id IS NOT NULL
LIMIT ${limit}