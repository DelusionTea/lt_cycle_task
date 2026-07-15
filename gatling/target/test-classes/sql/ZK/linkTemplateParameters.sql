-- linkTemplateParameters <- cmpl.link_template_template_parameter
SELECT
    t.id::text AS "linkTemplateParametersId",
    t.template_id::text AS "linkTemplateParametersTemplateId",
    t.template_parameter_id::text AS "linkTemplateParametersTemplateParameterId"
FROM cmpl.link_template_template_parameter t
WHERE t.id IS NOT NULL
LIMIT ${limit}