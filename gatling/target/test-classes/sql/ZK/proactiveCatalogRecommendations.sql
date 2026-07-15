-- proactiveCatalogRecommendations <- cmpl.proactive_catalog_recommendations
SELECT
    t.id::text AS "proactiveCatalogRecommendationsId"
FROM cmpl.proactive_catalog_recommendations t
WHERE t.id IS NOT NULL
LIMIT ${limit}