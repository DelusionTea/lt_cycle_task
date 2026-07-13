package scenarios.ZK;

import cases.ZK.AccountNumbersCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class AccountNumbersScenario {

    public static ChainBuilder UC01_GET_v2_account_numbers =
            group("UC01_GET_v2_account_numbers").on(
                    exec(AccountNumbersCase.UC01_GET_v2_account_numbers));

    public static ChainBuilder UC02_POST_v2_account_numbers =
            group("UC02_POST_v2_account_numbers").on(
                    exec(AccountNumbersCase.UC02_POST_v2_account_numbers));

    public static ChainBuilder UC03_PATCH_v2_account_numbers_batch_identical =
            group("UC03_PATCH_v2_account_numbers_batch_identical").on(
                    exec(AccountNumbersCase.UC03_PATCH_v2_account_numbers_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_account_numbers_By_id =
            group("UC04_DELETE_v2_account_numbers_By_id").on(
                    exec(AccountNumbersCase.UC04_DELETE_v2_account_numbers_By_id));

    public static ChainBuilder UC05_GET_v2_account_numbers_By_id =
            group("UC05_GET_v2_account_numbers_By_id").on(
                    exec(AccountNumbersCase.UC05_GET_v2_account_numbers_By_id));

    public static ChainBuilder UC06_PATCH_v2_account_numbers_By_id =
            group("UC06_PATCH_v2_account_numbers_By_id").on(
                    exec(AccountNumbersCase.UC06_PATCH_v2_account_numbers_By_id));

    public static ChainBuilder UC07_PUT_v2_account_numbers_By_id =
            group("UC07_PUT_v2_account_numbers_By_id").on(
                    exec(AccountNumbersCase.UC07_PUT_v2_account_numbers_By_id));

    public static ScenarioBuilder scn = scenario("AccountNumbers")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_account_numbers),
                            new Choice.WithWeight(14, UC02_POST_v2_account_numbers),
                            new Choice.WithWeight(14, UC03_PATCH_v2_account_numbers_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_account_numbers_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_account_numbers_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_account_numbers_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_account_numbers_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug AccountNumbers")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(AccountNumbersCase.UC01_GET_v2_account_numbers)
            .exec(AccountNumbersCase.UC02_POST_v2_account_numbers)
            .exec(AccountNumbersCase.UC03_PATCH_v2_account_numbers_batch_identical)
            .exec(AccountNumbersCase.UC04_DELETE_v2_account_numbers_By_id)
            .exec(AccountNumbersCase.UC05_GET_v2_account_numbers_By_id)
            .exec(AccountNumbersCase.UC06_PATCH_v2_account_numbers_By_id)
            .exec(AccountNumbersCase.UC07_PUT_v2_account_numbers_By_id);
}
