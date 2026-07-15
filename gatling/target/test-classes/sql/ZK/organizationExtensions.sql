-- organizationExtensions <- cmpl.compliance_organization_ext
SELECT
    t.id::text AS "organizationExtensionsId",
    ('[' || quote_literal(t.id::text) || ']') AS "organizationExtensionsIds"
FROM cmpl.compliance_organization_ext t
WHERE t.id IS NOT NULL
LIMIT ${limit}