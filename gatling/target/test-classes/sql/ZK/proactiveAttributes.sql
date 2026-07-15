-- proactiveAttributes <- cmpl.proactive_attributes
SELECT
    t.id::text AS "proactiveAttributesId",
    t.proactive_onboarding_id::text AS "proactiveAttributesProactiveOnboardingId"
FROM cmpl.proactive_attributes t
WHERE t.id IS NOT NULL
LIMIT ${limit}