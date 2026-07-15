-- accountNumbers <- cmpl.compliance_account_number
SELECT
    t.organization_id::text AS "accountNumbersComplianceOrganizationId",
    t.compliance_request_id::text AS "accountNumbersComplianceRequestId",
    t.id::text AS "accountNumbersId",
    t.ucp_id::text AS "accountNumbersUcpId"
FROM cmpl.compliance_account_number t
WHERE t.id IS NOT NULL
LIMIT ${limit}