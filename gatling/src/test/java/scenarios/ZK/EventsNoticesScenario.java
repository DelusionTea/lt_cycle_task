package scenarios.ZK;

import cases.ZK.EventsNoticesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EventsNoticesScenario {

    public static ChainBuilder UC01_GET_v2_events_notices =
            group("UC01_GET_v2_events_notices").on(
                    exec(EventsNoticesCase.UC01_GET_v2_events_notices));

    public static ChainBuilder UC02_POST_v2_events_notices =
            group("UC02_POST_v2_events_notices").on(
                    exec(EventsNoticesCase.UC02_POST_v2_events_notices));

    public static ChainBuilder UC03_PATCH_v2_events_notices_batch_identical =
            group("UC03_PATCH_v2_events_notices_batch_identical").on(
                    exec(EventsNoticesCase.UC03_PATCH_v2_events_notices_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_events_notices_By_id =
            group("UC04_DELETE_v2_events_notices_By_id").on(
                    exec(EventsNoticesCase.UC04_DELETE_v2_events_notices_By_id));

    public static ChainBuilder UC05_GET_v2_events_notices_By_id =
            group("UC05_GET_v2_events_notices_By_id").on(
                    exec(EventsNoticesCase.UC05_GET_v2_events_notices_By_id));

    public static ChainBuilder UC06_PATCH_v2_events_notices_By_id =
            group("UC06_PATCH_v2_events_notices_By_id").on(
                    exec(EventsNoticesCase.UC06_PATCH_v2_events_notices_By_id));

    public static ChainBuilder UC07_PUT_v2_events_notices_By_id =
            group("UC07_PUT_v2_events_notices_By_id").on(
                    exec(EventsNoticesCase.UC07_PUT_v2_events_notices_By_id));

    public static ScenarioBuilder scn = scenario("EventsNotices")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_events_notices),
                            new Choice.WithWeight(14, UC02_POST_v2_events_notices),
                            new Choice.WithWeight(14, UC03_PATCH_v2_events_notices_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_events_notices_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_events_notices_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_events_notices_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_events_notices_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EventsNotices")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(EventsNoticesCase.UC01_GET_v2_events_notices)
            .exec(EventsNoticesCase.UC02_POST_v2_events_notices)
            .exec(EventsNoticesCase.UC03_PATCH_v2_events_notices_batch_identical)
            .exec(EventsNoticesCase.UC04_DELETE_v2_events_notices_By_id)
            .exec(EventsNoticesCase.UC05_GET_v2_events_notices_By_id)
            .exec(EventsNoticesCase.UC06_PATCH_v2_events_notices_By_id)
            .exec(EventsNoticesCase.UC07_PUT_v2_events_notices_By_id);
}
