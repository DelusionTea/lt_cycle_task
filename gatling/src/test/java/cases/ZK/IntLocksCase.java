package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class IntLocksCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_DELETE_v2_int_locks =
            http("UC01_DELETE_/api/v2/int-locks")
                    .delete("/api/v2/int-locks")
                    .queryParam("region", "#{intLocksRegion}")
                    .queryParam("lockKey", "#{intLocksLockKey}")
                    .queryParam("clientId", "#{intLocksClientId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_PATCH_v2_int_locks =
            http("UC02_PATCH_/api/v2/int-locks")
                    .patch("/api/v2/int-locks")
                    .body(ElFileBody(JSONS_PATH + "IntLocks/IntLockApiModel_body.json"))
                    .queryParam("region", "#{intLocksRegion}")
                    .queryParam("lockKey", "#{intLocksLockKey}")
                    .queryParam("clientId", "#{intLocksClientId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC03_POST_v2_int_locks =
            http("UC03_POST_/api/v2/int-locks")
                    .post("/api/v2/int-locks")
                    .body(ElFileBody(JSONS_PATH + "IntLocks/IntLockApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC04_PUT_v2_int_locks =
            http("UC04_PUT_/api/v2/int-locks")
                    .put("/api/v2/int-locks")
                    .body(ElFileBody(JSONS_PATH + "IntLocks/IntLockApiModel_body.json"))
                    .queryParam("region", "#{intLocksRegion}")
                    .queryParam("lockKey", "#{intLocksLockKey}")
                    .queryParam("clientId", "#{intLocksClientId}")
                    .queryParam("createdDate", "#{intLocksCreatedDate}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_DELETE_v2_int_locks_all =
            http("UC05_DELETE_/api/v2/int-locks/all")
                    .delete("/api/v2/int-locks/all")
                    .queryParam("region", "#{intLocksRegion}")
                    .queryParam("clientId", "#{intLocksClientId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_GET_v2_int_locks_count =
            http("UC06_GET_/api/v2/int-locks/count")
                    .get("/api/v2/int-locks/count")
                    .queryParam("region", "#{intLocksRegion}")
                    .queryParam("lockKey", "#{intLocksLockKey}")
                    .queryParam("clientId", "#{intLocksClientId}")
                    .queryParam("createdDate", "#{intLocksCreatedDate}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_DELETE_v2_int_locks_expired =
            http("UC07_DELETE_/api/v2/int-locks/expired")
                    .delete("/api/v2/int-locks/expired")
                    .queryParam("region", "#{intLocksRegion}")
                    .queryParam("createdDate", "#{intLocksCreatedDate}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
