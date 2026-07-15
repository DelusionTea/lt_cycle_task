-- digitalUserComplianceContacts <- cmpl.digital_user_compliance_contact
SELECT
    t.digital_user_compliance_id::text AS "digitalUserComplianceContactsDigitalUserComplianceId",
    t.id::text AS "digitalUserComplianceContactsId"
FROM cmpl.digital_user_compliance_contact t
WHERE t.id IS NOT NULL
LIMIT ${limit}