-- complianceTemplateParameters <- cmpl.template_parameter
SELECT
    t.id::text AS "complianceTemplateParametersId"
FROM cmpl.template_parameter t
WHERE t.id IS NOT NULL
LIMIT ${limit}