-- rehabilitationBufferChecklists <- cmpl.rehabilitation_buffer_checklist
SELECT
    t.ck_id AS "rehabilitationBufferChecklistsCkId",
    t.id::text AS "rehabilitationBufferChecklistsId",
    t.rehabilitation_buffer_request_id::text AS "rehabilitationBufferChecklistsRehabilitationBufferRequestId"
FROM cmpl.rehabilitation_buffer_checklist t
WHERE t.id IS NOT NULL
LIMIT ${limit}