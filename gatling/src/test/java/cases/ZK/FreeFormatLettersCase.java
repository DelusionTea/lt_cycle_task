package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class FreeFormatLettersCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_free_format_letters =
            http("UC01_GET_/api/v2/free-format-letters")
                    .get("/api/v2/free-format-letters")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_POST_v2_free_format_letters =
            http("UC02_POST_/api/v2/free-format-letters")
                    .post("/api/v2/free-format-letters")
                    .body(ElFileBody(JSONS_PATH + "FreeFormatLetters/ComplianceFreeFormatLetterApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC03_GET_v2_free_format_letters_all =
            http("UC03_GET_/api/v2/free-format-letters/all")
                    .get("/api/v2/free-format-letters/all")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PATCH_v2_free_format_letters_batch_identical =
            http("UC04_PATCH_/api/v2/free-format-letters/batch-identical")
                    .patch("/api/v2/free-format-letters/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "FreeFormatLetters/ComplianceFreeFormatLetterApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_DELETE_v2_free_format_letters_By_id =
            http("UC05_DELETE_/api/v2/free-format-letters/{id}")
                    .delete("/api/v2/free-format-letters/#{letter_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_GET_v2_free_format_letters_By_id =
            http("UC06_GET_/api/v2/free-format-letters/{id}")
                    .get("/api/v2/free-format-letters/#{letter_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PATCH_v2_free_format_letters_By_id =
            http("UC07_PATCH_/api/v2/free-format-letters/{id}")
                    .patch("/api/v2/free-format-letters/#{letter_id}")
                    .body(ElFileBody(JSONS_PATH + "FreeFormatLetters/ComplianceFreeFormatLetterApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_PUT_v2_free_format_letters_By_id =
            http("UC08_PUT_/api/v2/free-format-letters/{id}")
                    .put("/api/v2/free-format-letters/#{letter_id}")
                    .body(ElFileBody(JSONS_PATH + "FreeFormatLetters/ComplianceFreeFormatLetterApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
