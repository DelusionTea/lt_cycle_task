package scenarios.ZK;

import cases.ZK.ComplianceTemplatesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceTemplates;
import static feeders.ZK.ZKFeeder.complianceTemplates;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceTemplatesScenario {

    public static ChainBuilder UC01_GET_v2_compliance_templates =
            group("UC01_GET_v2_compliance_templates").on(
                    exec(ComplianceTemplatesCase.UC01_GET_v2_compliance_templates));

    public static ChainBuilder UC02_POST_v2_compliance_templates =
            group("UC02_POST_v2_compliance_templates").on(
                    exec(ComplianceTemplatesCase.UC02_POST_v2_compliance_templates));

    public static ChainBuilder UC03_PATCH_v2_compliance_templates_batch_identical =
            group("UC03_PATCH_v2_compliance_templates_batch_identical").on(
                    exec(ComplianceTemplatesCase.UC03_PATCH_v2_compliance_templates_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_templates_By_id =
            group("UC04_DELETE_v2_compliance_templates_By_id").on(
                    exec(ComplianceTemplatesCase.UC04_DELETE_v2_compliance_templates_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_templates_By_id =
            group("UC05_GET_v2_compliance_templates_By_id").on(
                    exec(ComplianceTemplatesCase.UC05_GET_v2_compliance_templates_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_templates_By_id =
            group("UC06_PATCH_v2_compliance_templates_By_id").on(
                    exec(ComplianceTemplatesCase.UC06_PATCH_v2_compliance_templates_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_templates_By_id =
            group("UC07_PUT_v2_compliance_templates_By_id").on(
                    exec(ComplianceTemplatesCase.UC07_PUT_v2_compliance_templates_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceTemplates")
            .feed(defaultFeeder)
            .feed(complianceTemplates)
            .feed(complianceTemplates)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_templates),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_templates),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_templates_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_templates_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_templates_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_templates_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_templates_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceTemplates")
            .feed(defaultFeeder)
            .feed(complianceTemplates)
            .feed(complianceTemplates)
            .feed(rqUidsFeeder)
            .exec(ComplianceTemplatesCase.UC01_GET_v2_compliance_templates)
            .exec(ComplianceTemplatesCase.UC02_POST_v2_compliance_templates)
            .exec(ComplianceTemplatesCase.UC03_PATCH_v2_compliance_templates_batch_identical)
            .exec(ComplianceTemplatesCase.UC04_DELETE_v2_compliance_templates_By_id)
            .exec(ComplianceTemplatesCase.UC05_GET_v2_compliance_templates_By_id)
            .exec(ComplianceTemplatesCase.UC06_PATCH_v2_compliance_templates_By_id)
            .exec(ComplianceTemplatesCase.UC07_PUT_v2_compliance_templates_By_id);
}
