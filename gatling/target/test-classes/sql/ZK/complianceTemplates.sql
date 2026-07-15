-- complianceTemplates <- cmpl.template
SELECT
    t.id::text AS "complianceTemplatesId"
FROM cmpl."template" t
WHERE t.id IS NOT NULL
LIMIT ${limit}