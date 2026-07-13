package scenarios.ZK;

import cases.ZK.FinOperationsAttributesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class FinOperationsAttributesScenario {

    public static ChainBuilder UC01_GET_v2_fin_operations_attributes =
            group("UC01_GET_v2_fin_operations_attributes").on(
                    exec(FinOperationsAttributesCase.UC01_GET_v2_fin_operations_attributes));

    public static ChainBuilder UC02_POST_v2_fin_operations_attributes =
            group("UC02_POST_v2_fin_operations_attributes").on(
                    exec(FinOperationsAttributesCase.UC02_POST_v2_fin_operations_attributes));

    public static ChainBuilder UC03_PATCH_v2_fin_operations_attributes_batch_identical =
            group("UC03_PATCH_v2_fin_operations_attributes_batch_identical").on(
                    exec(FinOperationsAttributesCase.UC03_PATCH_v2_fin_operations_attributes_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_fin_operations_attributes_By_id =
            group("UC04_DELETE_v2_fin_operations_attributes_By_id").on(
                    exec(FinOperationsAttributesCase.UC04_DELETE_v2_fin_operations_attributes_By_id));

    public static ChainBuilder UC05_GET_v2_fin_operations_attributes_By_id =
            group("UC05_GET_v2_fin_operations_attributes_By_id").on(
                    exec(FinOperationsAttributesCase.UC05_GET_v2_fin_operations_attributes_By_id));

    public static ChainBuilder UC06_PATCH_v2_fin_operations_attributes_By_id =
            group("UC06_PATCH_v2_fin_operations_attributes_By_id").on(
                    exec(FinOperationsAttributesCase.UC06_PATCH_v2_fin_operations_attributes_By_id));

    public static ChainBuilder UC07_PUT_v2_fin_operations_attributes_By_id =
            group("UC07_PUT_v2_fin_operations_attributes_By_id").on(
                    exec(FinOperationsAttributesCase.UC07_PUT_v2_fin_operations_attributes_By_id));

    public static ScenarioBuilder scn = scenario("FinOperationsAttributes")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_fin_operations_attributes),
                            new Choice.WithWeight(14, UC02_POST_v2_fin_operations_attributes),
                            new Choice.WithWeight(14, UC03_PATCH_v2_fin_operations_attributes_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_fin_operations_attributes_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_fin_operations_attributes_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_fin_operations_attributes_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_fin_operations_attributes_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug FinOperationsAttributes")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(FinOperationsAttributesCase.UC01_GET_v2_fin_operations_attributes)
            .exec(FinOperationsAttributesCase.UC02_POST_v2_fin_operations_attributes)
            .exec(FinOperationsAttributesCase.UC03_PATCH_v2_fin_operations_attributes_batch_identical)
            .exec(FinOperationsAttributesCase.UC04_DELETE_v2_fin_operations_attributes_By_id)
            .exec(FinOperationsAttributesCase.UC05_GET_v2_fin_operations_attributes_By_id)
            .exec(FinOperationsAttributesCase.UC06_PATCH_v2_fin_operations_attributes_By_id)
            .exec(FinOperationsAttributesCase.UC07_PUT_v2_fin_operations_attributes_By_id);
}
