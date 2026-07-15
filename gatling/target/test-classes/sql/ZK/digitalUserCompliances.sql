-- digitalUserCompliances <- cmpl.digital_user_compliance
SELECT
    t.organization_id::text AS "digitalUserCompliancesComplianceOrganizationId",
    t.digital_office_id::text AS "digitalUserCompliancesDigitalOfficeId",
    t.id::text AS "digitalUserCompliancesId",
    t.ucp_id::text AS "digitalUserCompliancesUcpId"
FROM cmpl.digital_user_compliance t
WHERE t.id IS NOT NULL
LIMIT ${limit}