package scenarios.ZK;

import cases.ZK.ComplianceRecommendationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceRecommendationsScenario {

    public static ChainBuilder UC01_GET_v2_compliance_recommendations =
            group("UC01_GET_v2_compliance_recommendations").on(
                    exec(ComplianceRecommendationsCase.UC01_GET_v2_compliance_recommendations));

    public static ChainBuilder UC02_POST_v2_compliance_recommendations =
            group("UC02_POST_v2_compliance_recommendations").on(
                    exec(ComplianceRecommendationsCase.UC02_POST_v2_compliance_recommendations));

    public static ChainBuilder UC03_PATCH_v2_compliance_recommendations_batch_identical =
            group("UC03_PATCH_v2_compliance_recommendations_batch_identical").on(
                    exec(ComplianceRecommendationsCase.UC03_PATCH_v2_compliance_recommendations_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_recommendations_By_id =
            group("UC04_DELETE_v2_compliance_recommendations_By_id").on(
                    exec(ComplianceRecommendationsCase.UC04_DELETE_v2_compliance_recommendations_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_recommendations_By_id =
            group("UC05_GET_v2_compliance_recommendations_By_id").on(
                    exec(ComplianceRecommendationsCase.UC05_GET_v2_compliance_recommendations_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_recommendations_By_id =
            group("UC06_PATCH_v2_compliance_recommendations_By_id").on(
                    exec(ComplianceRecommendationsCase.UC06_PATCH_v2_compliance_recommendations_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_recommendations_By_id =
            group("UC07_PUT_v2_compliance_recommendations_By_id").on(
                    exec(ComplianceRecommendationsCase.UC07_PUT_v2_compliance_recommendations_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceRecommendations")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_recommendations),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_recommendations),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_recommendations_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_recommendations_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_recommendations_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_recommendations_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_recommendations_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceRecommendations")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceRecommendationsCase.UC01_GET_v2_compliance_recommendations)
            .exec(ComplianceRecommendationsCase.UC02_POST_v2_compliance_recommendations)
            .exec(ComplianceRecommendationsCase.UC03_PATCH_v2_compliance_recommendations_batch_identical)
            .exec(ComplianceRecommendationsCase.UC04_DELETE_v2_compliance_recommendations_By_id)
            .exec(ComplianceRecommendationsCase.UC05_GET_v2_compliance_recommendations_By_id)
            .exec(ComplianceRecommendationsCase.UC06_PATCH_v2_compliance_recommendations_By_id)
            .exec(ComplianceRecommendationsCase.UC07_PUT_v2_compliance_recommendations_By_id);
}
