package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class InfrastructuresCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_infrastructures =
            http("UC01_GET_/api/v2/infrastructures")
                    .get("/api/v2/infrastructures")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_POST_v2_infrastructures =
            http("UC02_POST_/api/v2/infrastructures")
                    .post("/api/v2/infrastructures")
                    .body(ElFileBody(JSONS_PATH + "shared/InfrastructureApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_DELETE_v2_infrastructures_batch =
            http("UC03_DELETE_/api/v2/infrastructures/batch")
                    .delete("/api/v2/infrastructures/batch")
                    .queryParam("ids", "#{infrastructuresIds}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PATCH_v2_infrastructures_batch =
            http("UC04_PATCH_/api/v2/infrastructures/batch")
                    .patch("/api/v2/infrastructures/batch")
                    .body(ElFileBody(JSONS_PATH + "shared/body_5501b59c.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC05_POST_v2_infrastructures_batch =
            http("UC05_POST_/api/v2/infrastructures/batch")
                    .post("/api/v2/infrastructures/batch")
                    .body(ElFileBody(JSONS_PATH + "shared/body_5501b59c.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PUT_v2_infrastructures_batch =
            http("UC06_PUT_/api/v2/infrastructures/batch")
                    .put("/api/v2/infrastructures/batch")
                    .body(ElFileBody(JSONS_PATH + "shared/body_5501b59c.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PATCH_v2_infrastructures_batch_identical =
            http("UC07_PATCH_/api/v2/infrastructures/batch-identical")
                    .patch("/api/v2/infrastructures/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "shared/InfrastructureApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_DELETE_v2_infrastructures_By_id =
            http("UC08_DELETE_/api/v2/infrastructures/{id}")
                    .delete("/api/v2/infrastructures/#{infrastructuresId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_GET_v2_infrastructures_By_id =
            http("UC09_GET_/api/v2/infrastructures/{id}")
                    .get("/api/v2/infrastructures/#{infrastructuresId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_PATCH_v2_infrastructures_By_id =
            http("UC10_PATCH_/api/v2/infrastructures/{id}")
                    .patch("/api/v2/infrastructures/#{infrastructuresId}")
                    .body(ElFileBody(JSONS_PATH + "shared/InfrastructureApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_PUT_v2_infrastructures_By_id =
            http("UC11_PUT_/api/v2/infrastructures/{id}")
                    .put("/api/v2/infrastructures/#{infrastructuresId}")
                    .body(ElFileBody(JSONS_PATH + "shared/InfrastructureApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
