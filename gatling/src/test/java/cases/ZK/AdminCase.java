package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AdminCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_admin_attribute_display_configs =
            http("UC01_GET_/api/v2/admin/attribute-display-configs")
                    .get("/api/v2/admin/attribute-display-configs")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_admin_attribute_display_configs =
            http("UC02_POST_/api/v2/admin/attribute-display-configs")
                    .post("/api/v2/admin/attribute-display-configs")
                    .body(ElFileBody(JSONS_PATH + "shared/AttributeDisplayConfigApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v2_admin_attribute_display_configs_batch_identical =
            http("UC03_PATCH_/api/v2/admin/attribute-display-configs/batch-identical")
                    .patch("/api/v2/admin/attribute-display-configs/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "shared/AttributeDisplayConfigApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v2_admin_attribute_display_configs_By_id =
            http("UC04_DELETE_/api/v2/admin/attribute-display-configs/{id}")
                    .delete("/api/v2/admin/attribute-display-configs/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v2_admin_attribute_display_configs_By_id =
            http("UC05_GET_/api/v2/admin/attribute-display-configs/{id}")
                    .get("/api/v2/admin/attribute-display-configs/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v2_admin_attribute_display_configs_By_id =
            http("UC06_PATCH_/api/v2/admin/attribute-display-configs/{id}")
                    .patch("/api/v2/admin/attribute-display-configs/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "shared/AttributeDisplayConfigApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v2_admin_attribute_display_configs_By_id =
            http("UC07_PUT_/api/v2/admin/attribute-display-configs/{id}")
                    .put("/api/v2/admin/attribute-display-configs/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "shared/AttributeDisplayConfigApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC08_GET_v2_admin_compliance_case_decisions =
            http("UC08_GET_/api/v2/admin/compliance-case-decisions")
                    .get("/api/v2/admin/compliance-case-decisions")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_POST_v2_admin_compliance_case_decisions =
            http("UC09_POST_/api/v2/admin/compliance-case-decisions")
                    .post("/api/v2/admin/compliance-case-decisions")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCaseDecisionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_PATCH_v2_admin_compliance_case_decisions_batch_identical =
            http("UC10_PATCH_/api/v2/admin/compliance-case-decisions/batch-identical")
                    .patch("/api/v2/admin/compliance-case-decisions/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCaseDecisionApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_DELETE_v2_admin_compliance_case_decisions_By_id =
            http("UC11_DELETE_/api/v2/admin/compliance-case-decisions/{id}")
                    .delete("/api/v2/admin/compliance-case-decisions/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC12_GET_v2_admin_compliance_case_decisions_By_id =
            http("UC12_GET_/api/v2/admin/compliance-case-decisions/{id}")
                    .get("/api/v2/admin/compliance-case-decisions/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC13_PATCH_v2_admin_compliance_case_decisions_By_id =
            http("UC13_PATCH_/api/v2/admin/compliance-case-decisions/{id}")
                    .patch("/api/v2/admin/compliance-case-decisions/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCaseDecisionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC14_PUT_v2_admin_compliance_case_decisions_By_id =
            http("UC14_PUT_/api/v2/admin/compliance-case-decisions/{id}")
                    .put("/api/v2/admin/compliance-case-decisions/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCaseDecisionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC15_GET_v2_admin_compliance_checklists =
            http("UC15_GET_/api/v2/admin/compliance-checklists")
                    .get("/api/v2/admin/compliance-checklists")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC16_POST_v2_admin_compliance_checklists =
            http("UC16_POST_/api/v2/admin/compliance-checklists")
                    .post("/api/v2/admin/compliance-checklists")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceChecklistApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC17_PATCH_v2_admin_compliance_checklists_batch_identical =
            http("UC17_PATCH_/api/v2/admin/compliance-checklists/batch-identical")
                    .patch("/api/v2/admin/compliance-checklists/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceChecklistApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC18_DELETE_v2_admin_compliance_checklists_By_id =
            http("UC18_DELETE_/api/v2/admin/compliance-checklists/{id}")
                    .delete("/api/v2/admin/compliance-checklists/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC19_GET_v2_admin_compliance_checklists_By_id =
            http("UC19_GET_/api/v2/admin/compliance-checklists/{id}")
                    .get("/api/v2/admin/compliance-checklists/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC20_PATCH_v2_admin_compliance_checklists_By_id =
            http("UC20_PATCH_/api/v2/admin/compliance-checklists/{id}")
                    .patch("/api/v2/admin/compliance-checklists/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceChecklistApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC21_PUT_v2_admin_compliance_checklists_By_id =
            http("UC21_PUT_/api/v2/admin/compliance-checklists/{id}")
                    .put("/api/v2/admin/compliance-checklists/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceChecklistApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC22_GET_v2_admin_compliance_communications =
            http("UC22_GET_/api/v2/admin/compliance-communications")
                    .get("/api/v2/admin/compliance-communications")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC23_POST_v2_admin_compliance_communications =
            http("UC23_POST_/api/v2/admin/compliance-communications")
                    .post("/api/v2/admin/compliance-communications")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCommunicationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC24_PATCH_v2_admin_compliance_communications_batch_identical =
            http("UC24_PATCH_/api/v2/admin/compliance-communications/batch-identical")
                    .patch("/api/v2/admin/compliance-communications/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCommunicationApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC25_DELETE_v2_admin_compliance_communications_By_id =
            http("UC25_DELETE_/api/v2/admin/compliance-communications/{id}")
                    .delete("/api/v2/admin/compliance-communications/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC26_GET_v2_admin_compliance_communications_By_id =
            http("UC26_GET_/api/v2/admin/compliance-communications/{id}")
                    .get("/api/v2/admin/compliance-communications/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC27_PATCH_v2_admin_compliance_communications_By_id =
            http("UC27_PATCH_/api/v2/admin/compliance-communications/{id}")
                    .patch("/api/v2/admin/compliance-communications/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCommunicationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC28_PUT_v2_admin_compliance_communications_By_id =
            http("UC28_PUT_/api/v2/admin/compliance-communications/{id}")
                    .put("/api/v2/admin/compliance-communications/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceCommunicationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC29_GET_v2_admin_compliance_tasks =
            http("UC29_GET_/api/v2/admin/compliance-tasks")
                    .get("/api/v2/admin/compliance-tasks")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC30_POST_v2_admin_compliance_tasks =
            http("UC30_POST_/api/v2/admin/compliance-tasks")
                    .post("/api/v2/admin/compliance-tasks")
                    .body(ElFileBody(JSONS_PATH + "Admin/TaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC31_PATCH_v2_admin_compliance_tasks_batch_identical =
            http("UC31_PATCH_/api/v2/admin/compliance-tasks/batch-identical")
                    .patch("/api/v2/admin/compliance-tasks/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/TaskApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC32_DELETE_v2_admin_compliance_tasks_By_id =
            http("UC32_DELETE_/api/v2/admin/compliance-tasks/{id}")
                    .delete("/api/v2/admin/compliance-tasks/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC33_GET_v2_admin_compliance_tasks_By_id =
            http("UC33_GET_/api/v2/admin/compliance-tasks/{id}")
                    .get("/api/v2/admin/compliance-tasks/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC34_PATCH_v2_admin_compliance_tasks_By_id =
            http("UC34_PATCH_/api/v2/admin/compliance-tasks/{id}")
                    .patch("/api/v2/admin/compliance-tasks/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/TaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC35_PUT_v2_admin_compliance_tasks_By_id =
            http("UC35_PUT_/api/v2/admin/compliance-tasks/{id}")
                    .put("/api/v2/admin/compliance-tasks/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/TaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC36_GET_v2_admin_fin_operations_settings =
            http("UC36_GET_/api/v2/admin/fin-operations-settings")
                    .get("/api/v2/admin/fin-operations-settings")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC37_POST_v2_admin_fin_operations_settings =
            http("UC37_POST_/api/v2/admin/fin-operations-settings")
                    .post("/api/v2/admin/fin-operations-settings")
                    .body(ElFileBody(JSONS_PATH + "shared/ComplianceFinOperationSettingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC38_PATCH_v2_admin_fin_operations_settings_batch_identical =
            http("UC38_PATCH_/api/v2/admin/fin-operations-settings/batch-identical")
                    .patch("/api/v2/admin/fin-operations-settings/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "shared/ComplianceFinOperationSettingApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC39_DELETE_v2_admin_fin_operations_settings_By_id =
            http("UC39_DELETE_/api/v2/admin/fin-operations-settings/{id}")
                    .delete("/api/v2/admin/fin-operations-settings/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC40_GET_v2_admin_fin_operations_settings_By_id =
            http("UC40_GET_/api/v2/admin/fin-operations-settings/{id}")
                    .get("/api/v2/admin/fin-operations-settings/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC41_PATCH_v2_admin_fin_operations_settings_By_id =
            http("UC41_PATCH_/api/v2/admin/fin-operations-settings/{id}")
                    .patch("/api/v2/admin/fin-operations-settings/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "shared/ComplianceFinOperationSettingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC42_PUT_v2_admin_fin_operations_settings_By_id =
            http("UC42_PUT_/api/v2/admin/fin-operations-settings/{id}")
                    .put("/api/v2/admin/fin-operations-settings/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "shared/ComplianceFinOperationSettingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC43_GET_v2_admin_organizations =
            http("UC43_GET_/api/v2/admin/organizations")
                    .get("/api/v2/admin/organizations")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC44_POST_v2_admin_organizations =
            http("UC44_POST_/api/v2/admin/organizations")
                    .post("/api/v2/admin/organizations")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceOrganizationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC45_PATCH_v2_admin_organizations_batch_identical =
            http("UC45_PATCH_/api/v2/admin/organizations/batch-identical")
                    .patch("/api/v2/admin/organizations/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceOrganizationApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC46_GET_v2_admin_organizations_check_actual_id =
            http("UC46_GET_/api/v2/admin/organizations/check-actual-id")
                    .get("/api/v2/admin/organizations/check-actual-id")
                    .queryParam("ucpId", "#{adminUcpId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC47_POST_v2_admin_organizations_check_actual_id_or_create =
            http("UC47_POST_/api/v2/admin/organizations/check-actual-id-or-create")
                    .post("/api/v2/admin/organizations/check-actual-id-or-create")
                    .queryParam("ucpId", "#{adminUcpId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC48_DELETE_v2_admin_organizations_By_id =
            http("UC48_DELETE_/api/v2/admin/organizations/{id}")
                    .delete("/api/v2/admin/organizations/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC49_GET_v2_admin_organizations_By_id =
            http("UC49_GET_/api/v2/admin/organizations/{id}")
                    .get("/api/v2/admin/organizations/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC50_PATCH_v2_admin_organizations_By_id =
            http("UC50_PATCH_/api/v2/admin/organizations/{id}")
                    .patch("/api/v2/admin/organizations/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceOrganizationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC51_PUT_v2_admin_organizations_By_id =
            http("UC51_PUT_/api/v2/admin/organizations/{id}")
                    .put("/api/v2/admin/organizations/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceOrganizationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC52_GET_v3_admin_compliance_requests =
            http("UC52_GET_/api/v3/admin/compliance-requests")
                    .get("/api/v3/admin/compliance-requests")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC53_POST_v3_admin_compliance_requests =
            http("UC53_POST_/api/v3/admin/compliance-requests")
                    .post("/api/v3/admin/compliance-requests")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC54_PATCH_v3_admin_compliance_requests_batch_identical =
            http("UC54_PATCH_/api/v3/admin/compliance-requests/batch-identical")
                    .patch("/api/v3/admin/compliance-requests/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceRequestApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC55_GET_v3_admin_compliance_requests_by_ucp_id_for_mmb =
            http("UC55_GET_/api/v3/admin/compliance-requests/by-ucp-id-for-mmb")
                    .get("/api/v3/admin/compliance-requests/by-ucp-id-for-mmb")
                    .queryParam("ucpId", "#{adminUcpId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC56_DELETE_v3_admin_compliance_requests_By_id =
            http("UC56_DELETE_/api/v3/admin/compliance-requests/{id}")
                    .delete("/api/v3/admin/compliance-requests/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC57_GET_v3_admin_compliance_requests_By_id =
            http("UC57_GET_/api/v3/admin/compliance-requests/{id}")
                    .get("/api/v3/admin/compliance-requests/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC58_PATCH_v3_admin_compliance_requests_By_id =
            http("UC58_PATCH_/api/v3/admin/compliance-requests/{id}")
                    .patch("/api/v3/admin/compliance-requests/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC59_PUT_v3_admin_compliance_requests_By_id =
            http("UC59_PUT_/api/v3/admin/compliance-requests/{id}")
                    .put("/api/v3/admin/compliance-requests/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC60_GET_v3_admin_fin_operations =
            http("UC60_GET_/api/v3/admin/fin-operations")
                    .get("/api/v3/admin/fin-operations")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC61_POST_v3_admin_fin_operations =
            http("UC61_POST_/api/v3/admin/fin-operations")
                    .post("/api/v3/admin/fin-operations")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceFinOperationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC62_PATCH_v3_admin_fin_operations_batch_identical =
            http("UC62_PATCH_/api/v3/admin/fin-operations/batch-identical")
                    .patch("/api/v3/admin/fin-operations/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceFinOperationApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC63_GET_v3_admin_fin_operations_by_ucp_id_for_mmb =
            http("UC63_GET_/api/v3/admin/fin-operations/by-ucp-id-for-mmb")
                    .get("/api/v3/admin/fin-operations/by-ucp-id-for-mmb")
                    .queryParam("ucpId", "#{adminUcpId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC64_DELETE_v3_admin_fin_operations_By_id =
            http("UC64_DELETE_/api/v3/admin/fin-operations/{id}")
                    .delete("/api/v3/admin/fin-operations/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC65_GET_v3_admin_fin_operations_By_id =
            http("UC65_GET_/api/v3/admin/fin-operations/{id}")
                    .get("/api/v3/admin/fin-operations/#{adminId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC66_PATCH_v3_admin_fin_operations_By_id =
            http("UC66_PATCH_/api/v3/admin/fin-operations/{id}")
                    .patch("/api/v3/admin/fin-operations/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceFinOperationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC67_PUT_v3_admin_fin_operations_By_id =
            http("UC67_PUT_/api/v3/admin/fin-operations/{id}")
                    .put("/api/v3/admin/fin-operations/#{adminId}")
                    .body(ElFileBody(JSONS_PATH + "Admin/ComplianceFinOperationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
