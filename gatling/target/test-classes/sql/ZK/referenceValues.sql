-- referenceValues <- cmpl.lst_of_val
SELECT
    t.id::text AS "referenceValuesId"
FROM cmpl.lst_of_val t
WHERE t.id IS NOT NULL
LIMIT ${limit}