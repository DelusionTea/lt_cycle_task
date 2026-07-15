-- catalogRecommendationsCib <- cmpl.compliance_catalog_recommendations_cib
SELECT
    t.id::text AS "catalogRecommendationsCibId"
FROM cmpl.compliance_catalog_recommendations_cib t
WHERE t.id IS NOT NULL
LIMIT ${limit}