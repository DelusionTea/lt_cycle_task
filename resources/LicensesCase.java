package cases.pprbSberrating;

import feeders.pprbSberrating.Headers;
import feeders.pprbSberrating.Methods;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class LicensesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/pprbSberrating/Licenses/";

    public static HttpRequestActionBuilder UC01_POST_Licenses_Summary =
            http("UC01_POST_/licenses/summary")
                    .post("/licenses/v1/licenses/summary")
                    .body(ElFileBody(JSONS_PATH + "UC01.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_POST_Licenses_List =
            http("UC02_POST_/licenses/list")
                    .post("/licenses/v1/licenses/list")
                    .body(ElFileBody(JSONS_PATH + "UC02.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC03_POST_Licenses_Details =
            http("UC03_POST_/licenses/details")
                    .post("/licenses/v1/licenses/details")
                    .body(ElFileBody(JSONS_PATH + "UC03.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC04_POST_Licenses_CustomParams =
            http("UC04_POST_/licenses/v1/custom-params")
                    .post("/licenses/v1/custom-params")
                    .body(ElFileBody(JSONS_PATH + "UC04.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
