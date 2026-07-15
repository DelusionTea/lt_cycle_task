-- linkIndividualOrganization <- cmpl.link_individual_organization
SELECT
    t.id::text AS "linkIndividualOrganizationId",
    t.organization_id::text AS "linkIndividualOrganizationOrganizationId",
    t.ucp_sfl_id AS "linkIndividualOrganizationUcpSflId"
FROM cmpl.link_individual_organization t
WHERE t.id IS NOT NULL
LIMIT ${limit}