package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CkrCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_POST_v2_ckr_get_client_info =
            http("UC01_POST_/api/v2/ckr/get-client-info")
                    .post("/api/v2/ckr/get-client-info")
                    .body(ElFileBody(JSONS_PATH + "Ckr/CkrGetClientInfoRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_POST_v2_ckr_get_request_info =
            http("UC02_POST_/api/v2/ckr/get-request-info")
                    .post("/api/v2/ckr/get-request-info")
                    .body(ElFileBody(JSONS_PATH + "Ckr/CkrGetRequestInfoRqApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
