-- organizationAttributes <- cmpl.compliance_organization_attribute
SELECT
    t.id::text AS "organizationAttributesId",
    ('[' || quote_literal(t.id::text) || ']') AS "organizationAttributesIds",
    t.organization_id::text AS "organizationAttributesOrganizationId"
FROM cmpl.compliance_organization_attribute t
WHERE t.id IS NOT NULL
LIMIT ${limit}