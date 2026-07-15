-- complianceRecommendations <- cmpl.compliance_recommendations_cib
SELECT
    t.compliance_case_id::text AS "complianceRecommendationsComplianceCaseId",
    t.organization_id::text AS "complianceRecommendationsComplianceOrganizationId",
    t.compliance_request_id::text AS "complianceRecommendationsComplianceRequestId",
    t.crm_row_id AS "complianceRecommendationsCrmRowId",
    t.id::text AS "complianceRecommendationsId",
    t.ucp_id::text AS "complianceRecommendationsUcpId"
FROM cmpl.compliance_recommendations_cib t
WHERE t.id IS NOT NULL
LIMIT ${limit}