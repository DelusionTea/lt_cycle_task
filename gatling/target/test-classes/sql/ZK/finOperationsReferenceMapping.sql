-- finOperationsReferenceMapping <- cmpl.reference_mapping_fin_operation
SELECT
    t.id::text AS "finOperationsReferenceMappingId"
FROM cmpl.reference_mapping_fin_operation t
WHERE t.id IS NOT NULL
LIMIT ${limit}