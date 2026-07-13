package scenarios.ZK;

import cases.ZK.FreeFormatLettersCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class FreeFormatLettersScenario {

    public static ChainBuilder UC01_GET_v2_free_format_letters =
            group("UC01_GET_v2_free_format_letters").on(
                    exec(FreeFormatLettersCase.UC01_GET_v2_free_format_letters));

    public static ChainBuilder UC02_POST_v2_free_format_letters =
            group("UC02_POST_v2_free_format_letters").on(
                    exec(FreeFormatLettersCase.UC02_POST_v2_free_format_letters));

    public static ChainBuilder UC03_GET_v2_free_format_letters_all =
            group("UC03_GET_v2_free_format_letters_all").on(
                    exec(FreeFormatLettersCase.UC03_GET_v2_free_format_letters_all));

    public static ChainBuilder UC04_PATCH_v2_free_format_letters_batch_identical =
            group("UC04_PATCH_v2_free_format_letters_batch_identical").on(
                    exec(FreeFormatLettersCase.UC04_PATCH_v2_free_format_letters_batch_identical));

    public static ChainBuilder UC05_DELETE_v2_free_format_letters_By_id =
            group("UC05_DELETE_v2_free_format_letters_By_id").on(
                    exec(FreeFormatLettersCase.UC05_DELETE_v2_free_format_letters_By_id));

    public static ChainBuilder UC06_GET_v2_free_format_letters_By_id =
            group("UC06_GET_v2_free_format_letters_By_id").on(
                    exec(FreeFormatLettersCase.UC06_GET_v2_free_format_letters_By_id));

    public static ChainBuilder UC07_PATCH_v2_free_format_letters_By_id =
            group("UC07_PATCH_v2_free_format_letters_By_id").on(
                    exec(FreeFormatLettersCase.UC07_PATCH_v2_free_format_letters_By_id));

    public static ChainBuilder UC08_PUT_v2_free_format_letters_By_id =
            group("UC08_PUT_v2_free_format_letters_By_id").on(
                    exec(FreeFormatLettersCase.UC08_PUT_v2_free_format_letters_By_id));

    public static ScenarioBuilder scn = scenario("FreeFormatLetters")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(12, UC01_GET_v2_free_format_letters),
                            new Choice.WithWeight(12, UC02_POST_v2_free_format_letters),
                            new Choice.WithWeight(12, UC03_GET_v2_free_format_letters_all),
                            new Choice.WithWeight(12, UC04_PATCH_v2_free_format_letters_batch_identical),
                            new Choice.WithWeight(12, UC05_DELETE_v2_free_format_letters_By_id),
                            new Choice.WithWeight(12, UC06_GET_v2_free_format_letters_By_id),
                            new Choice.WithWeight(12, UC07_PATCH_v2_free_format_letters_By_id),
                            new Choice.WithWeight(12, UC08_PUT_v2_free_format_letters_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug FreeFormatLetters")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(FreeFormatLettersCase.UC01_GET_v2_free_format_letters)
            .exec(FreeFormatLettersCase.UC02_POST_v2_free_format_letters)
            .exec(FreeFormatLettersCase.UC03_GET_v2_free_format_letters_all)
            .exec(FreeFormatLettersCase.UC04_PATCH_v2_free_format_letters_batch_identical)
            .exec(FreeFormatLettersCase.UC05_DELETE_v2_free_format_letters_By_id)
            .exec(FreeFormatLettersCase.UC06_GET_v2_free_format_letters_By_id)
            .exec(FreeFormatLettersCase.UC07_PATCH_v2_free_format_letters_By_id)
            .exec(FreeFormatLettersCase.UC08_PUT_v2_free_format_letters_By_id);
}
