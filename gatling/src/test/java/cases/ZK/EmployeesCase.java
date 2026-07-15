package cases.ZK;

import feeders.ZK.Headers;
import feeders.ZK.Methods;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class EmployeesCase extends Methods {

    private static final String JSONS_PATH = "JSONs/ZK/";

    // Низконагружен не попал в профиль
    public static HttpRequestActionBuilder UC01_GET_v2_employees_By_employeeNumber =
            http("UC01_GET_/api/v2/employees/{employeeNumber}")
                    .get("/api/v2/employees/#{employee_number}")
                    .headers(Headers.getCommonHeaders())
                    .check(status().is(200));
}
