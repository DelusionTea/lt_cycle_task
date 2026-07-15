package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class InfrastructureCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v1_infrastructure_entities =
            http("UC01_GET_/api/v1/infrastructure/entities")
                    .get("/api/v1/infrastructure/entities")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC02_GET_v1_infrastructure_entities_By_entityName_filters =
            http("UC02_GET_/api/v1/infrastructure/entities/{entityName}/filters")
                    .get("/api/v1/infrastructure/entities/#{entity_name}/filters")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC03_GET_v1_infrastructure_entities_By_entityName_filters_view_all =
            http("UC03_GET_/api/v1/infrastructure/entities/{entityName}/filters/view-all")
                    .get("/api/v1/infrastructure/entities/#{entity_name}/filters/view-all")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC04_GET_v1_infrastructure_entities_By_entityName_filters_By_fieldName =
            http("UC04_GET_/api/v1/infrastructure/entities/{entityName}/filters/{fieldName}")
                    .get("/api/v1/infrastructure/entities/#{entity_name}/filters/#{field_name}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC05_GET_v1_infrastructure_entities_By_entityName_ordering =
            http("UC05_GET_/api/v1/infrastructure/entities/{entityName}/ordering")
                    .get("/api/v1/infrastructure/entities/#{entity_name}/ordering")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
