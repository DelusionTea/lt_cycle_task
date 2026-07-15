-- infrastructures <- cmpl.infrastructure
SELECT
    t.id::text AS "infrastructuresId",
    ('[' || quote_literal(t.id::text) || ']') AS "infrastructuresIds"
FROM cmpl.infrastructure t
WHERE t.id IS NOT NULL
LIMIT ${limit}