package feeders.ZK;

import io.gatling.javaapi.core.FeederBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.listFeeder;
import static io.gatling.javaapi.jdbc.JdbcDsl.jdbcFeeder;

/**
 * JDBC-фидеры ZK (как resources/Feeder.java).
 * Алиасы колонок — snake_case (ucp_id, taskid, organization_id).
 *
 * БД: -Dzk.jdbc.enabled=true -Dzk.jdbc.url=... -Dzk.jdbc.user=... -Dzk.jdbc.password=...
 */
public final class ZKFeeder {

    private ZKFeeder() {
    }

    private static String lim() {
        return String.valueOf(ZKDbConfig.limit());
    }

    private static FeederBuilder<Object> jdbc(String sql) {
        return jdbcFeeder(
                ZKDbConfig.url(),
                ZKDbConfig.user(),
                ZKDbConfig.password(),
                sql
        ).circular();
    }

    /** cmpl.compliance_organization */
    public static final FeederBuilder<Object> organizations =
            jdbc(
                    "SELECT ucp_id, ucp_id AS ucp_prof, inn, id AS organization_id FROM cmpl.compliance_organization LIMIT " + lim()
            );

    /** cmpl.compliance_task */
    public static final FeederBuilder<Object> tasks =
            jdbc(
                    "SELECT compliance_request_id, ucp_id, id AS taskid FROM cmpl.compliance_task LIMIT " + lim()
            );

    /** cmpl.compliance_account_number */
    public static final FeederBuilder<Object> accountNumbers =
            jdbc(
                    "SELECT t.compliance_request_id AS compliance_request_id, t.id AS number_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.compliance_account_number t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.ad_client_marking */
    public static final FeederBuilder<Object> adClientMarkings =
            jdbc(
                    "SELECT t.id AS marking_id, ('[' || quote_literal(t.id::text) || ']') AS marking_ids, t.organization_id AS organization_id FROM cmpl.ad_client_marking t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.ad_counter */
    public static final FeederBuilder<Object> adCounters =
            jdbc(
                    "SELECT t.id AS counter_id, t.employee_number AS employee_number FROM cmpl.ad_counter t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.ad_queue */
    public static final FeederBuilder<Object> adQueues =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.id AS queue_id FROM cmpl.ad_queue t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_attachments_for_client */
    public static final FeederBuilder<Object> attachmentsForClient =
            jdbc(
                    "SELECT t.attachment_id AS attachment_id, t.id AS client_id, t.ffl_id AS ffl_id FROM cmpl.compliance_attachments_for_client t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.blacklist */
    public static final FeederBuilder<Object> blackLists =
            jdbc(
                    "SELECT t.id AS blacklist_id FROM cmpl.blacklist t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_catalog_recommendations_cib */
    public static final FeederBuilder<Object> catalogRecommendationsCib =
            jdbc(
                    "SELECT t.id AS cib_id FROM cmpl.compliance_catalog_recommendations_cib t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.individual_request */
    public static final FeederBuilder<Object> ckr =
            jdbc(
                    "SELECT (SELECT ucp_id FROM cmpl.compliance_organization LIMIT 1) AS ucp_id, ('[' || quote_literal(t.id::text) || ']') AS ucp_ids FROM cmpl.individual_request t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_assistant_subscription */
    public static final FeederBuilder<Object> complianceAssistantSubscriptions =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.inn AS inn, t.id AS subscription_id, t.ucp_id AS ucp_id FROM cmpl.compliance_assistant_subscription t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_case_marking */
    public static final FeederBuilder<Object> complianceCaseMarkings =
            jdbc(
                    "SELECT t.compliance_case_id AS compliance_case_id, t.id AS marking_id FROM cmpl.compliance_case_marking t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_case */
    public static final FeederBuilder<Object> complianceCases =
            jdbc(
                    "SELECT t.cmpl_case_id AS cmpl_case_id, t.id AS compliance_case_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.compliance_case t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_checklist */
    public static final FeederBuilder<Object> complianceChecklists =
            jdbc(
                    "SELECT t.id AS checklist_id, t.compliance_request_id AS compliance_request_id FROM cmpl.compliance_checklist t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.deputy_employee */
    public static final FeederBuilder<Object> complianceDeputyEmployees =
            jdbc(
                    "SELECT t.id AS deputy_employee_id FROM cmpl.deputy_employee t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_employee */
    public static final FeederBuilder<Object> complianceEmployees =
            jdbc(
                    "SELECT t.id AS compliance_employee_id, t.employee_number AS employee_number, t.individual_request_id AS individual_request_id FROM cmpl.compliance_employee t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_request_history */
    public static final FeederBuilder<Object> complianceHistoryClient =
            jdbc(
                    "SELECT t.ucp_id AS ucp_id FROM cmpl.compliance_request_history t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_printed_form_config */
    public static final FeederBuilder<Object> compliancePrintedFormConfig =
            jdbc(
                    "SELECT t.id AS config_id FROM cmpl.compliance_printed_form_config t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_product_marking */
    public static final FeederBuilder<Object> complianceProductMarkings =
            jdbc(
                    "SELECT t.compliance_request_id AS compliance_request_id, t.crm_row_id AS crm_row_id, t.id AS marking_id FROM cmpl.compliance_product_marking t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_recommendations_cib */
    public static final FeederBuilder<Object> complianceRecommendations =
            jdbc(
                    "SELECT t.id AS cib_id, t.compliance_case_id AS compliance_case_id, t.compliance_request_id AS compliance_request_id, t.crm_row_id AS crm_row_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.compliance_recommendations_cib t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_request_marking */
    public static final FeederBuilder<Object> complianceRequestMarkings =
            jdbc(
                    "SELECT t.compliance_request_id AS compliance_request_id, t.id AS marking_id FROM cmpl.compliance_request_marking t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_request */
    public static final FeederBuilder<Object> complianceRequests =
            jdbc(
                    "SELECT t.id AS compliance_request_id, t.employee_number AS employee_number FROM cmpl.compliance_request t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.template_parameter */
    public static final FeederBuilder<Object> complianceTemplateParameters =
            jdbc(
                    "SELECT t.id AS template_parameter_id FROM cmpl.template_parameter t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.template */
    public static final FeederBuilder<Object> complianceTemplates =
            jdbc(
                    "SELECT t.id AS template_id FROM cmpl.\"template\" t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.constructor_pf */
    public static final FeederBuilder<Object> constructor =
            jdbc(
                    "SELECT t.id AS pf_id FROM cmpl.constructor_pf t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.constructor_pf_parameter */
    public static final FeederBuilder<Object> constructorPfParameters =
            jdbc(
                    "SELECT t.id AS parameter_id, t.parent_id AS parent_id FROM cmpl.constructor_pf_parameter t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.counterparty */
    public static final FeederBuilder<Object> counterparties =
            jdbc(
                    "SELECT t.id AS counterparty_id, ('[' || quote_literal(t.id::text) || ']') AS counterparty_ids, t.inn AS inn, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.counterparty t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.dbo_contract */
    public static final FeederBuilder<Object> dboContract =
            jdbc(
                    "SELECT t.id AS contract_id, t.digital_office_id AS digital_office_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.dbo_contract t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.digital_office */
    public static final FeederBuilder<Object> digitalOffices =
            jdbc(
                    "SELECT t.digital_id AS digital_id, t.id AS office_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.digital_office t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.digital_user_compliance_contact */
    public static final FeederBuilder<Object> digitalUserComplianceContacts =
            jdbc(
                    "SELECT t.id AS contact_id, t.digital_user_compliance_id AS digital_user_compliance_id FROM cmpl.digital_user_compliance_contact t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.digital_user_compliance */
    public static final FeederBuilder<Object> digitalUserCompliances =
            jdbc(
                    "SELECT t.id AS compliance_id, t.digital_office_id AS digital_office_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.digital_user_compliance t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.employee_notification_wl */
    public static final FeederBuilder<Object> employeeNotificationWhiteLists =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.id AS wl_id FROM cmpl.employee_notification_wl t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.employee_notification */
    public static final FeederBuilder<Object> employeeNotifications =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.id AS notification_id, t.report_id AS report_id FROM cmpl.employee_notification t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.event_history */
    public static final FeederBuilder<Object> eventsHistories =
            jdbc(
                    "SELECT t.compliance_case_id AS compliance_case_id, t.compliance_fin_operation_id AS compliance_fin_operation_id, t.compliance_request_id AS compliance_request_id, t.id AS history_id, t.individual_request_id AS individual_request_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.event_history t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.adm_condition */
    public static final FeederBuilder<Object> eventsNoticeConditions =
            jdbc(
                    "SELECT t.id AS condition_id FROM cmpl.adm_condition t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.adm_group_condition */
    public static final FeederBuilder<Object> eventsNoticeConditionsGroups =
            jdbc(
                    "SELECT t.id AS condition_id FROM cmpl.adm_group_condition t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.adm_notice */
    public static final FeederBuilder<Object> eventsNoticeSettings =
            jdbc(
                    "SELECT t.id AS notice_id FROM cmpl.adm_notice t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.feature_flag */
    public static final FeederBuilder<Object> featureFlags =
            jdbc(
                    "SELECT t.id AS flag_id FROM cmpl.feature_flag t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.file_loading_history */
    public static final FeederBuilder<Object> fileLoadingHistory =
            jdbc(
                    "SELECT t.ceph_id AS ceph_id, t.compliance_request_id AS compliance_request_id, t.ecm_folder_id AS ecm_folder_id, t.ecm_id AS ecm_id, t.ffl_id AS ffl_id, t.id AS history_id FROM cmpl.file_loading_history t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.file_transfer_task */
    public static final FeederBuilder<Object> fileTransferTasks =
            jdbc(
                    "SELECT t.compliance_request_id AS compliance_request_id, t.id AS task_id FROM cmpl.file_transfer_task t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_fin_operation */
    public static final FeederBuilder<Object> finOperations =
            jdbc(
                    "SELECT t.id AS operation_id FROM cmpl.compliance_fin_operation t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_fin_operation_attr */
    public static final FeederBuilder<Object> finOperationsAttributes =
            jdbc(
                    "SELECT t.id AS attr_id, t.compliance_fin_operation_id AS compliance_fin_operation_id FROM cmpl.compliance_fin_operation_attr t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.reference_mapping_fin_operation */
    public static final FeederBuilder<Object> finOperationsReferenceMapping =
            jdbc(
                    "SELECT t.id AS operation_id FROM cmpl.reference_mapping_fin_operation t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_free_format_letter */
    public static final FeederBuilder<Object> freeFormatLetters =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.folder_ecm_id AS folder_ecm_id, t.id AS letter_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.compliance_free_format_letter t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_communication */
    public static final FeederBuilder<Object> getAllCommunications =
            jdbc(
                    "SELECT t.id AS communication_id FROM cmpl.compliance_communication t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.individual_request_history */
    public static final FeederBuilder<Object> individualRequestHistory =
            jdbc(
                    "SELECT t.cib_ucp_sfl_id AS cib_ucp_sfl_id, t.ck_case_id AS ck_case_id, t.ck_request_id AS ck_request_id, t.employee_number AS employee_number, t.id AS history_id, t.individual_request_id AS individual_request_id, t.ucp_sfl_id AS ucp_sfl_id, t.uvsk_id AS uvsk_id, t.uvsk_public_id AS uvsk_public_id FROM cmpl.individual_request_history t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.infrastructure */
    public static final FeederBuilder<Object> infrastructures =
            jdbc(
                    "SELECT t.id AS infrastructure_id, ('[' || quote_literal(t.id::text) || ']') AS infrastructure_ids FROM cmpl.infrastructure t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.int_lock */
    public static final FeederBuilder<Object> intLocks =
            jdbc(
                    "SELECT t.client_id AS client_id, t.lock_key AS lock_key, t.region AS region FROM cmpl.int_lock t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.integration_logging */
    public static final FeederBuilder<Object> integrationLogReports =
            jdbc(
                    "SELECT t.id AS logging_id, t.message_id AS message_id FROM cmpl.integration_logging t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.job_history */
    public static final FeederBuilder<Object> jobHistories =
            jdbc(
                    "SELECT t.execution_id AS execution_id, t.id AS history_id FROM cmpl.job_history t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.link_individual_organization */
    public static final FeederBuilder<Object> linkIndividualOrganization =
            jdbc(
                    "SELECT t.organization_id AS organization_id, t.ucp_sfl_id AS ucp_sfl_id FROM cmpl.link_individual_organization t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.link_organization_ucp_id */
    public static final FeederBuilder<Object> linkOrganizationUcpId =
            jdbc(
                    "SELECT t.id AS id_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.link_organization_ucp_id t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.link_template_template_parameter */
    public static final FeederBuilder<Object> linkTemplateParameters =
            jdbc(
                    "SELECT t.id AS parameter_id, t.template_id AS template_id, t.template_parameter_id AS template_parameter_id FROM cmpl.link_template_template_parameter t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_organization_attribute */
    public static final FeederBuilder<Object> organizationAttributes =
            jdbc(
                    "SELECT t.id AS attribute_id, ('[' || quote_literal(t.id::text) || ']') AS attribute_ids, t.organization_id AS organization_id FROM cmpl.compliance_organization_attribute t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.organization_business_scheme */
    public static final FeederBuilder<Object> organizationBusinessScheme =
            jdbc(
                    "SELECT t.id AS scheme_id FROM cmpl.organization_business_scheme t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.organization_business_scheme_attachment */
    public static final FeederBuilder<Object> organizationBusinessSchemeAttachments =
            jdbc(
                    "SELECT t.id AS attachment_id, t.ecm_id AS ecm_id FROM cmpl.organization_business_scheme_attachment t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.compliance_organization_ext */
    public static final FeederBuilder<Object> organizationExtensions =
            jdbc(
                    "SELECT t.id AS ext_id, ('[' || quote_literal(t.id::text) || ']') AS ext_ids, t.organization_id AS organization_id FROM cmpl.compliance_organization_ext t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.pilot_log */
    public static final FeederBuilder<Object> pilotLog =
            jdbc(
                    "SELECT t.id AS pilot_log_id, t.process_setting_id AS process_setting_id FROM cmpl.pilot_log t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.proactive_attributes */
    public static final FeederBuilder<Object> proactiveAttributes =
            jdbc(
                    "SELECT t.id AS attributes_id, t.proactive_onboarding_id AS proactive_onboarding_id FROM cmpl.proactive_attributes t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.proactive_catalog_recommendations */
    public static final FeederBuilder<Object> proactiveCatalogRecommendations =
            jdbc(
                    "SELECT t.id AS recommendations_id FROM cmpl.proactive_catalog_recommendations t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.proactive_communication */
    public static final FeederBuilder<Object> proactiveCommunications =
            jdbc(
                    "SELECT t.id AS communication_id, t.contact_digital_id AS contact_digital_id, t.contact_digital_user_id AS contact_digital_user_id, t.employee_number AS employee_number, t.organization_id AS organization_id, t.proactive_onboarding_id AS proactive_onboarding_id, t.template_id AS template_id, t.ucp_id AS ucp_id FROM cmpl.proactive_communication t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.proactive_onboarding */
    public static final FeederBuilder<Object> proactiveOnboardings =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.id AS onboarding_id, t.organization_id AS organization_id, t.ucp_id AS ucp_id FROM cmpl.proactive_onboarding t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.proactive_task */
    public static final FeederBuilder<Object> proactiveTasks =
            jdbc(
                    "SELECT t.employee_number AS employee_number, t.organization_id AS organization_id, t.proactive_onboarding_id AS proactive_onboarding_id, t.id AS task_id, t.ucp_id AS ucp_id FROM cmpl.proactive_task t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.process_setting */
    public static final FeederBuilder<Object> processSettings =
            jdbc(
                    "SELECT t.id AS setting_id FROM cmpl.process_setting t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.link_lst_of_val */
    public static final FeederBuilder<Object> referenceValueLinks =
            jdbc(
                    "SELECT t.id AS val_id FROM cmpl.link_lst_of_val t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.lst_of_val */
    public static final FeederBuilder<Object> referenceValues =
            jdbc(
                    "SELECT t.id AS val_id FROM cmpl.lst_of_val t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.rehabilitation_buffer_checklist */
    public static final FeederBuilder<Object> rehabilitationBufferChecklists =
            jdbc(
                    "SELECT t.id AS checklist_id, t.ck_id AS ck_id, t.rehabilitation_buffer_request_id AS rehabilitation_buffer_request_id FROM cmpl.rehabilitation_buffer_checklist t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.rehabilitation_buffer_request */
    public static final FeederBuilder<Object> rehabilitationBufferRequests =
            jdbc(
                    "SELECT t.compliance_case_id AS compliance_case_id, t.compliance_fin_operation_id AS compliance_fin_operation_id, t.compliance_request_id AS compliance_request_id, t.fccm_request_id AS fccm_request_id, t.organization_id AS organization_id, t.id AS request_id, t.ucp_id AS ucp_id FROM cmpl.rehabilitation_buffer_request t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.report */
    public static final FeederBuilder<Object> reports =
            jdbc(
                    "SELECT t.file_ecm_id AS file_ecm_id, t.id AS report_id FROM cmpl.report t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    /** cmpl.sys_pref */
    public static final FeederBuilder<Object> systemPreferences =
            jdbc(
                    "SELECT t.id AS sys_pref_id FROM cmpl.sys_pref t WHERE t.id IS NOT NULL LIMIT " + lim()
            );

    private static Map<String, Object> stubRow() {
        Map<String, Object> row = new HashMap<>();
        row.put("attachment_id", "00000000-0000-0000-0000-000000000001");
        row.put("attr_id", "00000000-0000-0000-0000-000000000001");
        row.put("attribute_id", "00000000-0000-0000-0000-000000000001");
        row.put("attribute_ids", "[\"00000000-0000-0000-0000-000000000001\"]");
        row.put("attributes_id", "00000000-0000-0000-0000-000000000001");
        row.put("blacklist_id", "00000000-0000-0000-0000-000000000001");
        row.put("ceph_id", "00000000-0000-0000-0000-000000000001");
        row.put("checklist_id", "00000000-0000-0000-0000-000000000001");
        row.put("cib_id", "00000000-0000-0000-0000-000000000001");
        row.put("cib_ucp_sfl_id", "1");
        row.put("ck_case_id", "sample_ck_case_id");
        row.put("ck_id", "sample_ck_id");
        row.put("ck_request_id", "sample_ck_request_id");
        row.put("client_id", "00000000-0000-0000-0000-000000000001");
        row.put("cmpl_case_id", "sample_cmpl_case_id");
        row.put("communication_id", "00000000-0000-0000-0000-000000000001");
        row.put("compliance_case_id", "00000000-0000-0000-0000-000000000001");
        row.put("compliance_employee_id", "00000000-0000-0000-0000-000000000001");
        row.put("compliance_fin_operation_id", "00000000-0000-0000-0000-000000000001");
        row.put("compliance_id", "00000000-0000-0000-0000-000000000001");
        row.put("compliance_request_id", "00000000-0000-0000-0000-000000000001");
        row.put("condition_id", "00000000-0000-0000-0000-000000000001");
        row.put("config_id", "00000000-0000-0000-0000-000000000001");
        row.put("contact_digital_id", "1");
        row.put("contact_digital_user_id", "sample_contact_digital_user_id");
        row.put("contact_id", "00000000-0000-0000-0000-000000000001");
        row.put("contract_id", "00000000-0000-0000-0000-000000000001");
        row.put("counter_id", "00000000-0000-0000-0000-000000000001");
        row.put("counterparty_id", "00000000-0000-0000-0000-000000000001");
        row.put("counterparty_ids", "[\"00000000-0000-0000-0000-000000000001\"]");
        row.put("crm_row_id", "sample_crm_row_id");
        row.put("deputy_employee_id", "00000000-0000-0000-0000-000000000001");
        row.put("digital_id", "1");
        row.put("digital_office_id", "00000000-0000-0000-0000-000000000001");
        row.put("digital_user_compliance_id", "00000000-0000-0000-0000-000000000001");
        row.put("ecm_folder_id", "sample_ecm_folder_id");
        row.put("ecm_id", "sample_ecm_id");
        row.put("employee_number", "sample_employee_number");
        row.put("execution_id", "00000000-0000-0000-0000-000000000001");
        row.put("ext_id", "00000000-0000-0000-0000-000000000001");
        row.put("ext_ids", "[\"00000000-0000-0000-0000-000000000001\"]");
        row.put("fccm_request_id", "sample_fccm_request_id");
        row.put("ffl_id", "00000000-0000-0000-0000-000000000001");
        row.put("file_ecm_id", "sample_file_ecm_id");
        row.put("flag_id", "00000000-0000-0000-0000-000000000001");
        row.put("folder_ecm_id", "00000000-0000-0000-0000-000000000001");
        row.put("history_id", "00000000-0000-0000-0000-000000000001");
        row.put("id_id", "00000000-0000-0000-0000-000000000001");
        row.put("individual_request_id", "00000000-0000-0000-0000-000000000001");
        row.put("infrastructure_id", "00000000-0000-0000-0000-000000000001");
        row.put("infrastructure_ids", "[\"00000000-0000-0000-0000-000000000001\"]");
        row.put("inn", "0000000000");
        row.put("letter_id", "00000000-0000-0000-0000-000000000001");
        row.put("lock_key", "sample");
        row.put("logging_id", "00000000-0000-0000-0000-000000000001");
        row.put("marking_id", "00000000-0000-0000-0000-000000000001");
        row.put("marking_ids", "[\"00000000-0000-0000-0000-000000000001\"]");
        row.put("message_id", "00000000-0000-0000-0000-000000000001");
        row.put("notice_id", "00000000-0000-0000-0000-000000000001");
        row.put("notification_id", "00000000-0000-0000-0000-000000000001");
        row.put("number_id", "00000000-0000-0000-0000-000000000001");
        row.put("office_id", "00000000-0000-0000-0000-000000000001");
        row.put("onboarding_id", "00000000-0000-0000-0000-000000000001");
        row.put("operation_id", "00000000-0000-0000-0000-000000000001");
        row.put("organization_id", "00000000-0000-0000-0000-000000000001");
        row.put("parameter_id", "00000000-0000-0000-0000-000000000001");
        row.put("parent_id", "00000000-0000-0000-0000-000000000001");
        row.put("pf_id", "00000000-0000-0000-0000-000000000001");
        row.put("pilot_log_id", "00000000-0000-0000-0000-000000000001");
        row.put("proactive_onboarding_id", "00000000-0000-0000-0000-000000000001");
        row.put("process_setting_id", "00000000-0000-0000-0000-000000000001");
        row.put("queue_id", "00000000-0000-0000-0000-000000000001");
        row.put("recommendations_id", "00000000-0000-0000-0000-000000000001");
        row.put("region", "sample");
        row.put("rehabilitation_buffer_request_id", "00000000-0000-0000-0000-000000000001");
        row.put("report_id", "00000000-0000-0000-0000-000000000001");
        row.put("request_id", "00000000-0000-0000-0000-000000000001");
        row.put("scheme_id", "00000000-0000-0000-0000-000000000001");
        row.put("setting_id", "00000000-0000-0000-0000-000000000001");
        row.put("subscription_id", "00000000-0000-0000-0000-000000000001");
        row.put("sys_pref_id", "00000000-0000-0000-0000-000000000001");
        row.put("task_id", "00000000-0000-0000-0000-000000000001");
        row.put("taskid", "00000000-0000-0000-0000-000000000001");
        row.put("template_id", "00000000-0000-0000-0000-000000000001");
        row.put("template_parameter_id", "00000000-0000-0000-0000-000000000001");
        row.put("ucp_id", "1");
        row.put("ucp_ids", "[\"00000000-0000-0000-0000-000000000001\"]");
        row.put("ucp_prof", "sample");
        row.put("ucp_sfl_id", "00000000-0000-0000-0000-000000000001");
        row.put("uvsk_id", "00000000-0000-0000-0000-000000000001");
        row.put("uvsk_public_id", "00000000-0000-0000-0000-000000000001");
        row.put("val_id", "00000000-0000-0000-0000-000000000001");
        row.put("wl_id", "00000000-0000-0000-0000-000000000001");
        return row;
    }

    /** Базовый feeder: organizations (ucp_id, inn). Без БД — stub. */
    public static final FeederBuilder<Object> defaultFeeder =
            ZKDbConfig.useJdbc() ? organizations : listFeeder(List.of(stubRow())).circular();
}
