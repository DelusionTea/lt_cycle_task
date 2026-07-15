-- complianceRequestMarkings <- cmpl.compliance_request_marking
SELECT
    t.compliance_request_id::text AS "complianceRequestMarkingsComplianceRequestId",
    t.id::text AS "complianceRequestMarkingsId"
FROM cmpl.compliance_request_marking t
WHERE t.id IS NOT NULL
LIMIT ${limit}