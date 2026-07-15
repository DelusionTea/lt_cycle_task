package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RehabilitationBufferRequestsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_rehabilitation_buffer_requests =
            http("UC01_GET_/api/v2/rehabilitation-buffer-requests")
                    .get("/api/v2/rehabilitation-buffer-requests")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_rehabilitation_buffer_requests =
            http("UC02_POST_/api/v2/rehabilitation-buffer-requests")
                    .post("/api/v2/rehabilitation-buffer-requests")
                    .body(ElFileBody(JSONS_PATH + "RehabilitationBufferRequests/RehabilitationBufferRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v2_rehabilitation_buffer_requests_batch_identical =
            http("UC03_PATCH_/api/v2/rehabilitation-buffer-requests/batch-identical")
                    .patch("/api/v2/rehabilitation-buffer-requests/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "RehabilitationBufferRequests/RehabilitationBufferRequestApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_POST_v2_rehabilitation_buffer_requests_process_rehabilitation_request_By_id =
            http("UC04_POST_/api/v2/rehabilitation-buffer-requests/process-rehabilitation-request/{id}")
                    .post("/api/v2/rehabilitation-buffer-requests/process-rehabilitation-request/#{request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_DELETE_v2_rehabilitation_buffer_requests_By_id =
            http("UC05_DELETE_/api/v2/rehabilitation-buffer-requests/{id}")
                    .delete("/api/v2/rehabilitation-buffer-requests/#{request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_GET_v2_rehabilitation_buffer_requests_By_id =
            http("UC06_GET_/api/v2/rehabilitation-buffer-requests/{id}")
                    .get("/api/v2/rehabilitation-buffer-requests/#{request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PATCH_v2_rehabilitation_buffer_requests_By_id =
            http("UC07_PATCH_/api/v2/rehabilitation-buffer-requests/{id}")
                    .patch("/api/v2/rehabilitation-buffer-requests/#{request_id}")
                    .body(ElFileBody(JSONS_PATH + "RehabilitationBufferRequests/RehabilitationBufferRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_PUT_v2_rehabilitation_buffer_requests_By_id =
            http("UC08_PUT_/api/v2/rehabilitation-buffer-requests/{id}")
                    .put("/api/v2/rehabilitation-buffer-requests/#{request_id}")
                    .body(ElFileBody(JSONS_PATH + "RehabilitationBufferRequests/RehabilitationBufferRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
