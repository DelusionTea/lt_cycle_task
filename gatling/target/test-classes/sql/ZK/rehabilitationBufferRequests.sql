-- rehabilitationBufferRequests <- cmpl.rehabilitation_buffer_request
SELECT
    t.compliance_case_id::text AS "rehabilitationBufferRequestsComplianceCaseId",
    t.compliance_fin_operation_id::text AS "rehabilitationBufferRequestsComplianceFinOperationId",
    t.compliance_request_id::text AS "rehabilitationBufferRequestsComplianceRequestId",
    t.fccm_request_id AS "rehabilitationBufferRequestsFccmRequestId",
    t.id::text AS "rehabilitationBufferRequestsId",
    t.organization_id::text AS "rehabilitationBufferRequestsOrganizationId",
    t.ucp_id::text AS "rehabilitationBufferRequestsUcpId"
FROM cmpl.rehabilitation_buffer_request t
WHERE t.id IS NOT NULL
LIMIT ${limit}