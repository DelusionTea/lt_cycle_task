package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SystemPreferencesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_system_preferences =
            http("UC01_GET_/api/v2/system-preferences")
                    .get("/api/v2/system-preferences")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_system_preferences =
            http("UC02_POST_/api/v2/system-preferences")
                    .post("/api/v2/system-preferences")
                    .body(ElFileBody(JSONS_PATH + "shared/SystemPreferencesApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v2_system_preferences_batch_identical =
            http("UC03_PATCH_/api/v2/system-preferences/batch-identical")
                    .patch("/api/v2/system-preferences/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "shared/SystemPreferencesApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v2_system_preferences_By_id =
            http("UC04_DELETE_/api/v2/system-preferences/{id}")
                    .delete("/api/v2/system-preferences/#{sys_pref_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v2_system_preferences_By_id =
            http("UC05_GET_/api/v2/system-preferences/{id}")
                    .get("/api/v2/system-preferences/#{sys_pref_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v2_system_preferences_By_id =
            http("UC06_PATCH_/api/v2/system-preferences/{id}")
                    .patch("/api/v2/system-preferences/#{sys_pref_id}")
                    .body(ElFileBody(JSONS_PATH + "shared/SystemPreferencesApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v2_system_preferences_By_id =
            http("UC07_PUT_/api/v2/system-preferences/{id}")
                    .put("/api/v2/system-preferences/#{sys_pref_id}")
                    .body(ElFileBody(JSONS_PATH + "shared/SystemPreferencesApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
