package scenarios.ZK;

import cases.ZK.DigitalUserCompliancesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.digitalUserCompliances;
import static feeders.ZK.ZKFeeder.digitalUserCompliances;
import static io.gatling.javaapi.core.CoreDsl.*;

public class DigitalUserCompliancesScenario {

    public static ChainBuilder UC01_GET_v2_digital_user_compliances =
            group("UC01_GET_v2_digital_user_compliances").on(
                    exec(DigitalUserCompliancesCase.UC01_GET_v2_digital_user_compliances));

    public static ChainBuilder UC02_POST_v2_digital_user_compliances =
            group("UC02_POST_v2_digital_user_compliances").on(
                    exec(DigitalUserCompliancesCase.UC02_POST_v2_digital_user_compliances));

    public static ChainBuilder UC03_PATCH_v2_digital_user_compliances_batch_identical =
            group("UC03_PATCH_v2_digital_user_compliances_batch_identical").on(
                    exec(DigitalUserCompliancesCase.UC03_PATCH_v2_digital_user_compliances_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_digital_user_compliances_By_id =
            group("UC04_DELETE_v2_digital_user_compliances_By_id").on(
                    exec(DigitalUserCompliancesCase.UC04_DELETE_v2_digital_user_compliances_By_id));

    public static ChainBuilder UC05_GET_v2_digital_user_compliances_By_id =
            group("UC05_GET_v2_digital_user_compliances_By_id").on(
                    exec(DigitalUserCompliancesCase.UC05_GET_v2_digital_user_compliances_By_id));

    public static ChainBuilder UC06_PATCH_v2_digital_user_compliances_By_id =
            group("UC06_PATCH_v2_digital_user_compliances_By_id").on(
                    exec(DigitalUserCompliancesCase.UC06_PATCH_v2_digital_user_compliances_By_id));

    public static ChainBuilder UC07_PUT_v2_digital_user_compliances_By_id =
            group("UC07_PUT_v2_digital_user_compliances_By_id").on(
                    exec(DigitalUserCompliancesCase.UC07_PUT_v2_digital_user_compliances_By_id));

    public static ScenarioBuilder scn = scenario("DigitalUserCompliances")
            .feed(defaultFeeder)
            .feed(digitalUserCompliances)
            .feed(digitalUserCompliances)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_digital_user_compliances),
                            new Choice.WithWeight(14, UC02_POST_v2_digital_user_compliances),
                            new Choice.WithWeight(14, UC03_PATCH_v2_digital_user_compliances_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_digital_user_compliances_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_digital_user_compliances_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_digital_user_compliances_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_digital_user_compliances_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug DigitalUserCompliances")
            .feed(defaultFeeder)
            .feed(digitalUserCompliances)
            .feed(digitalUserCompliances)
            .feed(rqUidsFeeder)
            .exec(DigitalUserCompliancesCase.UC01_GET_v2_digital_user_compliances)
            .exec(DigitalUserCompliancesCase.UC02_POST_v2_digital_user_compliances)
            .exec(DigitalUserCompliancesCase.UC03_PATCH_v2_digital_user_compliances_batch_identical)
            .exec(DigitalUserCompliancesCase.UC04_DELETE_v2_digital_user_compliances_By_id)
            .exec(DigitalUserCompliancesCase.UC05_GET_v2_digital_user_compliances_By_id)
            .exec(DigitalUserCompliancesCase.UC06_PATCH_v2_digital_user_compliances_By_id)
            .exec(DigitalUserCompliancesCase.UC07_PUT_v2_digital_user_compliances_By_id);
}
