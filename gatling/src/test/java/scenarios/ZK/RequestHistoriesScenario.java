package scenarios.ZK;

import cases.ZK.RequestHistoriesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceRequests;
import static feeders.ZK.ZKFeeder.complianceRequests;
import static io.gatling.javaapi.core.CoreDsl.*;

public class RequestHistoriesScenario {

    public static ChainBuilder UC01_GET_v2_request_histories =
            group("UC01_GET_v2_request_histories").on(
                    exec(RequestHistoriesCase.UC01_GET_v2_request_histories));

    public static ChainBuilder UC02_POST_v2_request_histories =
            group("UC02_POST_v2_request_histories").on(
                    exec(RequestHistoriesCase.UC02_POST_v2_request_histories));

    public static ChainBuilder UC03_PATCH_v2_request_histories_batch_identical =
            group("UC03_PATCH_v2_request_histories_batch_identical").on(
                    exec(RequestHistoriesCase.UC03_PATCH_v2_request_histories_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_request_histories_By_id =
            group("UC04_DELETE_v2_request_histories_By_id").on(
                    exec(RequestHistoriesCase.UC04_DELETE_v2_request_histories_By_id));

    public static ChainBuilder UC05_GET_v2_request_histories_By_id =
            group("UC05_GET_v2_request_histories_By_id").on(
                    exec(RequestHistoriesCase.UC05_GET_v2_request_histories_By_id));

    public static ChainBuilder UC06_PATCH_v2_request_histories_By_id =
            group("UC06_PATCH_v2_request_histories_By_id").on(
                    exec(RequestHistoriesCase.UC06_PATCH_v2_request_histories_By_id));

    public static ChainBuilder UC07_PUT_v2_request_histories_By_id =
            group("UC07_PUT_v2_request_histories_By_id").on(
                    exec(RequestHistoriesCase.UC07_PUT_v2_request_histories_By_id));

    public static ScenarioBuilder scn = scenario("RequestHistories")
            .feed(defaultFeeder)
            .feed(complianceRequests)
            .feed(complianceRequests)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_request_histories),
                            new Choice.WithWeight(14, UC02_POST_v2_request_histories),
                            new Choice.WithWeight(14, UC03_PATCH_v2_request_histories_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_request_histories_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_request_histories_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_request_histories_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_request_histories_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug RequestHistories")
            .feed(defaultFeeder)
            .feed(complianceRequests)
            .feed(complianceRequests)
            .feed(rqUidsFeeder)
            .exec(RequestHistoriesCase.UC01_GET_v2_request_histories)
            .exec(RequestHistoriesCase.UC02_POST_v2_request_histories)
            .exec(RequestHistoriesCase.UC03_PATCH_v2_request_histories_batch_identical)
            .exec(RequestHistoriesCase.UC04_DELETE_v2_request_histories_By_id)
            .exec(RequestHistoriesCase.UC05_GET_v2_request_histories_By_id)
            .exec(RequestHistoriesCase.UC06_PATCH_v2_request_histories_By_id)
            .exec(RequestHistoriesCase.UC07_PUT_v2_request_histories_By_id);
}
