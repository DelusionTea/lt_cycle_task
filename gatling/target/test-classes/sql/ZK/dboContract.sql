-- dboContract <- cmpl.dbo_contract
SELECT
    t.organization_id::text AS "dboContractComplianceOrganizationId",
    t.digital_office_id::text AS "dboContractDigitalOfficeId",
    t.id::text AS "dboContractId",
    t.ucp_id::text AS "dboContractUcpId"
FROM cmpl.dbo_contract t
WHERE t.id IS NOT NULL
LIMIT ${limit}