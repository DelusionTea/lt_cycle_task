package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class EcmCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_POST_v2_ecm_create_contents =
            http("UC01_POST_/api/v2/ecm/create-contents")
                    .post("/api/v2/ecm/create-contents")
                    .body(ElFileBody(JSONS_PATH + "Ecm/body_ad46838e.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_ecm_create_structure =
            http("UC02_POST_/api/v2/ecm/create-structure")
                    .post("/api/v2/ecm/create-structure")
                    .body(ElFileBody(JSONS_PATH + "Ecm/EcmCreateStructureApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_POST_v2_ecm_delete_objects =
            http("UC03_POST_/api/v2/ecm/delete-objects")
                    .post("/api/v2/ecm/delete-objects")
                    .body(ElFileBody(JSONS_PATH + "Ecm/body_9f11c40c.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
