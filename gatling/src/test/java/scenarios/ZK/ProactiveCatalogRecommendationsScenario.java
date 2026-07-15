package scenarios.ZK;

import cases.ZK.ProactiveCatalogRecommendationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.proactiveCatalogRecommendations;
import static feeders.ZK.ZKFeeder.proactiveCatalogRecommendations;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ProactiveCatalogRecommendationsScenario {

    public static ChainBuilder UC01_GET_v1_proactive_catalog_recommendations =
            group("UC01_GET_v1_proactive_catalog_recommendations").on(
                    exec(ProactiveCatalogRecommendationsCase.UC01_GET_v1_proactive_catalog_recommendations));

    public static ChainBuilder UC02_POST_v1_proactive_catalog_recommendations =
            group("UC02_POST_v1_proactive_catalog_recommendations").on(
                    exec(ProactiveCatalogRecommendationsCase.UC02_POST_v1_proactive_catalog_recommendations));

    public static ChainBuilder UC03_PATCH_v1_proactive_catalog_recommendations_batch_identical =
            group("UC03_PATCH_v1_proactive_catalog_recommendations_batch_identical").on(
                    exec(ProactiveCatalogRecommendationsCase.UC03_PATCH_v1_proactive_catalog_recommendations_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_proactive_catalog_recommendations_By_id =
            group("UC04_DELETE_v1_proactive_catalog_recommendations_By_id").on(
                    exec(ProactiveCatalogRecommendationsCase.UC04_DELETE_v1_proactive_catalog_recommendations_By_id));

    public static ChainBuilder UC05_GET_v1_proactive_catalog_recommendations_By_id =
            group("UC05_GET_v1_proactive_catalog_recommendations_By_id").on(
                    exec(ProactiveCatalogRecommendationsCase.UC05_GET_v1_proactive_catalog_recommendations_By_id));

    public static ChainBuilder UC06_PATCH_v1_proactive_catalog_recommendations_By_id =
            group("UC06_PATCH_v1_proactive_catalog_recommendations_By_id").on(
                    exec(ProactiveCatalogRecommendationsCase.UC06_PATCH_v1_proactive_catalog_recommendations_By_id));

    public static ChainBuilder UC07_PUT_v1_proactive_catalog_recommendations_By_id =
            group("UC07_PUT_v1_proactive_catalog_recommendations_By_id").on(
                    exec(ProactiveCatalogRecommendationsCase.UC07_PUT_v1_proactive_catalog_recommendations_By_id));

    public static ScenarioBuilder scn = scenario("ProactiveCatalogRecommendations")
            .feed(defaultFeeder)
            .feed(proactiveCatalogRecommendations)
            .feed(proactiveCatalogRecommendations)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_proactive_catalog_recommendations),
                            new Choice.WithWeight(14, UC02_POST_v1_proactive_catalog_recommendations),
                            new Choice.WithWeight(14, UC03_PATCH_v1_proactive_catalog_recommendations_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_proactive_catalog_recommendations_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_proactive_catalog_recommendations_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_proactive_catalog_recommendations_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_proactive_catalog_recommendations_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ProactiveCatalogRecommendations")
            .feed(defaultFeeder)
            .feed(proactiveCatalogRecommendations)
            .feed(proactiveCatalogRecommendations)
            .feed(rqUidsFeeder)
            .exec(ProactiveCatalogRecommendationsCase.UC01_GET_v1_proactive_catalog_recommendations)
            .exec(ProactiveCatalogRecommendationsCase.UC02_POST_v1_proactive_catalog_recommendations)
            .exec(ProactiveCatalogRecommendationsCase.UC03_PATCH_v1_proactive_catalog_recommendations_batch_identical)
            .exec(ProactiveCatalogRecommendationsCase.UC04_DELETE_v1_proactive_catalog_recommendations_By_id)
            .exec(ProactiveCatalogRecommendationsCase.UC05_GET_v1_proactive_catalog_recommendations_By_id)
            .exec(ProactiveCatalogRecommendationsCase.UC06_PATCH_v1_proactive_catalog_recommendations_By_id)
            .exec(ProactiveCatalogRecommendationsCase.UC07_PUT_v1_proactive_catalog_recommendations_By_id);
}
