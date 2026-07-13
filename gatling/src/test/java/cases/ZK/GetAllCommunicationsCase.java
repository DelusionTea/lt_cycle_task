package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetAllCommunicationsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_get_all_communications =
            http("UC01_GET_/api/v1/get-all-communications")
                    .get("/api/v1/get-all-communications")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExtExtended_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
