-- proactiveCommunications <- cmpl.proactive_communication
SELECT
    t.organization_id::text AS "proactiveCommunicationsComplianceOrganizationId",
    t.contact_digital_id::text AS "proactiveCommunicationsContactDigitalId",
    t.contact_digital_user_id AS "proactiveCommunicationsContactDigitalUserId",
    t.employee_number AS "proactiveCommunicationsEmployeeNumber",
    t.id::text AS "proactiveCommunicationsId",
    t.proactive_onboarding_id::text AS "proactiveCommunicationsProactiveOnboardingId",
    t.template_id::text AS "proactiveCommunicationsTemplateId",
    t.ucp_id::text AS "proactiveCommunicationsUcpId"
FROM cmpl.proactive_communication t
WHERE t.id IS NOT NULL
LIMIT ${limit}