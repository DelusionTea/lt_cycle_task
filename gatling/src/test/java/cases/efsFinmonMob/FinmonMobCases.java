package cases.efsFinmonMob;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.regex;
import static io.gatling.javaapi.http.HttpDsl.*;

public class FinmonMobCases {
    public static String jsonPath = "JSONs/efsFinmonMob/";

    public static HttpRequestActionBuilder UC01_POST_UPLOAD_V1 =
            http("UC01_POST_/upload1")
                    .post("/ufs/mobile/compliance/v1/files/upload")
                    .header("Content-Type", "multipart/form-data; boundary=---------------------------7d025e2b16b064e")
//                    .body(ElFileBody(jsonPath + "UC01.json"))
                    .bodyPart(StringBodyPart("requestId", "d62826d1-7aa0-416f-b549-68d55aa59c8b"))
                    .bodyPart(RawFileBodyPart("file", jsonPath + "test.finmonmobxls1.xlsx"))
                    .asMultipartForm()
                    .header("csrftoken", "#{csrftoken}")
                    .header("Authorization","{sessionId}")
                    .header("User-Agent", "SBBOL for iPhone OS v3.55")
                    .check(status().is(200));
@Deprecated
    public static HttpRequestActionBuilder UC02_POST_request_list =
            http("UC02_POST_/list")
                    .post("/ufs/mobile/compliance/v1/request/list")
                    .body(ElFileBody(jsonPath + "UC02.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC03_GET_request_online_pprbId =
            http("UC03_GET_/online/{pprbId}")
                    .get("/ufs/mobile/compliance/v1/request/online/#{pprbId}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC04_GET_rejections_pprbId =
            http("UC04_GET_/rejections/{pprbId}")
                    .get("/ufs/mobile/compliance/v1/rejections/#{pprbId}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC05_POST_rejections_list =
            http("UC05_POST_/list")
                    .post("/ufs/mobile/compliance/v1/rejections/list")
                    .body(ElFileBody(jsonPath + "UC05.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC06_GET_REQUEST_V2 =
            http("UC06_GET_/v2/request")
                    .get("/ufs/mobile/compliance/v2/request")
                    .queryParam("pprbId", "#{pprbId}")
                    .queryParam("crmId", "#{crmId}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));
    @Deprecated
    public static HttpRequestActionBuilder UC07_GET_request_state =
            http("UC07_GET_/state")
                    .get("/ufs/mobile/compliance/v1/request/state")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC08_GET_files_params =
            http("UC08_GET_/params")
                    .get("/ufs/mobile/compliance/v1/files/params")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC09_GET_v1_support =
            http("UC09_GET_/support")
                    .get("/ufs/mobile/compliance/v1/support")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC10_GET_print =
            http("UC10_GET_/{pprbId}/print")
                    .get("/ufs/mobile/compliance/v1/request/#{pprbId}/print")
                    .queryParam("printType", "REQUEST")
                    .queryParam("requestType", "MOP1")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC11_GET_ffl_id =
            http("UC11_GET_/free-format-letter/{pprbId}")
                    .get("/ufs/mobile/compliance/v1/free-format-letter/#{pprbId}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC12_GET_download_ecmId =
            http("UC12_GET_/download/{ecmId}")
                    .get("/ufs/mobile/compliance/v1/free-format-letter/download/%7B20E60934-56BB-492E-8CB0-333DD3256E54%7D")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC13_GET_informing_id =
            http("UC13_GET_/informing/{pprbId}")
                    .get("/ufs/mobile/compliance/v1/informing/#{pprbId}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));
@Deprecated
    public static HttpRequestActionBuilder UC14_POST_informing_list =
            http("UC14_POST_/list")
                    .post("/ufs/mobile/compliance/v1/informing/list")
                    .body(ElFileBody(jsonPath + "UC14.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC15_GET_v2_request_state =
            http("UC15_GET_/state")
                    .get("/ufs/mobile/compliance/v2/request/state")
                    .queryParam("type", "MOP 1.0")
                    .queryParam("type", "Online control")
                    .queryParam("type", "FREE_FORMAT_LETTER")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC16_POST_v2_request_list =
            http("UC16_POST_/list")
                    .post("/ufs/mobile/compliance/v2/request/list")
                    .body(ElFileBody(jsonPath + "UC16.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC17_GET_v2_rejections_uuid =
            http("UC17_GET_/v2/rejections/{uuid}")
                    .get("/ufs/mobile/compliance/v2/rejections/#{uuid}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC18_GET_v1_rejections_startRehabilitation_uuid =
            http("UC18_GET_/startRehabilitatio/{uuid}")
                    .get("/ufs/mobile/compliance/v1/rejections/startRehabilitation/#{uuid}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC19_GET_v1_request_rehabilitation =
            http("UC19_GET_/rehabilitation")
                    .get("/ufs/mobile/compliance/v1/request/rehabilitation")
                    .queryParam("pprbId", "#{uuid}")
                    .queryParam("caseId", "#{uuid}")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC20_GET_compliance_profile_widget =
            http("UC20_GET_/widget")
                    .get("/ufs/mobile/compliance/v1/compliance-profile/widget")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC21_GET_compliance_profile_schemeBusiness =
            http("UC21_GET_/schemeBusiness")
                    .get("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC22_GET_compliance_profile_schemeBusiness_partners =
            http("UC22_GET_/partners")
                    .get("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/partners")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC23_GET_compliance_profile_schemeBusiness_mtb =
            http("UC23_GET_/mtb")
                    .get("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/mtb")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC24_DELETE_compliance_profile_schemeBusiness_detail_alert =
            http("UC24_DELETE_/alert")
                    .delete("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/detail/alert")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(204));

    public static HttpRequestActionBuilder UC25_POST_compliance_profile_schemeBusiness_detail =
            http("UC25_POST_/detail")
                    .post("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/detail")
                    .body(ElFileBody(jsonPath + "UC25.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC26_POST_compliance_profile_schemeBusiness_save_partners =
            http("UC26_POST_/partners")
                    .post("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/partners")
                    .body(ElFileBody(jsonPath + "UC26.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC27_POST_compliance_profile_schemeBusiness_save_mtb =
            http("UC27_POST_/mtb")
                    .post("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/mtb")
                    .body(ElFileBody(jsonPath + "UC27.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC28_POST_compliance_profile_schemeBusiness_save_other =
            http("UC28_POST_/other")
                    .post("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/other")
                    .body(ElFileBody(jsonPath + "UC28.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC29_POST_compliance_profile_schemeBusiness_save_aboutCompany =
            http("UC29_POST_/aboutCompany")
                    .post("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/aboutCompany")
                    .body(ElFileBody(jsonPath + "UC29.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC30_POST_compliance_profile_schemeBusiness_save_contacts =
            http("UC30_POST_/contacts")
                    .post("/ufs/mobile/compliance/v1/compliance-profile/schemeBusiness/save/contacts")
                    .body(ElFileBody(jsonPath + "UC30.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC31_GET_proactive_mop2_pprbId =
            http("UC31_GET_/mop2/{pprbId}")
                    .get("/ufs/mobile/compliance/v1/proactive/mop2/#{pprbId}")
                    .header("csrftoken", "#{csrftoken}")
                    .header("User-Agent", "SBBOL for iPhone OS v3.55")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC32_GET_proactive_onboarding_pprbId =
            http("UC32_GET_/onboarding/{pprbId}")
                    .get("/ufs/mobile/compliance/v1/proactive/onboarding/#{pprbId}")
                    .header("csrftoken", "#{csrftoken}")
                    .header("User-Agent", "SBBOL for iPhone OS v3.55")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC33_POST_informing_list_v2 =
            http("UC33_POST_v2/list")
                    .post("/ufs/mobile/compliance/v2/informing/list")
                    .body(ElFileBody(jsonPath + "UC14.json"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("csrftoken", "#{csrftoken}")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC34_POST_request_UUID_reply =
            http("UC34_POST_/request/{pprbId}/reply")
                    .post("/ufs/mobile/compliance/v2/request/#{uuid}/reply")
                    .body(ElFileBody(jsonPath + "UC31.json"))
                    .header("csrftoken", "#{csrftoken}")
                    .header("User-Agent", "SBBOL for iPhone OS v3.55")
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC35_POST_request_UUID_saveAttachments =
            http("UC35_POST_/request/[UUID]/saveAttachments")
                    .post("/ufs/mobile/compliance/v2/request/#{uuid}/saveAttachments")
                    .body(ElFileBody(jsonPath + "UC34.json"))
                    .header("csrftoken", "#{csrftoken}")
                    .header("User-Agent", "SBBOL for iPhone OS v3.55")
                    .check(status().is(200));
}
