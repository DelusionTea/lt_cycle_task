package scenarios.ZK;

import cases.ZK.AdClientMarkingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.adClientMarkings;
import static feeders.ZK.ZKFeeder.adClientMarkings;
import static io.gatling.javaapi.core.CoreDsl.*;

public class AdClientMarkingsScenario {

    public static ChainBuilder UC01_GET_v1_ad_client_markings =
            group("UC01_GET_v1_ad_client_markings").on(
                    exec(AdClientMarkingsCase.UC01_GET_v1_ad_client_markings));

    public static ChainBuilder UC02_POST_v1_ad_client_markings =
            group("UC02_POST_v1_ad_client_markings").on(
                    exec(AdClientMarkingsCase.UC02_POST_v1_ad_client_markings));

    public static ChainBuilder UC03_PATCH_v1_ad_client_markings_batch_identical =
            group("UC03_PATCH_v1_ad_client_markings_batch_identical").on(
                    exec(AdClientMarkingsCase.UC03_PATCH_v1_ad_client_markings_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_ad_client_markings_By_id =
            group("UC04_DELETE_v1_ad_client_markings_By_id").on(
                    exec(AdClientMarkingsCase.UC04_DELETE_v1_ad_client_markings_By_id));

    public static ChainBuilder UC05_GET_v1_ad_client_markings_By_id =
            group("UC05_GET_v1_ad_client_markings_By_id").on(
                    exec(AdClientMarkingsCase.UC05_GET_v1_ad_client_markings_By_id));

    public static ChainBuilder UC06_PATCH_v1_ad_client_markings_By_id =
            group("UC06_PATCH_v1_ad_client_markings_By_id").on(
                    exec(AdClientMarkingsCase.UC06_PATCH_v1_ad_client_markings_By_id));

    public static ChainBuilder UC07_PUT_v1_ad_client_markings_By_id =
            group("UC07_PUT_v1_ad_client_markings_By_id").on(
                    exec(AdClientMarkingsCase.UC07_PUT_v1_ad_client_markings_By_id));

    public static ChainBuilder UC08_DELETE_v2_ad_client_markings_batch =
            group("UC08_DELETE_v2_ad_client_markings_batch").on(
                    exec(AdClientMarkingsCase.UC08_DELETE_v2_ad_client_markings_batch));

    public static ChainBuilder UC09_PATCH_v2_ad_client_markings_batch =
            group("UC09_PATCH_v2_ad_client_markings_batch").on(
                    exec(AdClientMarkingsCase.UC09_PATCH_v2_ad_client_markings_batch));

    public static ChainBuilder UC10_POST_v2_ad_client_markings_batch =
            group("UC10_POST_v2_ad_client_markings_batch").on(
                    exec(AdClientMarkingsCase.UC10_POST_v2_ad_client_markings_batch));

    public static ChainBuilder UC11_PUT_v2_ad_client_markings_batch =
            group("UC11_PUT_v2_ad_client_markings_batch").on(
                    exec(AdClientMarkingsCase.UC11_PUT_v2_ad_client_markings_batch));

    public static ScenarioBuilder scn = scenario("AdClientMarkings")
            .feed(defaultFeeder)
            .feed(adClientMarkings)
            .feed(adClientMarkings)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(9, UC01_GET_v1_ad_client_markings),
                            new Choice.WithWeight(9, UC02_POST_v1_ad_client_markings),
                            new Choice.WithWeight(9, UC03_PATCH_v1_ad_client_markings_batch_identical),
                            new Choice.WithWeight(9, UC04_DELETE_v1_ad_client_markings_By_id),
                            new Choice.WithWeight(9, UC05_GET_v1_ad_client_markings_By_id),
                            new Choice.WithWeight(9, UC06_PATCH_v1_ad_client_markings_By_id),
                            new Choice.WithWeight(9, UC07_PUT_v1_ad_client_markings_By_id),
                            new Choice.WithWeight(9, UC08_DELETE_v2_ad_client_markings_batch),
                            new Choice.WithWeight(9, UC09_PATCH_v2_ad_client_markings_batch),
                            new Choice.WithWeight(9, UC10_POST_v2_ad_client_markings_batch),
                            new Choice.WithWeight(9, UC11_PUT_v2_ad_client_markings_batch)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug AdClientMarkings")
            .feed(defaultFeeder)
            .feed(adClientMarkings)
            .feed(adClientMarkings)
            .feed(rqUidsFeeder)
            .exec(AdClientMarkingsCase.UC01_GET_v1_ad_client_markings)
            .exec(AdClientMarkingsCase.UC02_POST_v1_ad_client_markings)
            .exec(AdClientMarkingsCase.UC03_PATCH_v1_ad_client_markings_batch_identical)
            .exec(AdClientMarkingsCase.UC04_DELETE_v1_ad_client_markings_By_id)
            .exec(AdClientMarkingsCase.UC05_GET_v1_ad_client_markings_By_id)
            .exec(AdClientMarkingsCase.UC06_PATCH_v1_ad_client_markings_By_id)
            .exec(AdClientMarkingsCase.UC07_PUT_v1_ad_client_markings_By_id)
            .exec(AdClientMarkingsCase.UC08_DELETE_v2_ad_client_markings_batch)
            .exec(AdClientMarkingsCase.UC09_PATCH_v2_ad_client_markings_batch)
            .exec(AdClientMarkingsCase.UC10_POST_v2_ad_client_markings_batch)
            .exec(AdClientMarkingsCase.UC11_PUT_v2_ad_client_markings_batch);
}
