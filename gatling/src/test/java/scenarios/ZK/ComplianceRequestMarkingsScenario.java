package scenarios.ZK;

import cases.ZK.ComplianceRequestMarkingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceRequestMarkingsScenario {

    public static ChainBuilder UC01_GET_v2_compliance_request_markings =
            group("UC01_GET_v2_compliance_request_markings").on(
                    exec(ComplianceRequestMarkingsCase.UC01_GET_v2_compliance_request_markings));

    public static ChainBuilder UC02_POST_v2_compliance_request_markings =
            group("UC02_POST_v2_compliance_request_markings").on(
                    exec(ComplianceRequestMarkingsCase.UC02_POST_v2_compliance_request_markings));

    public static ChainBuilder UC03_PATCH_v2_compliance_request_markings_batch_identical =
            group("UC03_PATCH_v2_compliance_request_markings_batch_identical").on(
                    exec(ComplianceRequestMarkingsCase.UC03_PATCH_v2_compliance_request_markings_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_request_markings_By_id =
            group("UC04_DELETE_v2_compliance_request_markings_By_id").on(
                    exec(ComplianceRequestMarkingsCase.UC04_DELETE_v2_compliance_request_markings_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_request_markings_By_id =
            group("UC05_GET_v2_compliance_request_markings_By_id").on(
                    exec(ComplianceRequestMarkingsCase.UC05_GET_v2_compliance_request_markings_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_request_markings_By_id =
            group("UC06_PATCH_v2_compliance_request_markings_By_id").on(
                    exec(ComplianceRequestMarkingsCase.UC06_PATCH_v2_compliance_request_markings_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_request_markings_By_id =
            group("UC07_PUT_v2_compliance_request_markings_By_id").on(
                    exec(ComplianceRequestMarkingsCase.UC07_PUT_v2_compliance_request_markings_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceRequestMarkings")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_request_markings),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_request_markings),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_request_markings_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_request_markings_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_request_markings_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_request_markings_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_request_markings_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceRequestMarkings")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceRequestMarkingsCase.UC01_GET_v2_compliance_request_markings)
            .exec(ComplianceRequestMarkingsCase.UC02_POST_v2_compliance_request_markings)
            .exec(ComplianceRequestMarkingsCase.UC03_PATCH_v2_compliance_request_markings_batch_identical)
            .exec(ComplianceRequestMarkingsCase.UC04_DELETE_v2_compliance_request_markings_By_id)
            .exec(ComplianceRequestMarkingsCase.UC05_GET_v2_compliance_request_markings_By_id)
            .exec(ComplianceRequestMarkingsCase.UC06_PATCH_v2_compliance_request_markings_By_id)
            .exec(ComplianceRequestMarkingsCase.UC07_PUT_v2_compliance_request_markings_By_id);
}
