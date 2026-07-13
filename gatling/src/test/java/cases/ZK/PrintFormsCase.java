package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PrintFormsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v3_print_forms =
            http("UC01_GET_/api/v3/print-forms")
                    .get("/api/v3/print-forms")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PrintFormsApiModel_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_GET_v3_print_forms_download =
            http("UC02_GET_/api/v3/print-forms/download")
                    .get("/api/v3/print-forms/download")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PrintFormsApiModel_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_POST_v3_print_forms_download_explanatory_note =
            http("UC03_POST_/api/v3/print-forms/download/explanatory-note")
                    .post("/api/v3/print-forms/download/explanatory-note")
                    .body(ElFileBody(JSONS_PATH + "shared/ExplanatoryNoteApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_POST_v3_print_forms_explanatory_note =
            http("UC04_POST_/api/v3/print-forms/explanatory-note")
                    .post("/api/v3/print-forms/explanatory-note")
                    .body(ElFileBody(JSONS_PATH + "shared/ExplanatoryNoteApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
