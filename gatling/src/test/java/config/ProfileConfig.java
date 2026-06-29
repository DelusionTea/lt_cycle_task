package config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Чтение профиля нагрузки из profile.properties (генерится profile_to_props.py
 * из profile.yaml). Позволяет вынести веса randomSwitch и интенсивность
 * injectOpen из хардкода в сценариях/симуляциях.
 *
 * Путь к файлу: системное свойство -DprofileProperties=/path/to/profile.properties,
 * иначе ищется profile.properties в рабочей директории, иначе в classpath.
 * Если файл не найден, используются дефолты, передаваемые в методы.
 *
 * Формат properties (см. profile_to_props.py):
 *   target_percent=50
 *   injection.duration=3600
 *   injection.rampup=60
 *   inject.Licenses.users=25
 *   weight.Licenses.UC01=25
 */
public final class ProfileConfig {

    private static final Properties PROPS = new Properties();
    private static boolean loaded = false;

    private ProfileConfig() {
    }

    private static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }
        loaded = true;
        String path = System.getProperty("profileProperties", "profile.properties");
        Path p = Paths.get(path);
        if (Files.exists(p)) {
            try (InputStream in = Files.newInputStream(p)) {
                PROPS.load(in);
                System.out.println("[ProfileConfig] Loaded profile from " + p.toAbsolutePath());
                return;
            } catch (IOException e) {
                System.err.println("[ProfileConfig] Failed to read " + p + ": " + e.getMessage());
            }
        }
        // fallback: classpath
        try (InputStream in = ProfileConfig.class.getClassLoader()
                .getResourceAsStream("profile.properties")) {
            if (in != null) {
                PROPS.load(in);
                System.out.println("[ProfileConfig] Loaded profile from classpath");
                return;
            }
        } catch (IOException e) {
            System.err.println("[ProfileConfig] Failed to read classpath profile: " + e.getMessage());
        }
        System.out.println("[ProfileConfig] profile.properties not found, using defaults");
    }

    public static int getInt(String key, int def) {
        ensureLoaded();
        String v = PROPS.getProperty(key);
        if (v == null || v.trim().isEmpty()) {
            return def;
        }
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static double getDouble(String key, double def) {
        ensureLoaded();
        String v = PROPS.getProperty(key);
        if (v == null || v.trim().isEmpty()) {
            return def;
        }
        try {
            return Double.parseDouble(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /** Относительный вес Choice в randomSwitch для сценария. */
    public static int getWeight(String scenario, String choice, int def) {
        return getInt("weight." + scenario + "." + choice, def);
    }

    /** Целевое число одновременных пользователей для сценария (уже масштабировано). */
    public static int getInjectUsers(String scenario, int def) {
        return getInt("inject." + scenario + ".users", def);
    }

    public static int getDuration(int def) {
        return getInt("injection.duration", def);
    }

    public static int getRampup(int def) {
        return getInt("injection.rampup", def);
    }

    public static double getTargetPercent(double def) {
        return getDouble("target_percent", def);
    }
}
