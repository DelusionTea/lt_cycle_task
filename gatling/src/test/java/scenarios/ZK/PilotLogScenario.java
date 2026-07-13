package scenarios.ZK;

import cases.ZK.PilotLogCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class PilotLogScenario {

    public static ChainBuilder UC01_GET_v2_pilot_log =
            group("UC01_GET_v2_pilot_log").on(
                    exec(PilotLogCase.UC01_GET_v2_pilot_log));

    public static ChainBuilder UC02_POST_v2_pilot_log =
            group("UC02_POST_v2_pilot_log").on(
                    exec(PilotLogCase.UC02_POST_v2_pilot_log));

    public static ChainBuilder UC03_PATCH_v2_pilot_log_batch_identical =
            group("UC03_PATCH_v2_pilot_log_batch_identical").on(
                    exec(PilotLogCase.UC03_PATCH_v2_pilot_log_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_pilot_log_By_id =
            group("UC04_DELETE_v2_pilot_log_By_id").on(
                    exec(PilotLogCase.UC04_DELETE_v2_pilot_log_By_id));

    public static ChainBuilder UC05_GET_v2_pilot_log_By_id =
            group("UC05_GET_v2_pilot_log_By_id").on(
                    exec(PilotLogCase.UC05_GET_v2_pilot_log_By_id));

    public static ChainBuilder UC06_PATCH_v2_pilot_log_By_id =
            group("UC06_PATCH_v2_pilot_log_By_id").on(
                    exec(PilotLogCase.UC06_PATCH_v2_pilot_log_By_id));

    public static ChainBuilder UC07_PUT_v2_pilot_log_By_id =
            group("UC07_PUT_v2_pilot_log_By_id").on(
                    exec(PilotLogCase.UC07_PUT_v2_pilot_log_By_id));

    public static ScenarioBuilder scn = scenario("PilotLog")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_pilot_log),
                            new Choice.WithWeight(14, UC02_POST_v2_pilot_log),
                            new Choice.WithWeight(14, UC03_PATCH_v2_pilot_log_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_pilot_log_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_pilot_log_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_pilot_log_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_pilot_log_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug PilotLog")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(PilotLogCase.UC01_GET_v2_pilot_log)
            .exec(PilotLogCase.UC02_POST_v2_pilot_log)
            .exec(PilotLogCase.UC03_PATCH_v2_pilot_log_batch_identical)
            .exec(PilotLogCase.UC04_DELETE_v2_pilot_log_By_id)
            .exec(PilotLogCase.UC05_GET_v2_pilot_log_By_id)
            .exec(PilotLogCase.UC06_PATCH_v2_pilot_log_By_id)
            .exec(PilotLogCase.UC07_PUT_v2_pilot_log_By_id);
}
