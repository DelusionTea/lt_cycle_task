package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReportsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_reports =
            http("UC01_GET_/api/v1/reports")
                    .get("/api/v1/reports")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_reports =
            http("UC02_POST_/api/v1/reports")
                    .post("/api/v1/reports")
                    .body(ElFileBody(JSONS_PATH + "Reports/ReportApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_reports_batch_identical =
            http("UC03_PATCH_/api/v1/reports/batch-identical")
                    .patch("/api/v1/reports/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "Reports/ReportApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_reports_By_id =
            http("UC04_DELETE_/api/v1/reports/{id}")
                    .delete("/api/v1/reports/#{reportsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_reports_By_id =
            http("UC05_GET_/api/v1/reports/{id}")
                    .get("/api/v1/reports/#{reportsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_reports_By_id =
            http("UC06_PATCH_/api/v1/reports/{id}")
                    .patch("/api/v1/reports/#{reportsId}")
                    .body(ElFileBody(JSONS_PATH + "Reports/ReportApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_reports_By_id =
            http("UC07_PUT_/api/v1/reports/{id}")
                    .put("/api/v1/reports/#{reportsId}")
                    .body(ElFileBody(JSONS_PATH + "Reports/ReportApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
