package scenarios.ZK;

import cases.ZK.FinOperationsReferenceMappingCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.finOperationsReferenceMapping;
import static feeders.ZK.ZKFeeder.finOperationsReferenceMapping;
import static io.gatling.javaapi.core.CoreDsl.*;

public class FinOperationsReferenceMappingScenario {

    public static ChainBuilder UC01_GET_v1_fin_operations_reference_mapping =
            group("UC01_GET_v1_fin_operations_reference_mapping").on(
                    exec(FinOperationsReferenceMappingCase.UC01_GET_v1_fin_operations_reference_mapping));

    public static ChainBuilder UC02_POST_v1_fin_operations_reference_mapping =
            group("UC02_POST_v1_fin_operations_reference_mapping").on(
                    exec(FinOperationsReferenceMappingCase.UC02_POST_v1_fin_operations_reference_mapping));

    public static ChainBuilder UC03_PATCH_v1_fin_operations_reference_mapping_batch_identical =
            group("UC03_PATCH_v1_fin_operations_reference_mapping_batch_identical").on(
                    exec(FinOperationsReferenceMappingCase.UC03_PATCH_v1_fin_operations_reference_mapping_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_fin_operations_reference_mapping_By_id =
            group("UC04_DELETE_v1_fin_operations_reference_mapping_By_id").on(
                    exec(FinOperationsReferenceMappingCase.UC04_DELETE_v1_fin_operations_reference_mapping_By_id));

    public static ChainBuilder UC05_GET_v1_fin_operations_reference_mapping_By_id =
            group("UC05_GET_v1_fin_operations_reference_mapping_By_id").on(
                    exec(FinOperationsReferenceMappingCase.UC05_GET_v1_fin_operations_reference_mapping_By_id));

    public static ChainBuilder UC06_PATCH_v1_fin_operations_reference_mapping_By_id =
            group("UC06_PATCH_v1_fin_operations_reference_mapping_By_id").on(
                    exec(FinOperationsReferenceMappingCase.UC06_PATCH_v1_fin_operations_reference_mapping_By_id));

    public static ChainBuilder UC07_PUT_v1_fin_operations_reference_mapping_By_id =
            group("UC07_PUT_v1_fin_operations_reference_mapping_By_id").on(
                    exec(FinOperationsReferenceMappingCase.UC07_PUT_v1_fin_operations_reference_mapping_By_id));

    public static ScenarioBuilder scn = scenario("FinOperationsReferenceMapping")
            .feed(defaultFeeder)
            .feed(finOperationsReferenceMapping)
            .feed(finOperationsReferenceMapping)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_fin_operations_reference_mapping),
                            new Choice.WithWeight(14, UC02_POST_v1_fin_operations_reference_mapping),
                            new Choice.WithWeight(14, UC03_PATCH_v1_fin_operations_reference_mapping_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_fin_operations_reference_mapping_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_fin_operations_reference_mapping_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_fin_operations_reference_mapping_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_fin_operations_reference_mapping_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug FinOperationsReferenceMapping")
            .feed(defaultFeeder)
            .feed(finOperationsReferenceMapping)
            .feed(finOperationsReferenceMapping)
            .feed(rqUidsFeeder)
            .exec(FinOperationsReferenceMappingCase.UC01_GET_v1_fin_operations_reference_mapping)
            .exec(FinOperationsReferenceMappingCase.UC02_POST_v1_fin_operations_reference_mapping)
            .exec(FinOperationsReferenceMappingCase.UC03_PATCH_v1_fin_operations_reference_mapping_batch_identical)
            .exec(FinOperationsReferenceMappingCase.UC04_DELETE_v1_fin_operations_reference_mapping_By_id)
            .exec(FinOperationsReferenceMappingCase.UC05_GET_v1_fin_operations_reference_mapping_By_id)
            .exec(FinOperationsReferenceMappingCase.UC06_PATCH_v1_fin_operations_reference_mapping_By_id)
            .exec(FinOperationsReferenceMappingCase.UC07_PUT_v1_fin_operations_reference_mapping_By_id);
}
