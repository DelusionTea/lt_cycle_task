package scenarios.ZK;

import cases.ZK.ProcessSettingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.processSettings;
import static feeders.ZK.ZKFeeder.processSettings;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ProcessSettingsScenario {

    public static ChainBuilder UC01_GET_v2_process_settings =
            group("UC01_GET_v2_process_settings").on(
                    exec(ProcessSettingsCase.UC01_GET_v2_process_settings));

    public static ChainBuilder UC02_POST_v2_process_settings =
            group("UC02_POST_v2_process_settings").on(
                    exec(ProcessSettingsCase.UC02_POST_v2_process_settings));

    public static ChainBuilder UC03_PATCH_v2_process_settings_batch_identical =
            group("UC03_PATCH_v2_process_settings_batch_identical").on(
                    exec(ProcessSettingsCase.UC03_PATCH_v2_process_settings_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_process_settings_By_id =
            group("UC04_DELETE_v2_process_settings_By_id").on(
                    exec(ProcessSettingsCase.UC04_DELETE_v2_process_settings_By_id));

    public static ChainBuilder UC05_GET_v2_process_settings_By_id =
            group("UC05_GET_v2_process_settings_By_id").on(
                    exec(ProcessSettingsCase.UC05_GET_v2_process_settings_By_id));

    public static ChainBuilder UC06_PATCH_v2_process_settings_By_id =
            group("UC06_PATCH_v2_process_settings_By_id").on(
                    exec(ProcessSettingsCase.UC06_PATCH_v2_process_settings_By_id));

    public static ChainBuilder UC07_PUT_v2_process_settings_By_id =
            group("UC07_PUT_v2_process_settings_By_id").on(
                    exec(ProcessSettingsCase.UC07_PUT_v2_process_settings_By_id));

    public static ScenarioBuilder scn = scenario("ProcessSettings")
            .feed(defaultFeeder)
            .feed(processSettings)
            .feed(processSettings)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_process_settings),
                            new Choice.WithWeight(14, UC02_POST_v2_process_settings),
                            new Choice.WithWeight(14, UC03_PATCH_v2_process_settings_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_process_settings_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_process_settings_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_process_settings_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_process_settings_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ProcessSettings")
            .feed(defaultFeeder)
            .feed(processSettings)
            .feed(processSettings)
            .feed(rqUidsFeeder)
            .exec(ProcessSettingsCase.UC01_GET_v2_process_settings)
            .exec(ProcessSettingsCase.UC02_POST_v2_process_settings)
            .exec(ProcessSettingsCase.UC03_PATCH_v2_process_settings_batch_identical)
            .exec(ProcessSettingsCase.UC04_DELETE_v2_process_settings_By_id)
            .exec(ProcessSettingsCase.UC05_GET_v2_process_settings_By_id)
            .exec(ProcessSettingsCase.UC06_PATCH_v2_process_settings_By_id)
            .exec(ProcessSettingsCase.UC07_PUT_v2_process_settings_By_id);
}
