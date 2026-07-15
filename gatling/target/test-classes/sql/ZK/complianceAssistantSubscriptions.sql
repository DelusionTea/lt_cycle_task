-- complianceAssistantSubscriptions <- cmpl.compliance_assistant_subscription
SELECT
    t.employee_number AS "complianceAssistantSubscriptionsEmployeeNumber",
    t.id::text AS "complianceAssistantSubscriptionsId",
    t.inn AS "complianceAssistantSubscriptionsInn",
    t.ucp_id::text AS "complianceAssistantSubscriptionsUcpId"
FROM cmpl.compliance_assistant_subscription t
WHERE t.id IS NOT NULL
LIMIT ${limit}