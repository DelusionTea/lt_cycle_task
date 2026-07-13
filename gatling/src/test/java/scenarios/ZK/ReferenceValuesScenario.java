package scenarios.ZK;

import cases.ZK.ReferenceValuesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ReferenceValuesScenario {

    public static ChainBuilder UC01_GET_v2_reference_values =
            group("UC01_GET_v2_reference_values").on(
                    exec(ReferenceValuesCase.UC01_GET_v2_reference_values));

    public static ChainBuilder UC02_POST_v2_reference_values =
            group("UC02_POST_v2_reference_values").on(
                    exec(ReferenceValuesCase.UC02_POST_v2_reference_values));

    public static ChainBuilder UC03_PATCH_v2_reference_values_batch_identical =
            group("UC03_PATCH_v2_reference_values_batch_identical").on(
                    exec(ReferenceValuesCase.UC03_PATCH_v2_reference_values_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_reference_values_By_id =
            group("UC04_DELETE_v2_reference_values_By_id").on(
                    exec(ReferenceValuesCase.UC04_DELETE_v2_reference_values_By_id));

    public static ChainBuilder UC05_GET_v2_reference_values_By_id =
            group("UC05_GET_v2_reference_values_By_id").on(
                    exec(ReferenceValuesCase.UC05_GET_v2_reference_values_By_id));

    public static ChainBuilder UC06_PATCH_v2_reference_values_By_id =
            group("UC06_PATCH_v2_reference_values_By_id").on(
                    exec(ReferenceValuesCase.UC06_PATCH_v2_reference_values_By_id));

    public static ChainBuilder UC07_PUT_v2_reference_values_By_id =
            group("UC07_PUT_v2_reference_values_By_id").on(
                    exec(ReferenceValuesCase.UC07_PUT_v2_reference_values_By_id));

    public static ScenarioBuilder scn = scenario("ReferenceValues")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_reference_values),
                            new Choice.WithWeight(14, UC02_POST_v2_reference_values),
                            new Choice.WithWeight(14, UC03_PATCH_v2_reference_values_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_reference_values_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_reference_values_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_reference_values_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_reference_values_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ReferenceValues")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ReferenceValuesCase.UC01_GET_v2_reference_values)
            .exec(ReferenceValuesCase.UC02_POST_v2_reference_values)
            .exec(ReferenceValuesCase.UC03_PATCH_v2_reference_values_batch_identical)
            .exec(ReferenceValuesCase.UC04_DELETE_v2_reference_values_By_id)
            .exec(ReferenceValuesCase.UC05_GET_v2_reference_values_By_id)
            .exec(ReferenceValuesCase.UC06_PATCH_v2_reference_values_By_id)
            .exec(ReferenceValuesCase.UC07_PUT_v2_reference_values_By_id);
}
