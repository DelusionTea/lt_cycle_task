package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ProactiveOnboardingsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v1_proactive_onboardings =
            http("UC01_GET_/api/v1/proactive-onboardings")
                    .get("/api/v1/proactive-onboardings")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_proactive_onboardings =
            http("UC02_POST_/api/v1/proactive-onboardings")
                    .post("/api/v1/proactive-onboardings")
                    .body(ElFileBody(JSONS_PATH + "ProactiveOnboardings/ProactiveOnboardingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_proactive_onboardings_batch_identical =
            http("UC03_PATCH_/api/v1/proactive-onboardings/batch-identical")
                    .patch("/api/v1/proactive-onboardings/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "ProactiveOnboardings/ProactiveOnboardingApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_GET_v1_proactive_onboardings_find_list_extended =
            http("UC04_GET_/api/v1/proactive-onboardings/find-list-extended")
                    .get("/api/v1/proactive-onboardings/find-list-extended")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExtExtended_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_proactive_onboardings_get_counters =
            http("UC05_GET_/api/v1/proactive-onboardings/get-counters")
                    .get("/api/v1/proactive-onboardings/get-counters")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_DELETE_v1_proactive_onboardings_By_id =
            http("UC06_DELETE_/api/v1/proactive-onboardings/{id}")
                    .delete("/api/v1/proactive-onboardings/#{proactiveOnboardingsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_GET_v1_proactive_onboardings_By_id =
            http("UC07_GET_/api/v1/proactive-onboardings/{id}")
                    .get("/api/v1/proactive-onboardings/#{proactiveOnboardingsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_PATCH_v1_proactive_onboardings_By_id =
            http("UC08_PATCH_/api/v1/proactive-onboardings/{id}")
                    .patch("/api/v1/proactive-onboardings/#{proactiveOnboardingsId}")
                    .body(ElFileBody(JSONS_PATH + "ProactiveOnboardings/ProactiveOnboardingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_PUT_v1_proactive_onboardings_By_id =
            http("UC09_PUT_/api/v1/proactive-onboardings/{id}")
                    .put("/api/v1/proactive-onboardings/#{proactiveOnboardingsId}")
                    .body(ElFileBody(JSONS_PATH + "ProactiveOnboardings/ProactiveOnboardingApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
