-- featureFlags <- cmpl.feature_flag
SELECT
    t.id::text AS "featureFlagsId"
FROM cmpl.feature_flag t
WHERE t.id IS NOT NULL
LIMIT ${limit}