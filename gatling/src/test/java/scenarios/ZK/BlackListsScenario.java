package scenarios.ZK;

import cases.ZK.BlackListsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class BlackListsScenario {

    public static ChainBuilder UC01_GET_v2_black_lists =
            group("UC01_GET_v2_black_lists").on(
                    exec(BlackListsCase.UC01_GET_v2_black_lists));

    public static ChainBuilder UC02_POST_v2_black_lists =
            group("UC02_POST_v2_black_lists").on(
                    exec(BlackListsCase.UC02_POST_v2_black_lists));

    public static ChainBuilder UC03_PATCH_v2_black_lists_batch_identical =
            group("UC03_PATCH_v2_black_lists_batch_identical").on(
                    exec(BlackListsCase.UC03_PATCH_v2_black_lists_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_black_lists_By_id =
            group("UC04_DELETE_v2_black_lists_By_id").on(
                    exec(BlackListsCase.UC04_DELETE_v2_black_lists_By_id));

    public static ChainBuilder UC05_GET_v2_black_lists_By_id =
            group("UC05_GET_v2_black_lists_By_id").on(
                    exec(BlackListsCase.UC05_GET_v2_black_lists_By_id));

    public static ChainBuilder UC06_PATCH_v2_black_lists_By_id =
            group("UC06_PATCH_v2_black_lists_By_id").on(
                    exec(BlackListsCase.UC06_PATCH_v2_black_lists_By_id));

    public static ChainBuilder UC07_PUT_v2_black_lists_By_id =
            group("UC07_PUT_v2_black_lists_By_id").on(
                    exec(BlackListsCase.UC07_PUT_v2_black_lists_By_id));

    public static ScenarioBuilder scn = scenario("BlackLists")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_black_lists),
                            new Choice.WithWeight(14, UC02_POST_v2_black_lists),
                            new Choice.WithWeight(14, UC03_PATCH_v2_black_lists_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_black_lists_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_black_lists_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_black_lists_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_black_lists_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug BlackLists")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(BlackListsCase.UC01_GET_v2_black_lists)
            .exec(BlackListsCase.UC02_POST_v2_black_lists)
            .exec(BlackListsCase.UC03_PATCH_v2_black_lists_batch_identical)
            .exec(BlackListsCase.UC04_DELETE_v2_black_lists_By_id)
            .exec(BlackListsCase.UC05_GET_v2_black_lists_By_id)
            .exec(BlackListsCase.UC06_PATCH_v2_black_lists_By_id)
            .exec(BlackListsCase.UC07_PUT_v2_black_lists_By_id);
}
