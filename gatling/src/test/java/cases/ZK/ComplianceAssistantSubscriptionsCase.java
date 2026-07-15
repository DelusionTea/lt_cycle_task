package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ComplianceAssistantSubscriptionsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v1_compliance_assistant_subscriptions =
            http("UC01_GET_/api/v1/compliance-assistant-subscriptions")
                    .get("/api/v1/compliance-assistant-subscriptions")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_POST_v1_compliance_assistant_subscriptions =
            http("UC02_POST_/api/v1/compliance-assistant-subscriptions")
                    .post("/api/v1/compliance-assistant-subscriptions")
                    .body(ElFileBody(JSONS_PATH + "ComplianceAssistantSubscriptions/ComplianceAssistantSubscriptionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_compliance_assistant_subscriptions_batch_identical =
            http("UC03_PATCH_/api/v1/compliance-assistant-subscriptions/batch-identical")
                    .patch("/api/v1/compliance-assistant-subscriptions/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "ComplianceAssistantSubscriptions/ComplianceAssistantSubscriptionApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_compliance_assistant_subscriptions_By_id =
            http("UC04_DELETE_/api/v1/compliance-assistant-subscriptions/{id}")
                    .delete("/api/v1/compliance-assistant-subscriptions/#{subscription_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_compliance_assistant_subscriptions_By_id =
            http("UC05_GET_/api/v1/compliance-assistant-subscriptions/{id}")
                    .get("/api/v1/compliance-assistant-subscriptions/#{subscription_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_compliance_assistant_subscriptions_By_id =
            http("UC06_PATCH_/api/v1/compliance-assistant-subscriptions/{id}")
                    .patch("/api/v1/compliance-assistant-subscriptions/#{subscription_id}")
                    .body(ElFileBody(JSONS_PATH + "ComplianceAssistantSubscriptions/ComplianceAssistantSubscriptionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_compliance_assistant_subscriptions_By_id =
            http("UC07_PUT_/api/v1/compliance-assistant-subscriptions/{id}")
                    .put("/api/v1/compliance-assistant-subscriptions/#{subscription_id}")
                    .body(ElFileBody(JSONS_PATH + "ComplianceAssistantSubscriptions/ComplianceAssistantSubscriptionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
