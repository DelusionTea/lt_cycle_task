package scenarios.ZK;

import cases.ZK.ComplianceCaseMarkingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceCaseMarkingsScenario {

    public static ChainBuilder UC01_GET_v2_compliance_case_markings =
            group("UC01_GET_v2_compliance_case_markings").on(
                    exec(ComplianceCaseMarkingsCase.UC01_GET_v2_compliance_case_markings));

    public static ChainBuilder UC02_POST_v2_compliance_case_markings =
            group("UC02_POST_v2_compliance_case_markings").on(
                    exec(ComplianceCaseMarkingsCase.UC02_POST_v2_compliance_case_markings));

    public static ChainBuilder UC03_PATCH_v2_compliance_case_markings_batch_identical =
            group("UC03_PATCH_v2_compliance_case_markings_batch_identical").on(
                    exec(ComplianceCaseMarkingsCase.UC03_PATCH_v2_compliance_case_markings_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_case_markings_By_id =
            group("UC04_DELETE_v2_compliance_case_markings_By_id").on(
                    exec(ComplianceCaseMarkingsCase.UC04_DELETE_v2_compliance_case_markings_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_case_markings_By_id =
            group("UC05_GET_v2_compliance_case_markings_By_id").on(
                    exec(ComplianceCaseMarkingsCase.UC05_GET_v2_compliance_case_markings_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_case_markings_By_id =
            group("UC06_PATCH_v2_compliance_case_markings_By_id").on(
                    exec(ComplianceCaseMarkingsCase.UC06_PATCH_v2_compliance_case_markings_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_case_markings_By_id =
            group("UC07_PUT_v2_compliance_case_markings_By_id").on(
                    exec(ComplianceCaseMarkingsCase.UC07_PUT_v2_compliance_case_markings_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceCaseMarkings")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_case_markings),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_case_markings),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_case_markings_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_case_markings_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_case_markings_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_case_markings_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_case_markings_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceCaseMarkings")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceCaseMarkingsCase.UC01_GET_v2_compliance_case_markings)
            .exec(ComplianceCaseMarkingsCase.UC02_POST_v2_compliance_case_markings)
            .exec(ComplianceCaseMarkingsCase.UC03_PATCH_v2_compliance_case_markings_batch_identical)
            .exec(ComplianceCaseMarkingsCase.UC04_DELETE_v2_compliance_case_markings_By_id)
            .exec(ComplianceCaseMarkingsCase.UC05_GET_v2_compliance_case_markings_By_id)
            .exec(ComplianceCaseMarkingsCase.UC06_PATCH_v2_compliance_case_markings_By_id)
            .exec(ComplianceCaseMarkingsCase.UC07_PUT_v2_compliance_case_markings_By_id);
}
