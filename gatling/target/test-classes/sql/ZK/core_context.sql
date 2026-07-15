-- core: organization + request + task + case (согласованные id)
SELECT
    o.ucp_id::text AS "organizationsUcpId",
    o.inn AS "organizationsInn",
    o.id::text AS "organizationsComplianceOrganizationId",
    o.ucp_id::text AS "allTasksUcpId",
    o.ucp_id::text AS "complianceHistoryClientUcpId",
    ct.id::text AS "complianceTasksId",
    ct.compliance_request_id::text AS "complianceTasksComplianceRequestId",
    cr.id::text AS "complianceRequestsId",
    cr.employee_number AS "complianceRequestsEmployeeNumber",
    cr.ucp_id::text AS "requestHistoriesUcpId",
    cr.organization_id::text AS "requestHistoriesComplianceOrganizationId",
    cc.id::text AS "complianceCasesId",
    cc.organization_id::text AS "complianceCasesComplianceOrganizationId",
    cc.ucp_id::text AS "complianceCasesUcpId",
    cc.cmpl_case_id AS "complianceCasesCmplCaseId"
FROM cmpl.compliance_organization o
LEFT JOIN LATERAL (
    SELECT * FROM cmpl.compliance_request r
    WHERE r.organization_id = o.id
    ORDER BY r.created DESC
    LIMIT 1
) cr ON true
LEFT JOIN LATERAL (
    SELECT * FROM cmpl.compliance_task tk
    WHERE tk.compliance_request_id = cr.id
    ORDER BY tk.created DESC
    LIMIT 1
) ct ON true
LEFT JOIN LATERAL (
    SELECT * FROM cmpl.compliance_case c
    WHERE c.organization_id = o.id
    ORDER BY c.created DESC
    LIMIT 1
) cc ON true
LIMIT ${limit}