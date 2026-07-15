-- constructor <- cmpl.constructor_pf
SELECT
    t.id::text AS "constructorId"
FROM cmpl.constructor_pf t
WHERE t.id IS NOT NULL
LIMIT ${limit}