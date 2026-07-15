package scenarios.ZK;

import cases.ZK.CounterpartiesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.counterparties;
import static feeders.ZK.ZKFeeder.counterparties;
import static io.gatling.javaapi.core.CoreDsl.*;

public class CounterpartiesScenario {

    public static ChainBuilder UC01_GET_v2_counterparties =
            group("UC01_GET_v2_counterparties").on(
                    exec(CounterpartiesCase.UC01_GET_v2_counterparties));

    public static ChainBuilder UC02_POST_v2_counterparties =
            group("UC02_POST_v2_counterparties").on(
                    exec(CounterpartiesCase.UC02_POST_v2_counterparties));

    public static ChainBuilder UC03_DELETE_v2_counterparties_batch =
            group("UC03_DELETE_v2_counterparties_batch").on(
                    exec(CounterpartiesCase.UC03_DELETE_v2_counterparties_batch));

    public static ChainBuilder UC04_PATCH_v2_counterparties_batch =
            group("UC04_PATCH_v2_counterparties_batch").on(
                    exec(CounterpartiesCase.UC04_PATCH_v2_counterparties_batch));

    public static ChainBuilder UC05_POST_v2_counterparties_batch =
            group("UC05_POST_v2_counterparties_batch").on(
                    exec(CounterpartiesCase.UC05_POST_v2_counterparties_batch));

    public static ChainBuilder UC06_PUT_v2_counterparties_batch =
            group("UC06_PUT_v2_counterparties_batch").on(
                    exec(CounterpartiesCase.UC06_PUT_v2_counterparties_batch));

    public static ChainBuilder UC07_PATCH_v2_counterparties_batch_identical =
            group("UC07_PATCH_v2_counterparties_batch_identical").on(
                    exec(CounterpartiesCase.UC07_PATCH_v2_counterparties_batch_identical));

    public static ChainBuilder UC08_DELETE_v2_counterparties_By_id =
            group("UC08_DELETE_v2_counterparties_By_id").on(
                    exec(CounterpartiesCase.UC08_DELETE_v2_counterparties_By_id));

    public static ChainBuilder UC09_GET_v2_counterparties_By_id =
            group("UC09_GET_v2_counterparties_By_id").on(
                    exec(CounterpartiesCase.UC09_GET_v2_counterparties_By_id));

    public static ChainBuilder UC10_PATCH_v2_counterparties_By_id =
            group("UC10_PATCH_v2_counterparties_By_id").on(
                    exec(CounterpartiesCase.UC10_PATCH_v2_counterparties_By_id));

    public static ChainBuilder UC11_PUT_v2_counterparties_By_id =
            group("UC11_PUT_v2_counterparties_By_id").on(
                    exec(CounterpartiesCase.UC11_PUT_v2_counterparties_By_id));

    public static ScenarioBuilder scn = scenario("Counterparties")
            .feed(defaultFeeder)
            .feed(counterparties)
            .feed(counterparties)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(9, UC01_GET_v2_counterparties),
                            new Choice.WithWeight(9, UC02_POST_v2_counterparties),
                            new Choice.WithWeight(9, UC03_DELETE_v2_counterparties_batch),
                            new Choice.WithWeight(9, UC04_PATCH_v2_counterparties_batch),
                            new Choice.WithWeight(9, UC05_POST_v2_counterparties_batch),
                            new Choice.WithWeight(9, UC06_PUT_v2_counterparties_batch),
                            new Choice.WithWeight(9, UC07_PATCH_v2_counterparties_batch_identical),
                            new Choice.WithWeight(9, UC08_DELETE_v2_counterparties_By_id),
                            new Choice.WithWeight(9, UC09_GET_v2_counterparties_By_id),
                            new Choice.WithWeight(9, UC10_PATCH_v2_counterparties_By_id),
                            new Choice.WithWeight(9, UC11_PUT_v2_counterparties_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Counterparties")
            .feed(defaultFeeder)
            .feed(counterparties)
            .feed(counterparties)
            .feed(rqUidsFeeder)
            .exec(CounterpartiesCase.UC01_GET_v2_counterparties)
            .exec(CounterpartiesCase.UC02_POST_v2_counterparties)
            .exec(CounterpartiesCase.UC03_DELETE_v2_counterparties_batch)
            .exec(CounterpartiesCase.UC04_PATCH_v2_counterparties_batch)
            .exec(CounterpartiesCase.UC05_POST_v2_counterparties_batch)
            .exec(CounterpartiesCase.UC06_PUT_v2_counterparties_batch)
            .exec(CounterpartiesCase.UC07_PATCH_v2_counterparties_batch_identical)
            .exec(CounterpartiesCase.UC08_DELETE_v2_counterparties_By_id)
            .exec(CounterpartiesCase.UC09_GET_v2_counterparties_By_id)
            .exec(CounterpartiesCase.UC10_PATCH_v2_counterparties_By_id)
            .exec(CounterpartiesCase.UC11_PUT_v2_counterparties_By_id);
}
