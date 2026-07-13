package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class EmployeeNotificationsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_employee_notifications =
            http("UC01_GET_/api/v1/employee-notifications")
                    .get("/api/v1/employee-notifications")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_employee_notifications =
            http("UC02_POST_/api/v1/employee-notifications")
                    .post("/api/v1/employee-notifications")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotifications/EmployeeNotificationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_employee_notifications_batch_identical =
            http("UC03_PATCH_/api/v1/employee-notifications/batch-identical")
                    .patch("/api/v1/employee-notifications/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotifications/EmployeeNotificationApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_employee_notifications_By_id =
            http("UC04_DELETE_/api/v1/employee-notifications/{id}")
                    .delete("/api/v1/employee-notifications/#{employeeNotificationsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_employee_notifications_By_id =
            http("UC05_GET_/api/v1/employee-notifications/{id}")
                    .get("/api/v1/employee-notifications/#{employeeNotificationsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_employee_notifications_By_id =
            http("UC06_PATCH_/api/v1/employee-notifications/{id}")
                    .patch("/api/v1/employee-notifications/#{employeeNotificationsId}")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotifications/EmployeeNotificationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_employee_notifications_By_id =
            http("UC07_PUT_/api/v1/employee-notifications/{id}")
                    .put("/api/v1/employee-notifications/#{employeeNotificationsId}")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotifications/EmployeeNotificationApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
