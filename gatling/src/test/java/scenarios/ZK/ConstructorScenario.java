package scenarios.ZK;

import cases.ZK.ConstructorCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ConstructorScenario {

    public static ChainBuilder UC01_GET_v1_constructor =
            group("UC01_GET_v1_constructor").on(
                    exec(ConstructorCase.UC01_GET_v1_constructor));

    public static ChainBuilder UC02_POST_v1_constructor =
            group("UC02_POST_v1_constructor").on(
                    exec(ConstructorCase.UC02_POST_v1_constructor));

    public static ChainBuilder UC03_PATCH_v1_constructor_batch_identical =
            group("UC03_PATCH_v1_constructor_batch_identical").on(
                    exec(ConstructorCase.UC03_PATCH_v1_constructor_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_constructor_By_id =
            group("UC04_DELETE_v1_constructor_By_id").on(
                    exec(ConstructorCase.UC04_DELETE_v1_constructor_By_id));

    public static ChainBuilder UC05_GET_v1_constructor_By_id =
            group("UC05_GET_v1_constructor_By_id").on(
                    exec(ConstructorCase.UC05_GET_v1_constructor_By_id));

    public static ChainBuilder UC06_PATCH_v1_constructor_By_id =
            group("UC06_PATCH_v1_constructor_By_id").on(
                    exec(ConstructorCase.UC06_PATCH_v1_constructor_By_id));

    public static ChainBuilder UC07_PUT_v1_constructor_By_id =
            group("UC07_PUT_v1_constructor_By_id").on(
                    exec(ConstructorCase.UC07_PUT_v1_constructor_By_id));

    public static ScenarioBuilder scn = scenario("Constructor")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_constructor),
                            new Choice.WithWeight(14, UC02_POST_v1_constructor),
                            new Choice.WithWeight(14, UC03_PATCH_v1_constructor_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_constructor_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_constructor_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_constructor_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_constructor_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Constructor")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ConstructorCase.UC01_GET_v1_constructor)
            .exec(ConstructorCase.UC02_POST_v1_constructor)
            .exec(ConstructorCase.UC03_PATCH_v1_constructor_batch_identical)
            .exec(ConstructorCase.UC04_DELETE_v1_constructor_By_id)
            .exec(ConstructorCase.UC05_GET_v1_constructor_By_id)
            .exec(ConstructorCase.UC06_PATCH_v1_constructor_By_id)
            .exec(ConstructorCase.UC07_PUT_v1_constructor_By_id);
}
