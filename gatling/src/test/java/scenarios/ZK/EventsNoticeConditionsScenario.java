package scenarios.ZK;

import cases.ZK.EventsNoticeConditionsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EventsNoticeConditionsScenario {

    public static ChainBuilder UC01_GET_v2_events_notice_conditions =
            group("UC01_GET_v2_events_notice_conditions").on(
                    exec(EventsNoticeConditionsCase.UC01_GET_v2_events_notice_conditions));

    public static ChainBuilder UC02_POST_v2_events_notice_conditions =
            group("UC02_POST_v2_events_notice_conditions").on(
                    exec(EventsNoticeConditionsCase.UC02_POST_v2_events_notice_conditions));

    public static ChainBuilder UC03_PATCH_v2_events_notice_conditions_batch_identical =
            group("UC03_PATCH_v2_events_notice_conditions_batch_identical").on(
                    exec(EventsNoticeConditionsCase.UC03_PATCH_v2_events_notice_conditions_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_events_notice_conditions_By_id =
            group("UC04_DELETE_v2_events_notice_conditions_By_id").on(
                    exec(EventsNoticeConditionsCase.UC04_DELETE_v2_events_notice_conditions_By_id));

    public static ChainBuilder UC05_GET_v2_events_notice_conditions_By_id =
            group("UC05_GET_v2_events_notice_conditions_By_id").on(
                    exec(EventsNoticeConditionsCase.UC05_GET_v2_events_notice_conditions_By_id));

    public static ChainBuilder UC06_PATCH_v2_events_notice_conditions_By_id =
            group("UC06_PATCH_v2_events_notice_conditions_By_id").on(
                    exec(EventsNoticeConditionsCase.UC06_PATCH_v2_events_notice_conditions_By_id));

    public static ChainBuilder UC07_PUT_v2_events_notice_conditions_By_id =
            group("UC07_PUT_v2_events_notice_conditions_By_id").on(
                    exec(EventsNoticeConditionsCase.UC07_PUT_v2_events_notice_conditions_By_id));

    public static ScenarioBuilder scn = scenario("EventsNoticeConditions")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_events_notice_conditions),
                            new Choice.WithWeight(14, UC02_POST_v2_events_notice_conditions),
                            new Choice.WithWeight(14, UC03_PATCH_v2_events_notice_conditions_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_events_notice_conditions_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_events_notice_conditions_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_events_notice_conditions_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_events_notice_conditions_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EventsNoticeConditions")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(EventsNoticeConditionsCase.UC01_GET_v2_events_notice_conditions)
            .exec(EventsNoticeConditionsCase.UC02_POST_v2_events_notice_conditions)
            .exec(EventsNoticeConditionsCase.UC03_PATCH_v2_events_notice_conditions_batch_identical)
            .exec(EventsNoticeConditionsCase.UC04_DELETE_v2_events_notice_conditions_By_id)
            .exec(EventsNoticeConditionsCase.UC05_GET_v2_events_notice_conditions_By_id)
            .exec(EventsNoticeConditionsCase.UC06_PATCH_v2_events_notice_conditions_By_id)
            .exec(EventsNoticeConditionsCase.UC07_PUT_v2_events_notice_conditions_By_id);
}
