package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AdClientMarkingsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_ad_client_markings =
            http("UC01_GET_/api/v1/ad-client-markings")
                    .get("/api/v1/ad-client-markings")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_ad_client_markings =
            http("UC02_POST_/api/v1/ad-client-markings")
                    .post("/api/v1/ad-client-markings")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/AdClientMarkingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_ad_client_markings_batch_identical =
            http("UC03_PATCH_/api/v1/ad-client-markings/batch-identical")
                    .patch("/api/v1/ad-client-markings/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/AdClientMarkingApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_ad_client_markings_By_id =
            http("UC04_DELETE_/api/v1/ad-client-markings/{id}")
                    .delete("/api/v1/ad-client-markings/#{marking_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_ad_client_markings_By_id =
            http("UC05_GET_/api/v1/ad-client-markings/{id}")
                    .get("/api/v1/ad-client-markings/#{marking_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_ad_client_markings_By_id =
            http("UC06_PATCH_/api/v1/ad-client-markings/{id}")
                    .patch("/api/v1/ad-client-markings/#{marking_id}")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/AdClientMarkingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_ad_client_markings_By_id =
            http("UC07_PUT_/api/v1/ad-client-markings/{id}")
                    .put("/api/v1/ad-client-markings/#{marking_id}")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/AdClientMarkingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_DELETE_v2_ad_client_markings_batch =
            http("UC08_DELETE_/api/v2/ad-client-markings/batch")
                    .delete("/api/v2/ad-client-markings/batch")
                    .queryParam("ids", "#{marking_ids}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_PATCH_v2_ad_client_markings_batch =
            http("UC09_PATCH_/api/v2/ad-client-markings/batch")
                    .patch("/api/v2/ad-client-markings/batch")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/body_e71cd5f2.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_POST_v2_ad_client_markings_batch =
            http("UC10_POST_/api/v2/ad-client-markings/batch")
                    .post("/api/v2/ad-client-markings/batch")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/body_e71cd5f2.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_PUT_v2_ad_client_markings_batch =
            http("UC11_PUT_/api/v2/ad-client-markings/batch")
                    .put("/api/v2/ad-client-markings/batch")
                    .body(ElFileBody(JSONS_PATH + "AdClientMarkings/body_e71cd5f2.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
