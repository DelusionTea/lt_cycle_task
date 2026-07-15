package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SbbolCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_sbbol_compliance_requests_callback_cancel =
            http("UC01_GET_/api/v1/sbbol/compliance-requests/callback/cancel")
                    .get("/api/v1/sbbol/compliance-requests/callback/cancel")
                    .queryParam("taskId", "#{taskid}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_sbbol_compliance_requests_callback_save =
            http("UC02_POST_/api/v1/sbbol/compliance-requests/callback/save")
                    .post("/api/v1/sbbol/compliance-requests/callback/save")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/SaveCallbackRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_POST_v1_sbbol_compliance_requests_callback_update =
            http("UC03_POST_/api/v1/sbbol/compliance-requests/callback/update")
                    .post("/api/v1/sbbol/compliance-requests/callback/update")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/SaveCallbackRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_GET_v1_sbbol_compliance_requests_mop_one =
            http("UC04_GET_/api/v1/sbbol/compliance-requests/mop-one")
                    .get("/api/v1/sbbol/compliance-requests/mop-one")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/MopOneDetailRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_sbbol_compliance_requests_rehabilitation =
            http("UC05_GET_/api/v1/sbbol/compliance-requests/rehabilitation")
                    .get("/api/v1/sbbol/compliance-requests/rehabilitation")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/MopOneDetailRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC06_GET_v1_sbbol_compliance_requests_start_rehabilitation_By_requestId =
            http("UC06_GET_/api/v1/sbbol/compliance-requests/start-rehabilitation/{requestId}")
                    .get("/api/v1/sbbol/compliance-requests/start-rehabilitation/#{compliance_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC07_GET_v1_sbbol_free_format_letters_detail =
            http("UC07_GET_/api/v1/sbbol/free-format-letters/detail")
                    .get("/api/v1/sbbol/free-format-letters/detail")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/FreeFormatLetterDetailRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC08_POST_v2_sbbol_compliance_requests_attachments =
            http("UC08_POST_/api/v2/sbbol/compliance-requests/attachments")
                    .post("/api/v2/sbbol/compliance-requests/attachments")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/SaveAttachmentRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_POST_v2_sbbol_compliance_requests_attachments_batch =
            http("UC09_POST_/api/v2/sbbol/compliance-requests/attachments/batch")
                    .post("/api/v2/sbbol/compliance-requests/attachments/batch")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/body_e6cfb526.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_POST_v2_sbbol_compliance_requests_attachments_with_transfer =
            http("UC10_POST_/api/v2/sbbol/compliance-requests/attachments/with-transfer")
                    .post("/api/v2/sbbol/compliance-requests/attachments/with-transfer")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/SaveAttachmentRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_DELETE_v2_sbbol_compliance_requests_attachments_By_id =
            http("UC11_DELETE_/api/v2/sbbol/compliance-requests/attachments/{id}")
                    .delete("/api/v2/sbbol/compliance-requests/attachments/#{compliance_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC12_POST_v2_sbbol_compliance_requests_callback_detail =
            http("UC12_POST_/api/v2/sbbol/compliance-requests/callback/detail")
                    .post("/api/v2/sbbol/compliance-requests/callback/detail")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/CallbackDetailRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC13_POST_v2_sbbol_compliance_requests_callback_info =
            http("UC13_POST_/api/v2/sbbol/compliance-requests/callback/info")
                    .post("/api/v2/sbbol/compliance-requests/callback/info")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/CallbackInfoRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC14_GET_v2_sbbol_compliance_requests_compliance_profile =
            http("UC14_GET_/api/v2/sbbol/compliance-requests/compliance-profile")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC15_DELETE_v2_sbbol_compliance_requests_compliance_profile_alert =
            http("UC15_DELETE_/api/v2/sbbol/compliance-requests/compliance-profile/alert")
                    .delete("/api/v2/sbbol/compliance-requests/compliance-profile/alert")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC16_GET_v2_sbbol_compliance_requests_compliance_profile_contacts =
            http("UC16_GET_/api/v2/sbbol/compliance-requests/compliance-profile/contacts")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile/contacts")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC17_POST_v2_sbbol_compliance_requests_compliance_profile_contacts =
            http("UC17_POST_/api/v2/sbbol/compliance-requests/compliance-profile/contacts")
                    .post("/api/v2/sbbol/compliance-requests/compliance-profile/contacts")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceProfileContactsRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC18_GET_v2_sbbol_compliance_requests_compliance_profile_counterparties =
            http("UC18_GET_/api/v2/sbbol/compliance-requests/compliance-profile/counterparties")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile/counterparties")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC19_POST_v2_sbbol_compliance_requests_compliance_profile_counterparties =
            http("UC19_POST_/api/v2/sbbol/compliance-requests/compliance-profile/counterparties")
                    .post("/api/v2/sbbol/compliance-requests/compliance-profile/counterparties")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceProfileCounterpartiesUpdateRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC20_GET_v2_sbbol_compliance_requests_compliance_profile_detail =
            http("UC20_GET_/api/v2/sbbol/compliance-requests/compliance-profile/detail")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile/detail")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC21_GET_v2_sbbol_compliance_requests_compliance_profile_dopInfo =
            http("UC21_GET_/api/v2/sbbol/compliance-requests/compliance-profile/dopInfo")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile/dopInfo")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC22_POST_v2_sbbol_compliance_requests_compliance_profile_dopInfo =
            http("UC22_POST_/api/v2/sbbol/compliance-requests/compliance-profile/dopInfo")
                    .post("/api/v2/sbbol/compliance-requests/compliance-profile/dopInfo")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceProfileAdditionalInfoRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC23_GET_v2_sbbol_compliance_requests_compliance_profile_mtb =
            http("UC23_GET_/api/v2/sbbol/compliance-requests/compliance-profile/mtb")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile/mtb")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC24_POST_v2_sbbol_compliance_requests_compliance_profile_mtb =
            http("UC24_POST_/api/v2/sbbol/compliance-requests/compliance-profile/mtb")
                    .post("/api/v2/sbbol/compliance-requests/compliance-profile/mtb")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceProfileMtbRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC25_GET_v2_sbbol_compliance_requests_compliance_profile_organization =
            http("UC25_GET_/api/v2/sbbol/compliance-requests/compliance-profile/organization")
                    .get("/api/v2/sbbol/compliance-requests/compliance-profile/organization")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceProfileByOrganizationRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC26_POST_v2_sbbol_compliance_requests_compliance_profile_organization =
            http("UC26_POST_/api/v2/sbbol/compliance-requests/compliance-profile/organization")
                    .post("/api/v2/sbbol/compliance-requests/compliance-profile/organization")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceProfileOrganizationUpdateRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC27_GET_v2_sbbol_compliance_requests_organization_by_ucp_id_By_ucpId =
            http("UC27_GET_/api/v2/sbbol/compliance-requests/organization/by-ucp-id/{ucpId}")
                    .get("/api/v2/sbbol/compliance-requests/organization/by-ucp-id/#{ucp_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC28_GET_v2_sbbol_compliance_requests_proactive =
            http("UC28_GET_/api/v2/sbbol/compliance-requests/proactive")
                    .get("/api/v2/sbbol/compliance-requests/proactive")
                    .queryParam("pagingRequest", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC29_GET_v2_sbbol_compliance_requests_proactive_by_ucp =
            http("UC29_GET_/api/v2/sbbol/compliance-requests/proactive/by-ucp")
                    .get("/api/v2/sbbol/compliance-requests/proactive/by-ucp")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC30_GET_v2_sbbol_compliance_requests_proactive_mop2 =
            http("UC30_GET_/api/v2/sbbol/compliance-requests/proactive/mop2")
                    .get("/api/v2/sbbol/compliance-requests/proactive/mop2")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ProactiveDetailRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC31_GET_v2_sbbol_compliance_requests_proactive_onboarding =
            http("UC31_GET_/api/v2/sbbol/compliance-requests/proactive/onboarding")
                    .get("/api/v2/sbbol/compliance-requests/proactive/onboarding")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ProactiveDetailRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC32_GET_v2_sbbol_compliance_requests_request_online_control =
            http("UC32_GET_/api/v2/sbbol/compliance-requests/request/online-control")
                    .get("/api/v2/sbbol/compliance-requests/request/online-control")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/ComplianceOnlineRequestRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC33_POST_v2_sbbol_compliance_requests_send_to_bank_By_requestId =
            http("UC33_POST_/api/v2/sbbol/compliance-requests/send-to-bank/{requestId}")
                    .post("/api/v2/sbbol/compliance-requests/send-to-bank/#{compliance_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC34_GET_v2_sbbol_compliance_requests_unread_posts_counter =
            http("UC34_GET_/api/v2/sbbol/compliance-requests/unread-posts-counter")
                    .get("/api/v2/sbbol/compliance-requests/unread-posts-counter")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/PostsCounterRqApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC35_GET_v2_sbbol_free_format_letters_detail_By_freeFormatLetterId =
            http("UC35_GET_/api/v2/sbbol/free-format-letters/detail/{freeFormatLetterId}")
                    .get("/api/v2/sbbol/free-format-letters/detail/#{free_format_letter_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC36_GET_v2_sbbol_free_format_letters_list =
            http("UC36_GET_/api/v2/sbbol/free-format-letters/list")
                    .get("/api/v2/sbbol/free-format-letters/list")
                    .queryParam("pagingRequest", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC37_POST_v2_sbbol_free_format_letters_send_to_bank_By_freeFormatLetterId =
            http("UC37_POST_/api/v2/sbbol/free-format-letters/send-to-bank/{freeFormatLetterId}")
                    .post("/api/v2/sbbol/free-format-letters/send-to-bank/#{free_format_letter_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC38_POST_v3_sbbol_compliance_requests_callback_detail =
            http("UC38_POST_/api/v3/sbbol/compliance-requests/callback/detail")
                    .post("/api/v3/sbbol/compliance-requests/callback/detail")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/CallbackDetailRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC39_POST_v3_sbbol_compliance_requests_detail =
            http("UC39_POST_/api/v3/sbbol/compliance-requests/detail")
                    .post("/api/v3/sbbol/compliance-requests/detail")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceRequestDetailRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC40_GET_v3_sbbol_compliance_requests_fin_operation_list =
            http("UC40_GET_/api/v3/sbbol/compliance-requests/fin-operation/list")
                    .get("/api/v3/sbbol/compliance-requests/fin-operation/list")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC41_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_case =
            http("UC41_GET_/api/v3/sbbol/compliance-requests/fin-operation/operation-by-case")
                    .get("/api/v3/sbbol/compliance-requests/fin-operation/operation-by-case")
                    .queryParam("caseId", "#{case_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC42_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_id =
            http("UC42_GET_/api/v3/sbbol/compliance-requests/fin-operation/operation-by-id")
                    .get("/api/v3/sbbol/compliance-requests/fin-operation/operation-by-id")
                    .queryParam("request", Methods.queryFromFile(JSONS_PATH + "shared/FinOperationByIdApiModel_request.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC43_GET_v3_sbbol_compliance_requests_fin_operation_operations_by_request =
            http("UC43_GET_/api/v3/sbbol/compliance-requests/fin-operation/operations-by-request")
                    .get("/api/v3/sbbol/compliance-requests/fin-operation/operations-by-request")
                    .queryParam("requestId", "#{compliance_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC44_POST_v4_sbbol_compliance_requests_detail =
            http("UC44_POST_/api/v4/sbbol/compliance-requests/detail")
                    .post("/api/v4/sbbol/compliance-requests/detail")
                    .body(ElFileBody(JSONS_PATH + "Sbbol/ComplianceRequestDetailRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC45_GET_v5_sbbol_compliance_requests_request_list =
            http("UC45_GET_/api/v5/sbbol/compliance-requests/request/list")
                    .get("/api/v5/sbbol/compliance-requests/request/list")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC46_GET_v7_sbbol_compliance_requests_request_list =
            http("UC46_GET_/api/v7/sbbol/compliance-requests/request/list")
                    .get("/api/v7/sbbol/compliance-requests/request/list")
                    .queryParam("pagingRequest", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
