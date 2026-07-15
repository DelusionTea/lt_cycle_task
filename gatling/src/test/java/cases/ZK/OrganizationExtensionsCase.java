package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class OrganizationExtensionsCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v2_organization_extensions =
            http("UC01_GET_/api/v2/organization-extensions")
                    .get("/api/v2/organization-extensions")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC02_POST_v2_organization_extensions =
            http("UC02_POST_/api/v2/organization-extensions")
                    .post("/api/v2/organization-extensions")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/OrganizationExtensionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_DELETE_v2_organization_extensions_batch =
            http("UC03_DELETE_/api/v2/organization-extensions/batch")
                    .delete("/api/v2/organization-extensions/batch")
                    .queryParam("ids", "#{ext_ids}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PATCH_v2_organization_extensions_batch =
            http("UC04_PATCH_/api/v2/organization-extensions/batch")
                    .patch("/api/v2/organization-extensions/batch")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/body_f2a11f0e.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_POST_v2_organization_extensions_batch =
            http("UC05_POST_/api/v2/organization-extensions/batch")
                    .post("/api/v2/organization-extensions/batch")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/body_f2a11f0e.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PUT_v2_organization_extensions_batch =
            http("UC06_PUT_/api/v2/organization-extensions/batch")
                    .put("/api/v2/organization-extensions/batch")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/body_f2a11f0e.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PATCH_v2_organization_extensions_batch_identical =
            http("UC07_PATCH_/api/v2/organization-extensions/batch-identical")
                    .patch("/api/v2/organization-extensions/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/OrganizationExtensionApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_DELETE_v2_organization_extensions_By_id =
            http("UC08_DELETE_/api/v2/organization-extensions/{id}")
                    .delete("/api/v2/organization-extensions/#{ext_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_GET_v2_organization_extensions_By_id =
            http("UC09_GET_/api/v2/organization-extensions/{id}")
                    .get("/api/v2/organization-extensions/#{ext_id}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC10_PATCH_v2_organization_extensions_By_id =
            http("UC10_PATCH_/api/v2/organization-extensions/{id}")
                    .patch("/api/v2/organization-extensions/#{ext_id}")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/OrganizationExtensionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC11_PUT_v2_organization_extensions_By_id =
            http("UC11_PUT_/api/v2/organization-extensions/{id}")
                    .put("/api/v2/organization-extensions/#{ext_id}")
                    .body(ElFileBody(JSONS_PATH + "OrganizationExtensions/OrganizationExtensionApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
