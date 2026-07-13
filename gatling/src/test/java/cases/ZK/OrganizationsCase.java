package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class OrganizationsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v4_organizations =
            http("UC01_GET_/api/v4/organizations")
                    .get("/api/v4/organizations")
                    .queryParam("ucpId", "#{organizationsUcpId}")
                    .queryParam("inn", "#{organizationsInn}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
