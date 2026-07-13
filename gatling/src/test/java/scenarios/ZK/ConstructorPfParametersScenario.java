package scenarios.ZK;

import cases.ZK.ConstructorPfParametersCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ConstructorPfParametersScenario {

    public static ChainBuilder UC01_GET_v1_constructor_pf_parameters =
            group("UC01_GET_v1_constructor_pf_parameters").on(
                    exec(ConstructorPfParametersCase.UC01_GET_v1_constructor_pf_parameters));

    public static ChainBuilder UC02_POST_v1_constructor_pf_parameters =
            group("UC02_POST_v1_constructor_pf_parameters").on(
                    exec(ConstructorPfParametersCase.UC02_POST_v1_constructor_pf_parameters));

    public static ChainBuilder UC03_PATCH_v1_constructor_pf_parameters_batch_identical =
            group("UC03_PATCH_v1_constructor_pf_parameters_batch_identical").on(
                    exec(ConstructorPfParametersCase.UC03_PATCH_v1_constructor_pf_parameters_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_constructor_pf_parameters_By_id =
            group("UC04_DELETE_v1_constructor_pf_parameters_By_id").on(
                    exec(ConstructorPfParametersCase.UC04_DELETE_v1_constructor_pf_parameters_By_id));

    public static ChainBuilder UC05_GET_v1_constructor_pf_parameters_By_id =
            group("UC05_GET_v1_constructor_pf_parameters_By_id").on(
                    exec(ConstructorPfParametersCase.UC05_GET_v1_constructor_pf_parameters_By_id));

    public static ChainBuilder UC06_PATCH_v1_constructor_pf_parameters_By_id =
            group("UC06_PATCH_v1_constructor_pf_parameters_By_id").on(
                    exec(ConstructorPfParametersCase.UC06_PATCH_v1_constructor_pf_parameters_By_id));

    public static ChainBuilder UC07_PUT_v1_constructor_pf_parameters_By_id =
            group("UC07_PUT_v1_constructor_pf_parameters_By_id").on(
                    exec(ConstructorPfParametersCase.UC07_PUT_v1_constructor_pf_parameters_By_id));

    public static ScenarioBuilder scn = scenario("ConstructorPfParameters")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_constructor_pf_parameters),
                            new Choice.WithWeight(14, UC02_POST_v1_constructor_pf_parameters),
                            new Choice.WithWeight(14, UC03_PATCH_v1_constructor_pf_parameters_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_constructor_pf_parameters_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_constructor_pf_parameters_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_constructor_pf_parameters_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_constructor_pf_parameters_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ConstructorPfParameters")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ConstructorPfParametersCase.UC01_GET_v1_constructor_pf_parameters)
            .exec(ConstructorPfParametersCase.UC02_POST_v1_constructor_pf_parameters)
            .exec(ConstructorPfParametersCase.UC03_PATCH_v1_constructor_pf_parameters_batch_identical)
            .exec(ConstructorPfParametersCase.UC04_DELETE_v1_constructor_pf_parameters_By_id)
            .exec(ConstructorPfParametersCase.UC05_GET_v1_constructor_pf_parameters_By_id)
            .exec(ConstructorPfParametersCase.UC06_PATCH_v1_constructor_pf_parameters_By_id)
            .exec(ConstructorPfParametersCase.UC07_PUT_v1_constructor_pf_parameters_By_id);
}
