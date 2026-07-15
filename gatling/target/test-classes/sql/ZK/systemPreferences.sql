-- systemPreferences <- cmpl.sys_pref
SELECT
    t.id::text AS "systemPreferencesId"
FROM cmpl.sys_pref t
WHERE t.id IS NOT NULL
LIMIT ${limit}