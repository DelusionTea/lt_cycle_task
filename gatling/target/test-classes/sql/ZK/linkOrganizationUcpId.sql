-- linkOrganizationUcpId <- cmpl.link_organization_ucp_id
SELECT
    t.id::text AS "linkOrganizationUcpIdId",
    t.organization_id::text AS "linkOrganizationUcpIdOrganizationId",
    t.ucp_id::text AS "linkOrganizationUcpIdUcpId"
FROM cmpl.link_organization_ucp_id t
WHERE t.id IS NOT NULL
LIMIT ${limit}