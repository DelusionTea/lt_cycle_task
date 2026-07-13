package scenarios.ZK;

import cases.ZK.ProactiveCommunicationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ProactiveCommunicationsScenario {

    public static ChainBuilder UC01_GET_v1_proactive_communications =
            group("UC01_GET_v1_proactive_communications").on(
                    exec(ProactiveCommunicationsCase.UC01_GET_v1_proactive_communications));

    public static ChainBuilder UC02_POST_v1_proactive_communications =
            group("UC02_POST_v1_proactive_communications").on(
                    exec(ProactiveCommunicationsCase.UC02_POST_v1_proactive_communications));

    public static ChainBuilder UC03_PATCH_v1_proactive_communications_batch_identical =
            group("UC03_PATCH_v1_proactive_communications_batch_identical").on(
                    exec(ProactiveCommunicationsCase.UC03_PATCH_v1_proactive_communications_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_proactive_communications_By_id =
            group("UC04_DELETE_v1_proactive_communications_By_id").on(
                    exec(ProactiveCommunicationsCase.UC04_DELETE_v1_proactive_communications_By_id));

    public static ChainBuilder UC05_GET_v1_proactive_communications_By_id =
            group("UC05_GET_v1_proactive_communications_By_id").on(
                    exec(ProactiveCommunicationsCase.UC05_GET_v1_proactive_communications_By_id));

    public static ChainBuilder UC06_PATCH_v1_proactive_communications_By_id =
            group("UC06_PATCH_v1_proactive_communications_By_id").on(
                    exec(ProactiveCommunicationsCase.UC06_PATCH_v1_proactive_communications_By_id));

    public static ChainBuilder UC07_PUT_v1_proactive_communications_By_id =
            group("UC07_PUT_v1_proactive_communications_By_id").on(
                    exec(ProactiveCommunicationsCase.UC07_PUT_v1_proactive_communications_By_id));

    public static ScenarioBuilder scn = scenario("ProactiveCommunications")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_proactive_communications),
                            new Choice.WithWeight(14, UC02_POST_v1_proactive_communications),
                            new Choice.WithWeight(14, UC03_PATCH_v1_proactive_communications_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_proactive_communications_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_proactive_communications_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_proactive_communications_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_proactive_communications_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ProactiveCommunications")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ProactiveCommunicationsCase.UC01_GET_v1_proactive_communications)
            .exec(ProactiveCommunicationsCase.UC02_POST_v1_proactive_communications)
            .exec(ProactiveCommunicationsCase.UC03_PATCH_v1_proactive_communications_batch_identical)
            .exec(ProactiveCommunicationsCase.UC04_DELETE_v1_proactive_communications_By_id)
            .exec(ProactiveCommunicationsCase.UC05_GET_v1_proactive_communications_By_id)
            .exec(ProactiveCommunicationsCase.UC06_PATCH_v1_proactive_communications_By_id)
            .exec(ProactiveCommunicationsCase.UC07_PUT_v1_proactive_communications_By_id);
}
