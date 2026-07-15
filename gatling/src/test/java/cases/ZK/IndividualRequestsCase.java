package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class IndividualRequestsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_individual_requests =
            http("UC01_GET_/api/v2/individual-requests")
                    .get("/api/v2/individual-requests")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_individual_requests =
            http("UC02_POST_/api/v2/individual-requests")
                    .post("/api/v2/individual-requests")
                    .body(ElFileBody(JSONS_PATH + "IndividualRequests/IndividualRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v2_individual_requests_batch_identical =
            http("UC03_PATCH_/api/v2/individual-requests/batch-identical")
                    .patch("/api/v2/individual-requests/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "IndividualRequests/IndividualRequestApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_GET_v2_individual_requests_filter =
            http("UC04_GET_/api/v2/individual-requests/filter")
                    .get("/api/v2/individual-requests/filter")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v2_individual_requests_get_individual_request_for_mmb =
            http("UC05_GET_/api/v2/individual-requests/get-individual-request-for-mmb")
                    .get("/api/v2/individual-requests/get-individual-request-for-mmb")
                    .queryParam("ucpId", "#{ucp_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PUT_v2_individual_requests_process_sfl_ties_By_individualRequestId =
            http("UC06_PUT_/api/v2/individual-requests/process-sfl-ties/{individualRequestId}")
                    .put("/api/v2/individual-requests/process-sfl-ties/#{individual_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_GET_v2_individual_requests_with_task =
            http("UC07_GET_/api/v2/individual-requests/with-task")
                    .get("/api/v2/individual-requests/with-task")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExtExtended_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_DELETE_v2_individual_requests_By_id =
            http("UC08_DELETE_/api/v2/individual-requests/{id}")
                    .delete("/api/v2/individual-requests/#{individual_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_GET_v2_individual_requests_By_id =
            http("UC09_GET_/api/v2/individual-requests/{id}")
                    .get("/api/v2/individual-requests/#{individual_request_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_PATCH_v2_individual_requests_By_id =
            http("UC10_PATCH_/api/v2/individual-requests/{id}")
                    .patch("/api/v2/individual-requests/#{individual_request_id}")
                    .body(ElFileBody(JSONS_PATH + "IndividualRequests/IndividualRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_PUT_v2_individual_requests_By_id =
            http("UC11_PUT_/api/v2/individual-requests/{id}")
                    .put("/api/v2/individual-requests/#{individual_request_id}")
                    .body(ElFileBody(JSONS_PATH + "IndividualRequests/IndividualRequestApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC12_PUT_v2_individual_requests_By_individualRequestId_assign =
            http("UC12_PUT_/api/v2/individual-requests/{individualRequestId}/assign")
                    .put("/api/v2/individual-requests/#{individual_request_id}/assign")
                    .body(ElFileBody(JSONS_PATH + "IndividualRequests/IndividualRequestAssignApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
