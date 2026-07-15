-- complianceCases <- cmpl.compliance_case
SELECT
    t.cmpl_case_id AS "complianceCasesCmplCaseId",
    t.organization_id::text AS "complianceCasesComplianceOrganizationId",
    t.id::text AS "complianceCasesId",
    t.ucp_id::text AS "complianceCasesUcpId"
FROM cmpl.compliance_case t
WHERE t.id IS NOT NULL
LIMIT ${limit}