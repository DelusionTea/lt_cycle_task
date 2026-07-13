package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class EmployeeNotificationWhiteListsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_employee_notification_white_lists =
            http("UC01_GET_/api/v1/employee-notification-white-lists")
                    .get("/api/v1/employee-notification-white-lists")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_employee_notification_white_lists =
            http("UC02_POST_/api/v1/employee-notification-white-lists")
                    .post("/api/v1/employee-notification-white-lists")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotificationWhiteLists/EmployeeNotificationWhiteListApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_employee_notification_white_lists_batch_identical =
            http("UC03_PATCH_/api/v1/employee-notification-white-lists/batch-identical")
                    .patch("/api/v1/employee-notification-white-lists/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotificationWhiteLists/EmployeeNotificationWhiteListApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_employee_notification_white_lists_By_id =
            http("UC04_DELETE_/api/v1/employee-notification-white-lists/{id}")
                    .delete("/api/v1/employee-notification-white-lists/#{employeeNotificationWhiteListsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_employee_notification_white_lists_By_id =
            http("UC05_GET_/api/v1/employee-notification-white-lists/{id}")
                    .get("/api/v1/employee-notification-white-lists/#{employeeNotificationWhiteListsId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_employee_notification_white_lists_By_id =
            http("UC06_PATCH_/api/v1/employee-notification-white-lists/{id}")
                    .patch("/api/v1/employee-notification-white-lists/#{employeeNotificationWhiteListsId}")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotificationWhiteLists/EmployeeNotificationWhiteListApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_employee_notification_white_lists_By_id =
            http("UC07_PUT_/api/v1/employee-notification-white-lists/{id}")
                    .put("/api/v1/employee-notification-white-lists/#{employeeNotificationWhiteListsId}")
                    .body(ElFileBody(JSONS_PATH + "EmployeeNotificationWhiteLists/EmployeeNotificationWhiteListApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
