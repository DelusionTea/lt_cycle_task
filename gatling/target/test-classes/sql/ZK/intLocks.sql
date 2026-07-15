-- intLocks <- cmpl.int_lock
SELECT
    t.client_id AS "intLocksClientId",
    t.lock_key AS "intLocksLockKey",
    t.region AS "intLocksRegion"
FROM cmpl.int_lock t
WHERE t.id IS NOT NULL
LIMIT ${limit}