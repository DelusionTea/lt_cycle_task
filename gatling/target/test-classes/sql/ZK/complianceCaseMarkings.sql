-- complianceCaseMarkings <- cmpl.compliance_case_marking
SELECT
    t.compliance_case_id::text AS "complianceCaseMarkingsComplianceCaseId",
    t.id::text AS "complianceCaseMarkingsId"
FROM cmpl.compliance_case_marking t
WHERE t.id IS NOT NULL
LIMIT ${limit}