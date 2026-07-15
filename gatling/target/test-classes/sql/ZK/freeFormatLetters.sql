-- freeFormatLetters <- cmpl.compliance_free_format_letter
SELECT
    t.organization_id::text AS "freeFormatLettersComplianceOrganizationId",
    t.employee_number AS "freeFormatLettersEmployeeNumber",
    t.folder_ecm_id AS "freeFormatLettersFolderEcmId",
    t.id::text AS "freeFormatLettersId",
    t.ucp_id::text AS "freeFormatLettersUcpId"
FROM cmpl.compliance_free_format_letter t
WHERE t.id IS NOT NULL
LIMIT ${limit}