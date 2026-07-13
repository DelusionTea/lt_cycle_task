package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class EventsHistoriesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_events_histories =
            http("UC01_GET_/api/v2/events-histories")
                    .get("/api/v2/events-histories")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_events_histories =
            http("UC02_POST_/api/v2/events-histories")
                    .post("/api/v2/events-histories")
                    .body(ElFileBody(JSONS_PATH + "EventsHistories/EventHistoryApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v2_events_histories_batch_identical =
            http("UC03_PATCH_/api/v2/events-histories/batch-identical")
                    .patch("/api/v2/events-histories/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "EventsHistories/EventHistoryApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v2_events_histories_By_id =
            http("UC04_DELETE_/api/v2/events-histories/{id}")
                    .delete("/api/v2/events-histories/#{eventsHistoriesId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v2_events_histories_By_id =
            http("UC05_GET_/api/v2/events-histories/{id}")
                    .get("/api/v2/events-histories/#{eventsHistoriesId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v2_events_histories_By_id =
            http("UC06_PATCH_/api/v2/events-histories/{id}")
                    .patch("/api/v2/events-histories/#{eventsHistoriesId}")
                    .body(ElFileBody(JSONS_PATH + "EventsHistories/EventHistoryApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v2_events_histories_By_id =
            http("UC07_PUT_/api/v2/events-histories/{id}")
                    .put("/api/v2/events-histories/#{eventsHistoriesId}")
                    .body(ElFileBody(JSONS_PATH + "EventsHistories/EventHistoryApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
