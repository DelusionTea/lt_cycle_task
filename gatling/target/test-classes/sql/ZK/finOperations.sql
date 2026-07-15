-- finOperations <- cmpl.compliance_fin_operation
SELECT
    t.id::text AS "finOperationsId"
FROM cmpl.compliance_fin_operation t
WHERE t.id IS NOT NULL
LIMIT ${limit}