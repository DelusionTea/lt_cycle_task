package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReferenceValueLinksCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_reference_value_links =
            http("UC01_GET_/api/v2/reference-value-links")
                    .get("/api/v2/reference-value-links")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_reference_value_links =
            http("UC02_POST_/api/v2/reference-value-links")
                    .post("/api/v2/reference-value-links")
                    .body(ElFileBody(JSONS_PATH + "shared/ReferenceValueLinkApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v2_reference_value_links_batch_identical =
            http("UC03_PATCH_/api/v2/reference-value-links/batch-identical")
                    .patch("/api/v2/reference-value-links/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "shared/ReferenceValueLinkApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v2_reference_value_links_By_id =
            http("UC04_DELETE_/api/v2/reference-value-links/{id}")
                    .delete("/api/v2/reference-value-links/#{referenceValueLinksId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v2_reference_value_links_By_id =
            http("UC05_GET_/api/v2/reference-value-links/{id}")
                    .get("/api/v2/reference-value-links/#{referenceValueLinksId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v2_reference_value_links_By_id =
            http("UC06_PATCH_/api/v2/reference-value-links/{id}")
                    .patch("/api/v2/reference-value-links/#{referenceValueLinksId}")
                    .body(ElFileBody(JSONS_PATH + "shared/ReferenceValueLinkApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v2_reference_value_links_By_id =
            http("UC07_PUT_/api/v2/reference-value-links/{id}")
                    .put("/api/v2/reference-value-links/#{referenceValueLinksId}")
                    .body(ElFileBody(JSONS_PATH + "shared/ReferenceValueLinkApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
