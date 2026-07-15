package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class FinOperationsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v3_fin_operations =
            http("UC01_GET_/api/v3/fin-operations")
                    .get("/api/v3/fin-operations")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_GET_v3_fin_operations_By_id =
            http("UC02_GET_/api/v3/fin-operations/{id}")
                    .get("/api/v3/fin-operations/#{operation_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
