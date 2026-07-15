package scenarios.ZK;

import cases.ZK.ProactiveOnboardingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.proactiveOnboardings;
import static feeders.ZK.ZKFeeder.proactiveOnboardings;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ProactiveOnboardingsScenario {

    public static ChainBuilder UC01_GET_v1_proactive_onboardings =
            group("UC01_GET_v1_proactive_onboardings").on(
                    exec(ProactiveOnboardingsCase.UC01_GET_v1_proactive_onboardings));

    public static ChainBuilder UC02_POST_v1_proactive_onboardings =
            group("UC02_POST_v1_proactive_onboardings").on(
                    exec(ProactiveOnboardingsCase.UC02_POST_v1_proactive_onboardings));

    public static ChainBuilder UC03_PATCH_v1_proactive_onboardings_batch_identical =
            group("UC03_PATCH_v1_proactive_onboardings_batch_identical").on(
                    exec(ProactiveOnboardingsCase.UC03_PATCH_v1_proactive_onboardings_batch_identical));

    public static ChainBuilder UC04_GET_v1_proactive_onboardings_find_list_extended =
            group("UC04_GET_v1_proactive_onboardings_find_list_extended").on(
                    exec(ProactiveOnboardingsCase.UC04_GET_v1_proactive_onboardings_find_list_extended));

    public static ChainBuilder UC05_GET_v1_proactive_onboardings_get_counters =
            group("UC05_GET_v1_proactive_onboardings_get_counters").on(
                    exec(ProactiveOnboardingsCase.UC05_GET_v1_proactive_onboardings_get_counters));

    public static ChainBuilder UC06_DELETE_v1_proactive_onboardings_By_id =
            group("UC06_DELETE_v1_proactive_onboardings_By_id").on(
                    exec(ProactiveOnboardingsCase.UC06_DELETE_v1_proactive_onboardings_By_id));

    public static ChainBuilder UC07_GET_v1_proactive_onboardings_By_id =
            group("UC07_GET_v1_proactive_onboardings_By_id").on(
                    exec(ProactiveOnboardingsCase.UC07_GET_v1_proactive_onboardings_By_id));

    public static ChainBuilder UC08_PATCH_v1_proactive_onboardings_By_id =
            group("UC08_PATCH_v1_proactive_onboardings_By_id").on(
                    exec(ProactiveOnboardingsCase.UC08_PATCH_v1_proactive_onboardings_By_id));

    public static ChainBuilder UC09_PUT_v1_proactive_onboardings_By_id =
            group("UC09_PUT_v1_proactive_onboardings_By_id").on(
                    exec(ProactiveOnboardingsCase.UC09_PUT_v1_proactive_onboardings_By_id));

    public static ScenarioBuilder scn = scenario("ProactiveOnboardings")
            .feed(defaultFeeder)
            .feed(proactiveOnboardings)
            .feed(proactiveOnboardings)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(11, UC01_GET_v1_proactive_onboardings),
                            new Choice.WithWeight(11, UC02_POST_v1_proactive_onboardings),
                            new Choice.WithWeight(11, UC03_PATCH_v1_proactive_onboardings_batch_identical),
                            new Choice.WithWeight(11, UC04_GET_v1_proactive_onboardings_find_list_extended),
                            new Choice.WithWeight(11, UC05_GET_v1_proactive_onboardings_get_counters),
                            new Choice.WithWeight(11, UC06_DELETE_v1_proactive_onboardings_By_id),
                            new Choice.WithWeight(11, UC07_GET_v1_proactive_onboardings_By_id),
                            new Choice.WithWeight(11, UC08_PATCH_v1_proactive_onboardings_By_id),
                            new Choice.WithWeight(11, UC09_PUT_v1_proactive_onboardings_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ProactiveOnboardings")
            .feed(defaultFeeder)
            .feed(proactiveOnboardings)
            .feed(proactiveOnboardings)
            .feed(rqUidsFeeder)
            .exec(ProactiveOnboardingsCase.UC01_GET_v1_proactive_onboardings)
            .exec(ProactiveOnboardingsCase.UC02_POST_v1_proactive_onboardings)
            .exec(ProactiveOnboardingsCase.UC03_PATCH_v1_proactive_onboardings_batch_identical)
            .exec(ProactiveOnboardingsCase.UC04_GET_v1_proactive_onboardings_find_list_extended)
            .exec(ProactiveOnboardingsCase.UC05_GET_v1_proactive_onboardings_get_counters)
            .exec(ProactiveOnboardingsCase.UC06_DELETE_v1_proactive_onboardings_By_id)
            .exec(ProactiveOnboardingsCase.UC07_GET_v1_proactive_onboardings_By_id)
            .exec(ProactiveOnboardingsCase.UC08_PATCH_v1_proactive_onboardings_By_id)
            .exec(ProactiveOnboardingsCase.UC09_PUT_v1_proactive_onboardings_By_id);
}
