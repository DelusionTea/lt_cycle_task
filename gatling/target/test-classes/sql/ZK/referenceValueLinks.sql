-- referenceValueLinks <- cmpl.link_lst_of_val
SELECT
    t.id::text AS "referenceValueLinksId"
FROM cmpl.link_lst_of_val t
WHERE t.id IS NOT NULL
LIMIT ${limit}