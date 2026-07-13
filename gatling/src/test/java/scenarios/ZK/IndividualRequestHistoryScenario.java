package scenarios.ZK;

import cases.ZK.IndividualRequestHistoryCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class IndividualRequestHistoryScenario {

    public static ChainBuilder UC01_GET_v2_individual_request_history =
            group("UC01_GET_v2_individual_request_history").on(
                    exec(IndividualRequestHistoryCase.UC01_GET_v2_individual_request_history));

    public static ChainBuilder UC02_POST_v2_individual_request_history =
            group("UC02_POST_v2_individual_request_history").on(
                    exec(IndividualRequestHistoryCase.UC02_POST_v2_individual_request_history));

    public static ChainBuilder UC03_PATCH_v2_individual_request_history_batch_identical =
            group("UC03_PATCH_v2_individual_request_history_batch_identical").on(
                    exec(IndividualRequestHistoryCase.UC03_PATCH_v2_individual_request_history_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_individual_request_history_By_id =
            group("UC04_DELETE_v2_individual_request_history_By_id").on(
                    exec(IndividualRequestHistoryCase.UC04_DELETE_v2_individual_request_history_By_id));

    public static ChainBuilder UC05_GET_v2_individual_request_history_By_id =
            group("UC05_GET_v2_individual_request_history_By_id").on(
                    exec(IndividualRequestHistoryCase.UC05_GET_v2_individual_request_history_By_id));

    public static ChainBuilder UC06_PATCH_v2_individual_request_history_By_id =
            group("UC06_PATCH_v2_individual_request_history_By_id").on(
                    exec(IndividualRequestHistoryCase.UC06_PATCH_v2_individual_request_history_By_id));

    public static ChainBuilder UC07_PUT_v2_individual_request_history_By_id =
            group("UC07_PUT_v2_individual_request_history_By_id").on(
                    exec(IndividualRequestHistoryCase.UC07_PUT_v2_individual_request_history_By_id));

    public static ScenarioBuilder scn = scenario("IndividualRequestHistory")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_individual_request_history),
                            new Choice.WithWeight(14, UC02_POST_v2_individual_request_history),
                            new Choice.WithWeight(14, UC03_PATCH_v2_individual_request_history_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_individual_request_history_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_individual_request_history_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_individual_request_history_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_individual_request_history_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug IndividualRequestHistory")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(IndividualRequestHistoryCase.UC01_GET_v2_individual_request_history)
            .exec(IndividualRequestHistoryCase.UC02_POST_v2_individual_request_history)
            .exec(IndividualRequestHistoryCase.UC03_PATCH_v2_individual_request_history_batch_identical)
            .exec(IndividualRequestHistoryCase.UC04_DELETE_v2_individual_request_history_By_id)
            .exec(IndividualRequestHistoryCase.UC05_GET_v2_individual_request_history_By_id)
            .exec(IndividualRequestHistoryCase.UC06_PATCH_v2_individual_request_history_By_id)
            .exec(IndividualRequestHistoryCase.UC07_PUT_v2_individual_request_history_By_id);
}
