package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class FileLoadingHistoryCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v4_file_loading_history =
            http("UC01_GET_/api/v4/file-loading-history")
                    .get("/api/v4/file-loading-history")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v4_file_loading_history =
            http("UC02_POST_/api/v4/file-loading-history")
                    .post("/api/v4/file-loading-history")
                    .body(ElFileBody(JSONS_PATH + "FileLoadingHistory/FileLoadingHistoryApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v4_file_loading_history_batch_identical =
            http("UC03_PATCH_/api/v4/file-loading-history/batch-identical")
                    .patch("/api/v4/file-loading-history/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "FileLoadingHistory/FileLoadingHistoryApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v4_file_loading_history_By_id =
            http("UC04_DELETE_/api/v4/file-loading-history/{id}")
                    .delete("/api/v4/file-loading-history/#{history_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC05_GET_v4_file_loading_history_By_id =
            http("UC05_GET_/api/v4/file-loading-history/{id}")
                    .get("/api/v4/file-loading-history/#{history_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v4_file_loading_history_By_id =
            http("UC06_PATCH_/api/v4/file-loading-history/{id}")
                    .patch("/api/v4/file-loading-history/#{history_id}")
                    .body(ElFileBody(JSONS_PATH + "FileLoadingHistory/FileLoadingHistoryApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v4_file_loading_history_By_id =
            http("UC07_PUT_/api/v4/file-loading-history/{id}")
                    .put("/api/v4/file-loading-history/#{history_id}")
                    .body(ElFileBody(JSONS_PATH + "FileLoadingHistory/FileLoadingHistoryApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
