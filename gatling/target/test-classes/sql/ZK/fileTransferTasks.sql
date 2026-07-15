-- fileTransferTasks <- cmpl.file_transfer_task
SELECT
    t.compliance_request_id::text AS "fileTransferTasksComplianceRequestId",
    t.id::text AS "fileTransferTasksId"
FROM cmpl.file_transfer_task t
WHERE t.id IS NOT NULL
LIMIT ${limit}