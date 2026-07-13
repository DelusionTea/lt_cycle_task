package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class OrganizationAttributesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    public static HttpRequestActionBuilder UC01_GET_v1_organization_attributes =
            http("UC01_GET_/api/v1/organization-attributes")
                    .get("/api/v1/organization-attributes")
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_POST_v1_organization_attributes =
            http("UC02_POST_/api/v1/organization-attributes")
                    .post("/api/v1/organization-attributes")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/OrganizationAttributeApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_DELETE_v1_organization_attributes_batch =
            http("UC03_DELETE_/api/v1/organization-attributes/batch")
                    .delete("/api/v1/organization-attributes/batch")
                    .queryParam("ids", "#{organizationAttributesIds}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_PATCH_v1_organization_attributes_batch =
            http("UC04_PATCH_/api/v1/organization-attributes/batch")
                    .patch("/api/v1/organization-attributes/batch")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/body_f0943b53.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    public static HttpRequestActionBuilder UC05_POST_v1_organization_attributes_batch =
            http("UC05_POST_/api/v1/organization-attributes/batch")
                    .post("/api/v1/organization-attributes/batch")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/body_f0943b53.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC06_PUT_v1_organization_attributes_batch =
            http("UC06_PUT_/api/v1/organization-attributes/batch")
                    .put("/api/v1/organization-attributes/batch")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/body_f0943b53.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC07_PATCH_v1_organization_attributes_batch_identical =
            http("UC07_PATCH_/api/v1/organization-attributes/batch-identical")
                    .patch("/api/v1/organization-attributes/batch-identical")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/OrganizationAttributeApiModel_body.json"))
                    .queryParam("filter", Methods.queryFromFile(JSONS_PATH + "shared/PagingRequestExt_filter.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC08_DELETE_v1_organization_attributes_By_id =
            http("UC08_DELETE_/api/v1/organization-attributes/{id}")
                    .delete("/api/v1/organization-attributes/#{organizationAttributesId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC09_GET_v1_organization_attributes_By_id =
            http("UC09_GET_/api/v1/organization-attributes/{id}")
                    .get("/api/v1/organization-attributes/#{organizationAttributesId}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC10_PATCH_v1_organization_attributes_By_id =
            http("UC10_PATCH_/api/v1/organization-attributes/{id}")
                    .patch("/api/v1/organization-attributes/#{organizationAttributesId}")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/OrganizationAttributeApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC11_PUT_v1_organization_attributes_By_id =
            http("UC11_PUT_/api/v1/organization-attributes/{id}")
                    .put("/api/v1/organization-attributes/#{organizationAttributesId}")
                    .body(ElFileBody(JSONS_PATH + "OrganizationAttributes/OrganizationAttributeApiModel_body.json"))
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
