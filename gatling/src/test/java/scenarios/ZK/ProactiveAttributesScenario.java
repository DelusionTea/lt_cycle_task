package scenarios.ZK;

import cases.ZK.ProactiveAttributesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.proactiveAttributes;
import static feeders.ZK.ZKFeeder.proactiveAttributes;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ProactiveAttributesScenario {

    public static ChainBuilder UC01_GET_v1_proactive_attributes =
            group("UC01_GET_v1_proactive_attributes").on(
                    exec(ProactiveAttributesCase.UC01_GET_v1_proactive_attributes));

    public static ChainBuilder UC02_POST_v1_proactive_attributes =
            group("UC02_POST_v1_proactive_attributes").on(
                    exec(ProactiveAttributesCase.UC02_POST_v1_proactive_attributes));

    public static ChainBuilder UC03_PATCH_v1_proactive_attributes_batch_identical =
            group("UC03_PATCH_v1_proactive_attributes_batch_identical").on(
                    exec(ProactiveAttributesCase.UC03_PATCH_v1_proactive_attributes_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_proactive_attributes_By_id =
            group("UC04_DELETE_v1_proactive_attributes_By_id").on(
                    exec(ProactiveAttributesCase.UC04_DELETE_v1_proactive_attributes_By_id));

    public static ChainBuilder UC05_GET_v1_proactive_attributes_By_id =
            group("UC05_GET_v1_proactive_attributes_By_id").on(
                    exec(ProactiveAttributesCase.UC05_GET_v1_proactive_attributes_By_id));

    public static ChainBuilder UC06_PATCH_v1_proactive_attributes_By_id =
            group("UC06_PATCH_v1_proactive_attributes_By_id").on(
                    exec(ProactiveAttributesCase.UC06_PATCH_v1_proactive_attributes_By_id));

    public static ChainBuilder UC07_PUT_v1_proactive_attributes_By_id =
            group("UC07_PUT_v1_proactive_attributes_By_id").on(
                    exec(ProactiveAttributesCase.UC07_PUT_v1_proactive_attributes_By_id));

    public static ScenarioBuilder scn = scenario("ProactiveAttributes")
            .feed(defaultFeeder)
            .feed(proactiveAttributes)
            .feed(proactiveAttributes)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_proactive_attributes),
                            new Choice.WithWeight(14, UC02_POST_v1_proactive_attributes),
                            new Choice.WithWeight(14, UC03_PATCH_v1_proactive_attributes_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_proactive_attributes_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_proactive_attributes_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_proactive_attributes_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_proactive_attributes_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ProactiveAttributes")
            .feed(defaultFeeder)
            .feed(proactiveAttributes)
            .feed(proactiveAttributes)
            .feed(rqUidsFeeder)
            .exec(ProactiveAttributesCase.UC01_GET_v1_proactive_attributes)
            .exec(ProactiveAttributesCase.UC02_POST_v1_proactive_attributes)
            .exec(ProactiveAttributesCase.UC03_PATCH_v1_proactive_attributes_batch_identical)
            .exec(ProactiveAttributesCase.UC04_DELETE_v1_proactive_attributes_By_id)
            .exec(ProactiveAttributesCase.UC05_GET_v1_proactive_attributes_By_id)
            .exec(ProactiveAttributesCase.UC06_PATCH_v1_proactive_attributes_By_id)
            .exec(ProactiveAttributesCase.UC07_PUT_v1_proactive_attributes_By_id);
}
