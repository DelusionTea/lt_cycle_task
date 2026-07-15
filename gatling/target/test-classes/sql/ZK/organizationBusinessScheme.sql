-- organizationBusinessScheme <- cmpl.organization_business_scheme
SELECT
    t.id::text AS "organizationBusinessSchemeId"
FROM cmpl.organization_business_scheme t
WHERE t.id IS NOT NULL
LIMIT ${limit}