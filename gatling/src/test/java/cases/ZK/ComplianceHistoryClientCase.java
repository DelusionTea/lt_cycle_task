package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ComplianceHistoryClientCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_compliance_history_client_By_ucpId =
            http("UC01_GET_/api/v2/compliance-history-client/{ucpId}")
                    .get("/api/v2/compliance-history-client/#{ucp_id}")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/TypesFilterApiModel_filter.json"))
                    .queryParam("pageNum", "#{page_num}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
