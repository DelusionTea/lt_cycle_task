package scenarios.ZK;

import cases.ZK.CompliancePrintedFormConfigCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class CompliancePrintedFormConfigScenario {

    public static ChainBuilder UC01_GET_v2_compliance_printed_form_config =
            group("UC01_GET_v2_compliance_printed_form_config").on(
                    exec(CompliancePrintedFormConfigCase.UC01_GET_v2_compliance_printed_form_config));

    public static ChainBuilder UC02_POST_v2_compliance_printed_form_config =
            group("UC02_POST_v2_compliance_printed_form_config").on(
                    exec(CompliancePrintedFormConfigCase.UC02_POST_v2_compliance_printed_form_config));

    public static ChainBuilder UC03_PATCH_v2_compliance_printed_form_config_batch_identical =
            group("UC03_PATCH_v2_compliance_printed_form_config_batch_identical").on(
                    exec(CompliancePrintedFormConfigCase.UC03_PATCH_v2_compliance_printed_form_config_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_printed_form_config_By_id =
            group("UC04_DELETE_v2_compliance_printed_form_config_By_id").on(
                    exec(CompliancePrintedFormConfigCase.UC04_DELETE_v2_compliance_printed_form_config_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_printed_form_config_By_id =
            group("UC05_GET_v2_compliance_printed_form_config_By_id").on(
                    exec(CompliancePrintedFormConfigCase.UC05_GET_v2_compliance_printed_form_config_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_printed_form_config_By_id =
            group("UC06_PATCH_v2_compliance_printed_form_config_By_id").on(
                    exec(CompliancePrintedFormConfigCase.UC06_PATCH_v2_compliance_printed_form_config_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_printed_form_config_By_id =
            group("UC07_PUT_v2_compliance_printed_form_config_By_id").on(
                    exec(CompliancePrintedFormConfigCase.UC07_PUT_v2_compliance_printed_form_config_By_id));

    public static ScenarioBuilder scn = scenario("CompliancePrintedFormConfig")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_printed_form_config),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_printed_form_config),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_printed_form_config_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_printed_form_config_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_printed_form_config_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_printed_form_config_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_printed_form_config_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug CompliancePrintedFormConfig")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(CompliancePrintedFormConfigCase.UC01_GET_v2_compliance_printed_form_config)
            .exec(CompliancePrintedFormConfigCase.UC02_POST_v2_compliance_printed_form_config)
            .exec(CompliancePrintedFormConfigCase.UC03_PATCH_v2_compliance_printed_form_config_batch_identical)
            .exec(CompliancePrintedFormConfigCase.UC04_DELETE_v2_compliance_printed_form_config_By_id)
            .exec(CompliancePrintedFormConfigCase.UC05_GET_v2_compliance_printed_form_config_By_id)
            .exec(CompliancePrintedFormConfigCase.UC06_PATCH_v2_compliance_printed_form_config_By_id)
            .exec(CompliancePrintedFormConfigCase.UC07_PUT_v2_compliance_printed_form_config_By_id);
}
