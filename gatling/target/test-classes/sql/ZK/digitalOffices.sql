-- digitalOffices <- cmpl.digital_office
SELECT
    t.organization_id::text AS "digitalOfficesComplianceOrganizationId",
    t.digital_id::text AS "digitalOfficesDigitalId",
    t.id::text AS "digitalOfficesId",
    t.ucp_id::text AS "digitalOfficesUcpId"
FROM cmpl.digital_office t
WHERE t.id IS NOT NULL
LIMIT ${limit}