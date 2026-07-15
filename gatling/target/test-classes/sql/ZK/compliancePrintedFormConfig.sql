-- compliancePrintedFormConfig <- cmpl.compliance_printed_form_config
SELECT
    t.id::text AS "compliancePrintedFormConfigId"
FROM cmpl.compliance_printed_form_config t
WHERE t.id IS NOT NULL
LIMIT ${limit}