package scenarios.ZK;

import cases.ZK.ComplianceTemplateParametersCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceTemplateParameters;
import static feeders.ZK.ZKFeeder.complianceTemplateParameters;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceTemplateParametersScenario {

    public static ChainBuilder UC01_GET_v2_compliance_template_parameters =
            group("UC01_GET_v2_compliance_template_parameters").on(
                    exec(ComplianceTemplateParametersCase.UC01_GET_v2_compliance_template_parameters));

    public static ChainBuilder UC02_POST_v2_compliance_template_parameters =
            group("UC02_POST_v2_compliance_template_parameters").on(
                    exec(ComplianceTemplateParametersCase.UC02_POST_v2_compliance_template_parameters));

    public static ChainBuilder UC03_PATCH_v2_compliance_template_parameters_batch_identical =
            group("UC03_PATCH_v2_compliance_template_parameters_batch_identical").on(
                    exec(ComplianceTemplateParametersCase.UC03_PATCH_v2_compliance_template_parameters_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_template_parameters_By_id =
            group("UC04_DELETE_v2_compliance_template_parameters_By_id").on(
                    exec(ComplianceTemplateParametersCase.UC04_DELETE_v2_compliance_template_parameters_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_template_parameters_By_id =
            group("UC05_GET_v2_compliance_template_parameters_By_id").on(
                    exec(ComplianceTemplateParametersCase.UC05_GET_v2_compliance_template_parameters_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_template_parameters_By_id =
            group("UC06_PATCH_v2_compliance_template_parameters_By_id").on(
                    exec(ComplianceTemplateParametersCase.UC06_PATCH_v2_compliance_template_parameters_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_template_parameters_By_id =
            group("UC07_PUT_v2_compliance_template_parameters_By_id").on(
                    exec(ComplianceTemplateParametersCase.UC07_PUT_v2_compliance_template_parameters_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceTemplateParameters")
            .feed(defaultFeeder)
            .feed(complianceTemplateParameters)
            .feed(complianceTemplateParameters)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_template_parameters),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_template_parameters),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_template_parameters_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_template_parameters_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_template_parameters_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_template_parameters_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_template_parameters_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceTemplateParameters")
            .feed(defaultFeeder)
            .feed(complianceTemplateParameters)
            .feed(complianceTemplateParameters)
            .feed(rqUidsFeeder)
            .exec(ComplianceTemplateParametersCase.UC01_GET_v2_compliance_template_parameters)
            .exec(ComplianceTemplateParametersCase.UC02_POST_v2_compliance_template_parameters)
            .exec(ComplianceTemplateParametersCase.UC03_PATCH_v2_compliance_template_parameters_batch_identical)
            .exec(ComplianceTemplateParametersCase.UC04_DELETE_v2_compliance_template_parameters_By_id)
            .exec(ComplianceTemplateParametersCase.UC05_GET_v2_compliance_template_parameters_By_id)
            .exec(ComplianceTemplateParametersCase.UC06_PATCH_v2_compliance_template_parameters_By_id)
            .exec(ComplianceTemplateParametersCase.UC07_PUT_v2_compliance_template_parameters_By_id);
}
