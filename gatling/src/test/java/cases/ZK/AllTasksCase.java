package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AllTasksCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_all_tasks_opened_tasks_By_ucpId =
            http("UC01_GET_/api/v1/all-tasks/opened-tasks/{ucpId}")
                    .get("/api/v1/all-tasks/opened-tasks/#{allTasksUcpId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
