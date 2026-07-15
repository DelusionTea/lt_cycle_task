package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DigitalUserCompliancesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_digital_user_compliances =
            http("UC01_GET_/api/v2/digital-user-compliances")
                    .get("/api/v2/digital-user-compliances")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_digital_user_compliances =
            http("UC02_POST_/api/v2/digital-user-compliances")
                    .post("/api/v2/digital-user-compliances")
                    .body(ElFileBody(JSONS_PATH + "DigitalUserCompliances/DigitalUserComplianceApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC03_PATCH_v2_digital_user_compliances_batch_identical =
            http("UC03_PATCH_/api/v2/digital-user-compliances/batch-identical")
                    .patch("/api/v2/digital-user-compliances/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "DigitalUserCompliances/DigitalUserComplianceApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v2_digital_user_compliances_By_id =
            http("UC04_DELETE_/api/v2/digital-user-compliances/{id}")
                    .delete("/api/v2/digital-user-compliances/#{compliance_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v2_digital_user_compliances_By_id =
            http("UC05_GET_/api/v2/digital-user-compliances/{id}")
                    .get("/api/v2/digital-user-compliances/#{compliance_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC06_PATCH_v2_digital_user_compliances_By_id =
            http("UC06_PATCH_/api/v2/digital-user-compliances/{id}")
                    .patch("/api/v2/digital-user-compliances/#{compliance_id}")
                    .body(ElFileBody(JSONS_PATH + "DigitalUserCompliances/DigitalUserComplianceApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v2_digital_user_compliances_By_id =
            http("UC07_PUT_/api/v2/digital-user-compliances/{id}")
                    .put("/api/v2/digital-user-compliances/#{compliance_id}")
                    .body(ElFileBody(JSONS_PATH + "DigitalUserCompliances/DigitalUserComplianceApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
