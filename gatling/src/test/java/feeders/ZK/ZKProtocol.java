package feeders.ZK;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * HTTP-протокол домена ZK для Simulation-классов.
 *
 * Base URL задаётся JVM-свойством {@code -DzkBaseUrl=...}
 * (по умолчанию {@code http://localhost:8080} для локальной отладки).
 */
public final class ZKProtocol {

    private ZKProtocol() {
    }

    public static final HttpProtocolBuilder httpProtocol =
            http.baseUrl(System.getProperty("zkBaseUrl", "http://localhost:8080"))
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json")
                    .shareConnections();
}
