package feeders.ZK;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.Session;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static io.gatling.javaapi.core.CoreDsl.listFeeder;

public class Methods {
    public static FeederBuilder<Object> rqUidsFeeder = listFeeder(
            Collections.singletonList(Map.of("rqUuid", UUID.randomUUID().toString()))
    ).circular();

    public static Function<Session, String> queryFromFile(String classpathResource) {
        return session -> readResource(classpathResource);
    }

    protected static String readResource(String classpathResource) {
        InputStream stream = Methods.class.getClassLoader().getResourceAsStream(classpathResource);
        if (stream == null) {
            throw new IllegalStateException("Resource not found: " + classpathResource);
        }
        try (InputStream in = stream) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + classpathResource, e);
        }
    }
}
