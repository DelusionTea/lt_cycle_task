package scenarios.ZK;

import cases.ZK.EventsHistoriesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EventsHistoriesScenario {

    public static ChainBuilder UC01_GET_v2_events_histories =
            group("UC01_GET_v2_events_histories").on(
                    exec(EventsHistoriesCase.UC01_GET_v2_events_histories));

    public static ChainBuilder UC02_POST_v2_events_histories =
            group("UC02_POST_v2_events_histories").on(
                    exec(EventsHistoriesCase.UC02_POST_v2_events_histories));

    public static ChainBuilder UC03_PATCH_v2_events_histories_batch_identical =
            group("UC03_PATCH_v2_events_histories_batch_identical").on(
                    exec(EventsHistoriesCase.UC03_PATCH_v2_events_histories_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_events_histories_By_id =
            group("UC04_DELETE_v2_events_histories_By_id").on(
                    exec(EventsHistoriesCase.UC04_DELETE_v2_events_histories_By_id));

    public static ChainBuilder UC05_GET_v2_events_histories_By_id =
            group("UC05_GET_v2_events_histories_By_id").on(
                    exec(EventsHistoriesCase.UC05_GET_v2_events_histories_By_id));

    public static ChainBuilder UC06_PATCH_v2_events_histories_By_id =
            group("UC06_PATCH_v2_events_histories_By_id").on(
                    exec(EventsHistoriesCase.UC06_PATCH_v2_events_histories_By_id));

    public static ChainBuilder UC07_PUT_v2_events_histories_By_id =
            group("UC07_PUT_v2_events_histories_By_id").on(
                    exec(EventsHistoriesCase.UC07_PUT_v2_events_histories_By_id));

    public static ScenarioBuilder scn = scenario("EventsHistories")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_events_histories),
                            new Choice.WithWeight(14, UC02_POST_v2_events_histories),
                            new Choice.WithWeight(14, UC03_PATCH_v2_events_histories_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_events_histories_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_events_histories_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_events_histories_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_events_histories_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EventsHistories")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(EventsHistoriesCase.UC01_GET_v2_events_histories)
            .exec(EventsHistoriesCase.UC02_POST_v2_events_histories)
            .exec(EventsHistoriesCase.UC03_PATCH_v2_events_histories_batch_identical)
            .exec(EventsHistoriesCase.UC04_DELETE_v2_events_histories_By_id)
            .exec(EventsHistoriesCase.UC05_GET_v2_events_histories_By_id)
            .exec(EventsHistoriesCase.UC06_PATCH_v2_events_histories_By_id)
            .exec(EventsHistoriesCase.UC07_PUT_v2_events_histories_By_id);
}
