package scenarios.ZK;

import cases.ZK.ReferenceValueLinksCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.referenceValueLinks;
import static feeders.ZK.ZKFeeder.referenceValueLinks;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ReferenceValueLinksScenario {

    public static ChainBuilder UC01_GET_v2_reference_value_links =
            group("UC01_GET_v2_reference_value_links").on(
                    exec(ReferenceValueLinksCase.UC01_GET_v2_reference_value_links));

    public static ChainBuilder UC02_POST_v2_reference_value_links =
            group("UC02_POST_v2_reference_value_links").on(
                    exec(ReferenceValueLinksCase.UC02_POST_v2_reference_value_links));

    public static ChainBuilder UC03_PATCH_v2_reference_value_links_batch_identical =
            group("UC03_PATCH_v2_reference_value_links_batch_identical").on(
                    exec(ReferenceValueLinksCase.UC03_PATCH_v2_reference_value_links_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_reference_value_links_By_id =
            group("UC04_DELETE_v2_reference_value_links_By_id").on(
                    exec(ReferenceValueLinksCase.UC04_DELETE_v2_reference_value_links_By_id));

    public static ChainBuilder UC05_GET_v2_reference_value_links_By_id =
            group("UC05_GET_v2_reference_value_links_By_id").on(
                    exec(ReferenceValueLinksCase.UC05_GET_v2_reference_value_links_By_id));

    public static ChainBuilder UC06_PATCH_v2_reference_value_links_By_id =
            group("UC06_PATCH_v2_reference_value_links_By_id").on(
                    exec(ReferenceValueLinksCase.UC06_PATCH_v2_reference_value_links_By_id));

    public static ChainBuilder UC07_PUT_v2_reference_value_links_By_id =
            group("UC07_PUT_v2_reference_value_links_By_id").on(
                    exec(ReferenceValueLinksCase.UC07_PUT_v2_reference_value_links_By_id));

    public static ScenarioBuilder scn = scenario("ReferenceValueLinks")
            .feed(defaultFeeder)
            .feed(referenceValueLinks)
            .feed(referenceValueLinks)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_reference_value_links),
                            new Choice.WithWeight(14, UC02_POST_v2_reference_value_links),
                            new Choice.WithWeight(14, UC03_PATCH_v2_reference_value_links_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_reference_value_links_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_reference_value_links_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_reference_value_links_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_reference_value_links_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ReferenceValueLinks")
            .feed(defaultFeeder)
            .feed(referenceValueLinks)
            .feed(referenceValueLinks)
            .feed(rqUidsFeeder)
            .exec(ReferenceValueLinksCase.UC01_GET_v2_reference_value_links)
            .exec(ReferenceValueLinksCase.UC02_POST_v2_reference_value_links)
            .exec(ReferenceValueLinksCase.UC03_PATCH_v2_reference_value_links_batch_identical)
            .exec(ReferenceValueLinksCase.UC04_DELETE_v2_reference_value_links_By_id)
            .exec(ReferenceValueLinksCase.UC05_GET_v2_reference_value_links_By_id)
            .exec(ReferenceValueLinksCase.UC06_PATCH_v2_reference_value_links_By_id)
            .exec(ReferenceValueLinksCase.UC07_PUT_v2_reference_value_links_By_id);
}
