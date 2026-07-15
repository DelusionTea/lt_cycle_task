-- blackLists <- cmpl.blacklist
SELECT
    t.id::text AS "blackListsId"
FROM cmpl.blacklist t
WHERE t.id IS NOT NULL
LIMIT ${limit}