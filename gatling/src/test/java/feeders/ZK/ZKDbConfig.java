package feeders.ZK;

/**
 * JDBC-подключение к БД ZK (PostgreSQL, схема cmpl).
 *
 * JVM-свойства:
 *   zk.jdbc.enabled   — true для загрузки данных из БД (default false)
 *   zk.jdbc.url       — jdbc:postgresql://host:port/db
 *   zk.jdbc.user
 *   zk.jdbc.password
 *   zk.jdbc.limit     — LIMIT для SELECT (default 1000)
 */
public final class ZKDbConfig {

    private ZKDbConfig() {
    }

    public static boolean useJdbc() {
        return Boolean.parseBoolean(System.getProperty("zk.jdbc.enabled", "false"));
    }

    public static String url() {
        return System.getProperty(
                "zk.jdbc.url",
                "jdbc:postgresql://localhost:5432/cmplnt");
    }

    public static String user() {
        return System.getProperty("zk.jdbc.user", "cmpladmin");
    }

    public static String password() {
        return System.getProperty("zk.jdbc.password", "");
    }

    public static int limit() {
        return Integer.parseInt(System.getProperty("zk.jdbc.limit", "1000"));
    }
}
