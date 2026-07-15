-- finOperationsAttributes <- cmpl.compliance_fin_operation_attr
SELECT
    t.compliance_fin_operation_id::text AS "finOperationsAttributesComplianceFinOperationId",
    t.id::text AS "finOperationsAttributesId"
FROM cmpl.compliance_fin_operation_attr t
WHERE t.id IS NOT NULL
LIMIT ${limit}