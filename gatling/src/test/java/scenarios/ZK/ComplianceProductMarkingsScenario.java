package scenarios.ZK;

import cases.ZK.ComplianceProductMarkingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceProductMarkings;
import static feeders.ZK.ZKFeeder.complianceProductMarkings;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceProductMarkingsScenario {

    public static ChainBuilder UC01_GET_v2_compliance_product_markings =
            group("UC01_GET_v2_compliance_product_markings").on(
                    exec(ComplianceProductMarkingsCase.UC01_GET_v2_compliance_product_markings));

    public static ChainBuilder UC02_POST_v2_compliance_product_markings =
            group("UC02_POST_v2_compliance_product_markings").on(
                    exec(ComplianceProductMarkingsCase.UC02_POST_v2_compliance_product_markings));

    public static ChainBuilder UC03_PATCH_v2_compliance_product_markings_batch_identical =
            group("UC03_PATCH_v2_compliance_product_markings_batch_identical").on(
                    exec(ComplianceProductMarkingsCase.UC03_PATCH_v2_compliance_product_markings_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_product_markings_By_id =
            group("UC04_DELETE_v2_compliance_product_markings_By_id").on(
                    exec(ComplianceProductMarkingsCase.UC04_DELETE_v2_compliance_product_markings_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_product_markings_By_id =
            group("UC05_GET_v2_compliance_product_markings_By_id").on(
                    exec(ComplianceProductMarkingsCase.UC05_GET_v2_compliance_product_markings_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_product_markings_By_id =
            group("UC06_PATCH_v2_compliance_product_markings_By_id").on(
                    exec(ComplianceProductMarkingsCase.UC06_PATCH_v2_compliance_product_markings_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_product_markings_By_id =
            group("UC07_PUT_v2_compliance_product_markings_By_id").on(
                    exec(ComplianceProductMarkingsCase.UC07_PUT_v2_compliance_product_markings_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceProductMarkings")
            .feed(defaultFeeder)
            .feed(complianceProductMarkings)
            .feed(complianceProductMarkings)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_product_markings),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_product_markings),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_product_markings_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_product_markings_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_product_markings_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_product_markings_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_product_markings_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceProductMarkings")
            .feed(defaultFeeder)
            .feed(complianceProductMarkings)
            .feed(complianceProductMarkings)
            .feed(rqUidsFeeder)
            .exec(ComplianceProductMarkingsCase.UC01_GET_v2_compliance_product_markings)
            .exec(ComplianceProductMarkingsCase.UC02_POST_v2_compliance_product_markings)
            .exec(ComplianceProductMarkingsCase.UC03_PATCH_v2_compliance_product_markings_batch_identical)
            .exec(ComplianceProductMarkingsCase.UC04_DELETE_v2_compliance_product_markings_By_id)
            .exec(ComplianceProductMarkingsCase.UC05_GET_v2_compliance_product_markings_By_id)
            .exec(ComplianceProductMarkingsCase.UC06_PATCH_v2_compliance_product_markings_By_id)
            .exec(ComplianceProductMarkingsCase.UC07_PUT_v2_compliance_product_markings_By_id);
}
