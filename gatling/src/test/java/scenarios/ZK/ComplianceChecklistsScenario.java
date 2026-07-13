package scenarios.ZK;

import cases.ZK.ComplianceChecklistsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceChecklistsScenario {

    public static ChainBuilder UC01_POST_v2_compliance_checklists_comments =
            group("UC01_POST_v2_compliance_checklists_comments").on(
                    exec(ComplianceChecklistsCase.UC01_POST_v2_compliance_checklists_comments));

    public static ChainBuilder UC02_PATCH_v2_compliance_checklists_comments_By_id =
            group("UC02_PATCH_v2_compliance_checklists_comments_By_id").on(
                    exec(ComplianceChecklistsCase.UC02_PATCH_v2_compliance_checklists_comments_By_id));

    public static ChainBuilder UC03_GET_v2_compliance_checklists_By_requestId =
            group("UC03_GET_v2_compliance_checklists_By_requestId").on(
                    exec(ComplianceChecklistsCase.UC03_GET_v2_compliance_checklists_By_requestId));

    public static ScenarioBuilder scn = scenario("ComplianceChecklists")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(33, UC01_POST_v2_compliance_checklists_comments),
                            new Choice.WithWeight(33, UC02_PATCH_v2_compliance_checklists_comments_By_id),
                            new Choice.WithWeight(33, UC03_GET_v2_compliance_checklists_By_requestId)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceChecklists")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceChecklistsCase.UC01_POST_v2_compliance_checklists_comments)
            .exec(ComplianceChecklistsCase.UC02_PATCH_v2_compliance_checklists_comments_By_id)
            .exec(ComplianceChecklistsCase.UC03_GET_v2_compliance_checklists_By_requestId);
}
