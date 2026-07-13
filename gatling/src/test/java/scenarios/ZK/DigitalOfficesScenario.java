package scenarios.ZK;

import cases.ZK.DigitalOfficesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class DigitalOfficesScenario {

    public static ChainBuilder UC01_GET_v2_digital_offices =
            group("UC01_GET_v2_digital_offices").on(
                    exec(DigitalOfficesCase.UC01_GET_v2_digital_offices));

    public static ChainBuilder UC02_POST_v2_digital_offices =
            group("UC02_POST_v2_digital_offices").on(
                    exec(DigitalOfficesCase.UC02_POST_v2_digital_offices));

    public static ChainBuilder UC03_PATCH_v2_digital_offices_batch_identical =
            group("UC03_PATCH_v2_digital_offices_batch_identical").on(
                    exec(DigitalOfficesCase.UC03_PATCH_v2_digital_offices_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_digital_offices_By_id =
            group("UC04_DELETE_v2_digital_offices_By_id").on(
                    exec(DigitalOfficesCase.UC04_DELETE_v2_digital_offices_By_id));

    public static ChainBuilder UC05_GET_v2_digital_offices_By_id =
            group("UC05_GET_v2_digital_offices_By_id").on(
                    exec(DigitalOfficesCase.UC05_GET_v2_digital_offices_By_id));

    public static ChainBuilder UC06_PATCH_v2_digital_offices_By_id =
            group("UC06_PATCH_v2_digital_offices_By_id").on(
                    exec(DigitalOfficesCase.UC06_PATCH_v2_digital_offices_By_id));

    public static ChainBuilder UC07_PUT_v2_digital_offices_By_id =
            group("UC07_PUT_v2_digital_offices_By_id").on(
                    exec(DigitalOfficesCase.UC07_PUT_v2_digital_offices_By_id));

    public static ScenarioBuilder scn = scenario("DigitalOffices")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_digital_offices),
                            new Choice.WithWeight(14, UC02_POST_v2_digital_offices),
                            new Choice.WithWeight(14, UC03_PATCH_v2_digital_offices_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_digital_offices_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_digital_offices_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_digital_offices_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_digital_offices_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug DigitalOffices")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(DigitalOfficesCase.UC01_GET_v2_digital_offices)
            .exec(DigitalOfficesCase.UC02_POST_v2_digital_offices)
            .exec(DigitalOfficesCase.UC03_PATCH_v2_digital_offices_batch_identical)
            .exec(DigitalOfficesCase.UC04_DELETE_v2_digital_offices_By_id)
            .exec(DigitalOfficesCase.UC05_GET_v2_digital_offices_By_id)
            .exec(DigitalOfficesCase.UC06_PATCH_v2_digital_offices_By_id)
            .exec(DigitalOfficesCase.UC07_PUT_v2_digital_offices_By_id);
}
