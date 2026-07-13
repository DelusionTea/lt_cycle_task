package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CounterpartiesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_counterparties =
            http("UC01_GET_/api/v2/counterparties")
                    .get("/api/v2/counterparties")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_counterparties =
            http("UC02_POST_/api/v2/counterparties")
                    .post("/api/v2/counterparties")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/CounterpartyApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_DELETE_v2_counterparties_batch =
            http("UC03_DELETE_/api/v2/counterparties/batch")
                    .delete("/api/v2/counterparties/batch")
                    .queryParam("ids", "#{counterpartiesIds}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PATCH_v2_counterparties_batch =
            http("UC04_PATCH_/api/v2/counterparties/batch")
                    .patch("/api/v2/counterparties/batch")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/body_b3d1c7d8.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC05_POST_v2_counterparties_batch =
            http("UC05_POST_/api/v2/counterparties/batch")
                    .post("/api/v2/counterparties/batch")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/body_b3d1c7d8.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PUT_v2_counterparties_batch =
            http("UC06_PUT_/api/v2/counterparties/batch")
                    .put("/api/v2/counterparties/batch")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/body_b3d1c7d8.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PATCH_v2_counterparties_batch_identical =
            http("UC07_PATCH_/api/v2/counterparties/batch-identical")
                    .patch("/api/v2/counterparties/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/CounterpartyApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_DELETE_v2_counterparties_By_id =
            http("UC08_DELETE_/api/v2/counterparties/{id}")
                    .delete("/api/v2/counterparties/#{counterpartiesId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_GET_v2_counterparties_By_id =
            http("UC09_GET_/api/v2/counterparties/{id}")
                    .get("/api/v2/counterparties/#{counterpartiesId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_PATCH_v2_counterparties_By_id =
            http("UC10_PATCH_/api/v2/counterparties/{id}")
                    .patch("/api/v2/counterparties/#{counterpartiesId}")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/CounterpartyApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_PUT_v2_counterparties_By_id =
            http("UC11_PUT_/api/v2/counterparties/{id}")
                    .put("/api/v2/counterparties/#{counterpartiesId}")
                    .body(ElFileBody(JSONS_PATH + "Counterparties/CounterpartyApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
