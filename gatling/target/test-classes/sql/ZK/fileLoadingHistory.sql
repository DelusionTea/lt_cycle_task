-- fileLoadingHistory <- cmpl.file_loading_history
SELECT
    t.ceph_id::text AS "fileLoadingHistoryCephId",
    t.compliance_request_id::text AS "fileLoadingHistoryComplianceRequestId",
    t.ecm_folder_id AS "fileLoadingHistoryEcmFolderId",
    t.ecm_id AS "fileLoadingHistoryEcmId",
    t.ffl_id::text AS "fileLoadingHistoryFflId",
    t.id::text AS "fileLoadingHistoryId"
FROM cmpl.file_loading_history t
WHERE t.id IS NOT NULL
LIMIT ${limit}