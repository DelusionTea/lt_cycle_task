package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ProactiveAttributesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v1_proactive_attributes =
            http("UC01_GET_/api/v1/proactive-attributes")
                    .get("/api/v1/proactive-attributes")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_proactive_attributes =
            http("UC02_POST_/api/v1/proactive-attributes")
                    .post("/api/v1/proactive-attributes")
                    .body(ElFileBody(JSONS_PATH + "ProactiveAttributes/ProactiveAttributesApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_proactive_attributes_batch_identical =
            http("UC03_PATCH_/api/v1/proactive-attributes/batch-identical")
                    .patch("/api/v1/proactive-attributes/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "ProactiveAttributes/ProactiveAttributesApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_proactive_attributes_By_id =
            http("UC04_DELETE_/api/v1/proactive-attributes/{id}")
                    .delete("/api/v1/proactive-attributes/#{attributes_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_proactive_attributes_By_id =
            http("UC05_GET_/api/v1/proactive-attributes/{id}")
                    .get("/api/v1/proactive-attributes/#{attributes_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_proactive_attributes_By_id =
            http("UC06_PATCH_/api/v1/proactive-attributes/{id}")
                    .patch("/api/v1/proactive-attributes/#{attributes_id}")
                    .body(ElFileBody(JSONS_PATH + "ProactiveAttributes/ProactiveAttributesApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_proactive_attributes_By_id =
            http("UC07_PUT_/api/v1/proactive-attributes/{id}")
                    .put("/api/v1/proactive-attributes/#{attributes_id}")
                    .body(ElFileBody(JSONS_PATH + "ProactiveAttributes/ProactiveAttributesApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
