package scenarios.ZK;

import cases.ZK.SystemPreferencesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class SystemPreferencesScenario {

    public static ChainBuilder UC01_GET_v2_system_preferences =
            group("UC01_GET_v2_system_preferences").on(
                    exec(SystemPreferencesCase.UC01_GET_v2_system_preferences));

    public static ChainBuilder UC02_POST_v2_system_preferences =
            group("UC02_POST_v2_system_preferences").on(
                    exec(SystemPreferencesCase.UC02_POST_v2_system_preferences));

    public static ChainBuilder UC03_PATCH_v2_system_preferences_batch_identical =
            group("UC03_PATCH_v2_system_preferences_batch_identical").on(
                    exec(SystemPreferencesCase.UC03_PATCH_v2_system_preferences_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_system_preferences_By_id =
            group("UC04_DELETE_v2_system_preferences_By_id").on(
                    exec(SystemPreferencesCase.UC04_DELETE_v2_system_preferences_By_id));

    public static ChainBuilder UC05_GET_v2_system_preferences_By_id =
            group("UC05_GET_v2_system_preferences_By_id").on(
                    exec(SystemPreferencesCase.UC05_GET_v2_system_preferences_By_id));

    public static ChainBuilder UC06_PATCH_v2_system_preferences_By_id =
            group("UC06_PATCH_v2_system_preferences_By_id").on(
                    exec(SystemPreferencesCase.UC06_PATCH_v2_system_preferences_By_id));

    public static ChainBuilder UC07_PUT_v2_system_preferences_By_id =
            group("UC07_PUT_v2_system_preferences_By_id").on(
                    exec(SystemPreferencesCase.UC07_PUT_v2_system_preferences_By_id));

    public static ScenarioBuilder scn = scenario("SystemPreferences")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_system_preferences),
                            new Choice.WithWeight(14, UC02_POST_v2_system_preferences),
                            new Choice.WithWeight(14, UC03_PATCH_v2_system_preferences_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_system_preferences_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_system_preferences_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_system_preferences_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_system_preferences_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug SystemPreferences")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(SystemPreferencesCase.UC01_GET_v2_system_preferences)
            .exec(SystemPreferencesCase.UC02_POST_v2_system_preferences)
            .exec(SystemPreferencesCase.UC03_PATCH_v2_system_preferences_batch_identical)
            .exec(SystemPreferencesCase.UC04_DELETE_v2_system_preferences_By_id)
            .exec(SystemPreferencesCase.UC05_GET_v2_system_preferences_By_id)
            .exec(SystemPreferencesCase.UC06_PATCH_v2_system_preferences_By_id)
            .exec(SystemPreferencesCase.UC07_PUT_v2_system_preferences_By_id);
}
