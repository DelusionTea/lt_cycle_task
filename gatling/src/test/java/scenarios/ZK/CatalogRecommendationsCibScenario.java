package scenarios.ZK;

import cases.ZK.CatalogRecommendationsCibCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.catalogRecommendationsCib;
import static feeders.ZK.ZKFeeder.catalogRecommendationsCib;
import static io.gatling.javaapi.core.CoreDsl.*;

public class CatalogRecommendationsCibScenario {

    public static ChainBuilder UC01_GET_v2_catalog_recommendations_cib =
            group("UC01_GET_v2_catalog_recommendations_cib").on(
                    exec(CatalogRecommendationsCibCase.UC01_GET_v2_catalog_recommendations_cib));

    public static ChainBuilder UC02_POST_v2_catalog_recommendations_cib =
            group("UC02_POST_v2_catalog_recommendations_cib").on(
                    exec(CatalogRecommendationsCibCase.UC02_POST_v2_catalog_recommendations_cib));

    public static ChainBuilder UC03_PATCH_v2_catalog_recommendations_cib_batch_identical =
            group("UC03_PATCH_v2_catalog_recommendations_cib_batch_identical").on(
                    exec(CatalogRecommendationsCibCase.UC03_PATCH_v2_catalog_recommendations_cib_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_catalog_recommendations_cib_By_id =
            group("UC04_DELETE_v2_catalog_recommendations_cib_By_id").on(
                    exec(CatalogRecommendationsCibCase.UC04_DELETE_v2_catalog_recommendations_cib_By_id));

    public static ChainBuilder UC05_GET_v2_catalog_recommendations_cib_By_id =
            group("UC05_GET_v2_catalog_recommendations_cib_By_id").on(
                    exec(CatalogRecommendationsCibCase.UC05_GET_v2_catalog_recommendations_cib_By_id));

    public static ChainBuilder UC06_PATCH_v2_catalog_recommendations_cib_By_id =
            group("UC06_PATCH_v2_catalog_recommendations_cib_By_id").on(
                    exec(CatalogRecommendationsCibCase.UC06_PATCH_v2_catalog_recommendations_cib_By_id));

    public static ChainBuilder UC07_PUT_v2_catalog_recommendations_cib_By_id =
            group("UC07_PUT_v2_catalog_recommendations_cib_By_id").on(
                    exec(CatalogRecommendationsCibCase.UC07_PUT_v2_catalog_recommendations_cib_By_id));

    public static ScenarioBuilder scn = scenario("CatalogRecommendationsCib")
            .feed(defaultFeeder)
            .feed(catalogRecommendationsCib)
            .feed(catalogRecommendationsCib)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_catalog_recommendations_cib),
                            new Choice.WithWeight(14, UC02_POST_v2_catalog_recommendations_cib),
                            new Choice.WithWeight(14, UC03_PATCH_v2_catalog_recommendations_cib_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_catalog_recommendations_cib_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_catalog_recommendations_cib_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_catalog_recommendations_cib_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_catalog_recommendations_cib_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug CatalogRecommendationsCib")
            .feed(defaultFeeder)
            .feed(catalogRecommendationsCib)
            .feed(catalogRecommendationsCib)
            .feed(rqUidsFeeder)
            .exec(CatalogRecommendationsCibCase.UC01_GET_v2_catalog_recommendations_cib)
            .exec(CatalogRecommendationsCibCase.UC02_POST_v2_catalog_recommendations_cib)
            .exec(CatalogRecommendationsCibCase.UC03_PATCH_v2_catalog_recommendations_cib_batch_identical)
            .exec(CatalogRecommendationsCibCase.UC04_DELETE_v2_catalog_recommendations_cib_By_id)
            .exec(CatalogRecommendationsCibCase.UC05_GET_v2_catalog_recommendations_cib_By_id)
            .exec(CatalogRecommendationsCibCase.UC06_PATCH_v2_catalog_recommendations_cib_By_id)
            .exec(CatalogRecommendationsCibCase.UC07_PUT_v2_catalog_recommendations_cib_By_id);
}
