package scenarios.ZK;

import cases.ZK.ComplianceCasesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceCasesScenario {

    public static ChainBuilder UC01_GET_v2_compliance_cases =
            group("UC01_GET_v2_compliance_cases").on(
                    exec(ComplianceCasesCase.UC01_GET_v2_compliance_cases));

    public static ChainBuilder UC02_POST_v2_compliance_cases =
            group("UC02_POST_v2_compliance_cases").on(
                    exec(ComplianceCasesCase.UC02_POST_v2_compliance_cases));

    public static ChainBuilder UC03_PATCH_v2_compliance_cases_batch_identical =
            group("UC03_PATCH_v2_compliance_cases_batch_identical").on(
                    exec(ComplianceCasesCase.UC03_PATCH_v2_compliance_cases_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_cases_By_id =
            group("UC04_DELETE_v2_compliance_cases_By_id").on(
                    exec(ComplianceCasesCase.UC04_DELETE_v2_compliance_cases_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_cases_By_id =
            group("UC05_GET_v2_compliance_cases_By_id").on(
                    exec(ComplianceCasesCase.UC05_GET_v2_compliance_cases_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_cases_By_id =
            group("UC06_PATCH_v2_compliance_cases_By_id").on(
                    exec(ComplianceCasesCase.UC06_PATCH_v2_compliance_cases_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_cases_By_id =
            group("UC07_PUT_v2_compliance_cases_By_id").on(
                    exec(ComplianceCasesCase.UC07_PUT_v2_compliance_cases_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceCases")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_cases),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_cases),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_cases_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_cases_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_cases_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_cases_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_cases_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceCases")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceCasesCase.UC01_GET_v2_compliance_cases)
            .exec(ComplianceCasesCase.UC02_POST_v2_compliance_cases)
            .exec(ComplianceCasesCase.UC03_PATCH_v2_compliance_cases_batch_identical)
            .exec(ComplianceCasesCase.UC04_DELETE_v2_compliance_cases_By_id)
            .exec(ComplianceCasesCase.UC05_GET_v2_compliance_cases_By_id)
            .exec(ComplianceCasesCase.UC06_PATCH_v2_compliance_cases_By_id)
            .exec(ComplianceCasesCase.UC07_PUT_v2_compliance_cases_By_id);
}
