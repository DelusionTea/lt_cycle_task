package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class FileTransferTasksCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_file_transfer_tasks =
            http("UC01_GET_/api/v1/file-transfer-tasks")
                    .get("/api/v1/file-transfer-tasks")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_file_transfer_tasks =
            http("UC02_POST_/api/v1/file-transfer-tasks")
                    .post("/api/v1/file-transfer-tasks")
                    .body(ElFileBody(JSONS_PATH + "FileTransferTasks/FileTransferTaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_PATCH_v1_file_transfer_tasks_batch_identical =
            http("UC03_PATCH_/api/v1/file-transfer-tasks/batch-identical")
                    .patch("/api/v1/file-transfer-tasks/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "FileTransferTasks/FileTransferTaskApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_DELETE_v1_file_transfer_tasks_By_id =
            http("UC04_DELETE_/api/v1/file-transfer-tasks/{id}")
                    .delete("/api/v1/file-transfer-tasks/#{fileTransferTasksId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_file_transfer_tasks_By_id =
            http("UC05_GET_/api/v1/file-transfer-tasks/{id}")
                    .get("/api/v1/file-transfer-tasks/#{fileTransferTasksId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PATCH_v1_file_transfer_tasks_By_id =
            http("UC06_PATCH_/api/v1/file-transfer-tasks/{id}")
                    .patch("/api/v1/file-transfer-tasks/#{fileTransferTasksId}")
                    .body(ElFileBody(JSONS_PATH + "FileTransferTasks/FileTransferTaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PUT_v1_file_transfer_tasks_By_id =
            http("UC07_PUT_/api/v1/file-transfer-tasks/{id}")
                    .put("/api/v1/file-transfer-tasks/#{fileTransferTasksId}")
                    .body(ElFileBody(JSONS_PATH + "FileTransferTasks/FileTransferTaskApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
