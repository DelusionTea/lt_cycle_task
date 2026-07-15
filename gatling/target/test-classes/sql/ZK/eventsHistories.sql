-- eventsHistories <- cmpl.event_history
SELECT
    t.compliance_case_id::text AS "eventsHistoriesComplianceCaseId",
    t.compliance_fin_operation_id::text AS "eventsHistoriesComplianceFinOperationId",
    t.organization_id::text AS "eventsHistoriesComplianceOrganizationId",
    t.compliance_request_id::text AS "eventsHistoriesComplianceRequestId",
    t.id::text AS "eventsHistoriesId",
    t.individual_request_id::text AS "eventsHistoriesIndividualRequestId",
    t.ucp_id::text AS "eventsHistoriesUcpId"
FROM cmpl.event_history t
WHERE t.id IS NOT NULL
LIMIT ${limit}