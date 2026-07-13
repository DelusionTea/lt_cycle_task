package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ComplianceChecklistsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_POST_v2_compliance_checklists_comments =
            http("UC01_POST_/api/v2/compliance-checklists/comments")
                    .post("/api/v2/compliance-checklists/comments")
                    .body(ElFileBody(JSONS_PATH + "ComplianceChecklists/CreateChecklistComment_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_PATCH_v2_compliance_checklists_comments_By_id =
            http("UC02_PATCH_/api/v2/compliance-checklists/comments/{id}")
                    .patch("/api/v2/compliance-checklists/comments/#{complianceChecklistsId}")
                    .body(ElFileBody(JSONS_PATH + "shared/UpdateChecklistComment_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC03_GET_v2_compliance_checklists_By_requestId =
            http("UC03_GET_/api/v2/compliance-checklists/{requestId}")
                    .get("/api/v2/compliance-checklists/#{complianceChecklistsRequestId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
