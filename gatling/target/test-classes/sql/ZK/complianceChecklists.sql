-- complianceChecklists <- cmpl.compliance_checklist
SELECT
    t.id::text AS "complianceChecklistsId"
FROM cmpl.compliance_checklist t
WHERE t.id IS NOT NULL
LIMIT ${limit}