package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ComplianceRequestsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v3_compliance_requests =
            http("UC01_GET_/api/v3/compliance-requests")
                    .get("/api/v3/compliance-requests")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/GetListCmplRequestsRqFilter_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_GET_v3_compliance_requests_counters =
            http("UC02_GET_/api/v3/compliance-requests/counters")
                    .get("/api/v3/compliance-requests/counters")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/GetCountersRqFilter_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PUT_v3_compliance_requests_By_id_assign =
            http("UC03_PUT_/api/v3/compliance-requests/{id}/assign")
                    .put("/api/v3/compliance-requests/#{compliance_request_id}/assign")
                    .body(ElFileBody(JSONS_PATH + "ComplianceRequests/ComplianceRequestAssign_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PUT_v3_compliance_requests_By_id_close =
            http("UC04_PUT_/api/v3/compliance-requests/{id}/close")
                    .put("/api/v3/compliance-requests/#{compliance_request_id}/close")
                    .body(ElFileBody(JSONS_PATH + "shared/ComplianceRequestClose_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v3_compliance_requests_By_id_detail =
            http("UC05_GET_/api/v3/compliance-requests/{id}/detail")
                    .get("/api/v3/compliance-requests/#{compliance_request_id}/detail")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PUT_v3_compliance_requests_By_id_from_to_bordo =
            http("UC06_PUT_/api/v3/compliance-requests/{id}/from-to-bordo")
                    .put("/api/v3/compliance-requests/#{compliance_request_id}/from-to-bordo")
                    .body(ElFileBody(JSONS_PATH + "shared/ComplianceRequestFromToBordo_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_GET_v3_compliance_requests_By_id_preview =
            http("UC07_GET_/api/v3/compliance-requests/{id}/preview")
                    .get("/api/v3/compliance-requests/#{compliance_request_id}/preview")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
