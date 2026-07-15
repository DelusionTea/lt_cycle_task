-- organizationBusinessSchemeAttachments <- cmpl.organization_business_scheme_attachment
SELECT
    t.ecm_id AS "organizationBusinessSchemeAttachmentsEcmId",
    t.id::text AS "organizationBusinessSchemeAttachmentsId"
FROM cmpl.organization_business_scheme_attachment t
WHERE t.id IS NOT NULL
LIMIT ${limit}