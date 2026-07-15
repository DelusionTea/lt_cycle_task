-- attachmentsForClient <- cmpl.compliance_attachments_for_client
SELECT
    t.attachment_id::text AS "attachmentsForClientAttachmentId",
    t.ffl_id::text AS "attachmentsForClientFflId",
    t.id::text AS "attachmentsForClientId"
FROM cmpl.compliance_attachments_for_client t
WHERE t.id IS NOT NULL
LIMIT ${limit}