-- requestHistories <- cmpl.compliance_request_history
SELECT
    t.compliance_case_id::text AS "requestHistoriesComplianceCaseId",
    t.organization_id::text AS "requestHistoriesComplianceOrganizationId",
    t.compliance_request_id::text AS "requestHistoriesComplianceRequestId",
    t.crm_par_row_id AS "requestHistoriesCrmParRowId",
    t.crm_row_id AS "requestHistoriesCrmRowId",
    t.employee_number AS "requestHistoriesEmployeeNumber",
    t.fccm_request_id AS "requestHistoriesFccmRequestId",
    t.id::text AS "requestHistoriesId",
    t.ucp_id::text AS "requestHistoriesUcpId"
FROM cmpl.compliance_request_history t
WHERE t.id IS NOT NULL
LIMIT ${limit}