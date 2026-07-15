-- infrastructure: entity/field из справочника (stub-like distinct)
SELECT DISTINCT
    'compliance_request'::text AS "infrastructureEntityName",
    'status'::text AS "infrastructureFieldName"
FROM cmpl.compliance_request
WHERE id IS NOT NULL
LIMIT ${limit}