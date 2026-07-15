package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ComplianceTasksCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_compliance_tasks_counters =
            http("UC01_GET_/api/v2/compliance-tasks/counters")
                    .get("/api/v2/compliance-tasks/counters")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/TaskCountersRqApiModel_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v2_compliance_tasks_documents_checks =
            http("UC02_POST_/api/v2/compliance-tasks/documents-checks")
                    .post("/api/v2/compliance-tasks/documents-checks")
                    .body(ElFileBody(JSONS_PATH + "ComplianceTasks/CreateDocCheckTaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PUT_v2_compliance_tasks_By_id_close_call =
            http("UC03_PUT_/api/v2/compliance-tasks/{id}/close-call")
                    .put("/api/v2/compliance-tasks/#{taskid}/close-call")
                    .body(ElFileBody(JSONS_PATH + "shared/CloseTaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PUT_v2_compliance_tasks_By_id_close_documents_check =
            http("UC04_PUT_/api/v2/compliance-tasks/{id}/close-documents-check")
                    .put("/api/v2/compliance-tasks/#{taskid}/close-documents-check")
                    .body(ElFileBody(JSONS_PATH + "shared/CloseTaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
