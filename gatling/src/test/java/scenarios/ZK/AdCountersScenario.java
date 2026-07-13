package scenarios.ZK;

import cases.ZK.AdCountersCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class AdCountersScenario {

    public static ChainBuilder UC01_GET_v1_ad_counters =
            group("UC01_GET_v1_ad_counters").on(
                    exec(AdCountersCase.UC01_GET_v1_ad_counters));

    public static ChainBuilder UC02_POST_v1_ad_counters =
            group("UC02_POST_v1_ad_counters").on(
                    exec(AdCountersCase.UC02_POST_v1_ad_counters));

    public static ChainBuilder UC03_PATCH_v1_ad_counters_batch_identical =
            group("UC03_PATCH_v1_ad_counters_batch_identical").on(
                    exec(AdCountersCase.UC03_PATCH_v1_ad_counters_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_ad_counters_By_id =
            group("UC04_DELETE_v1_ad_counters_By_id").on(
                    exec(AdCountersCase.UC04_DELETE_v1_ad_counters_By_id));

    public static ChainBuilder UC05_GET_v1_ad_counters_By_id =
            group("UC05_GET_v1_ad_counters_By_id").on(
                    exec(AdCountersCase.UC05_GET_v1_ad_counters_By_id));

    public static ChainBuilder UC06_PATCH_v1_ad_counters_By_id =
            group("UC06_PATCH_v1_ad_counters_By_id").on(
                    exec(AdCountersCase.UC06_PATCH_v1_ad_counters_By_id));

    public static ChainBuilder UC07_PUT_v1_ad_counters_By_id =
            group("UC07_PUT_v1_ad_counters_By_id").on(
                    exec(AdCountersCase.UC07_PUT_v1_ad_counters_By_id));

    public static ScenarioBuilder scn = scenario("AdCounters")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_ad_counters),
                            new Choice.WithWeight(14, UC02_POST_v1_ad_counters),
                            new Choice.WithWeight(14, UC03_PATCH_v1_ad_counters_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_ad_counters_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_ad_counters_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_ad_counters_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_ad_counters_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug AdCounters")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(AdCountersCase.UC01_GET_v1_ad_counters)
            .exec(AdCountersCase.UC02_POST_v1_ad_counters)
            .exec(AdCountersCase.UC03_PATCH_v1_ad_counters_batch_identical)
            .exec(AdCountersCase.UC04_DELETE_v1_ad_counters_By_id)
            .exec(AdCountersCase.UC05_GET_v1_ad_counters_By_id)
            .exec(AdCountersCase.UC06_PATCH_v1_ad_counters_By_id)
            .exec(AdCountersCase.UC07_PUT_v1_ad_counters_By_id);
}
