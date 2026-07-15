-- constructorPfParameters <- cmpl.constructor_pf_parameter
SELECT
    t.id::text AS "constructorPfParametersId",
    t.parent_id::text AS "constructorPfParametersParentId"
FROM cmpl.constructor_pf_parameter t
WHERE t.id IS NOT NULL
LIMIT ${limit}