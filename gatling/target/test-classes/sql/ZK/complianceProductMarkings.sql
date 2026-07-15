-- complianceProductMarkings <- cmpl.compliance_product_marking
SELECT
    t.compliance_request_id::text AS "complianceProductMarkingsComplianceRequestId",
    t.crm_row_id AS "complianceProductMarkingsCrmRowId",
    t.id::text AS "complianceProductMarkingsId"
FROM cmpl.compliance_product_marking t
WHERE t.id IS NOT NULL
LIMIT ${limit}