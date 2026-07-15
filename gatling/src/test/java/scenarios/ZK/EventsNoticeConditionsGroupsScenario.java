package scenarios.ZK;

import cases.ZK.EventsNoticeConditionsGroupsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.eventsNoticeConditionsGroups;
import static feeders.ZK.ZKFeeder.eventsNoticeConditionsGroups;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EventsNoticeConditionsGroupsScenario {

    public static ChainBuilder UC01_GET_v2_events_notice_conditions_groups =
            group("UC01_GET_v2_events_notice_conditions_groups").on(
                    exec(EventsNoticeConditionsGroupsCase.UC01_GET_v2_events_notice_conditions_groups));

    public static ChainBuilder UC02_POST_v2_events_notice_conditions_groups =
            group("UC02_POST_v2_events_notice_conditions_groups").on(
                    exec(EventsNoticeConditionsGroupsCase.UC02_POST_v2_events_notice_conditions_groups));

    public static ChainBuilder UC03_PATCH_v2_events_notice_conditions_groups_batch_identical =
            group("UC03_PATCH_v2_events_notice_conditions_groups_batch_identical").on(
                    exec(EventsNoticeConditionsGroupsCase.UC03_PATCH_v2_events_notice_conditions_groups_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_events_notice_conditions_groups_By_id =
            group("UC04_DELETE_v2_events_notice_conditions_groups_By_id").on(
                    exec(EventsNoticeConditionsGroupsCase.UC04_DELETE_v2_events_notice_conditions_groups_By_id));

    public static ChainBuilder UC05_GET_v2_events_notice_conditions_groups_By_id =
            group("UC05_GET_v2_events_notice_conditions_groups_By_id").on(
                    exec(EventsNoticeConditionsGroupsCase.UC05_GET_v2_events_notice_conditions_groups_By_id));

    public static ChainBuilder UC06_PATCH_v2_events_notice_conditions_groups_By_id =
            group("UC06_PATCH_v2_events_notice_conditions_groups_By_id").on(
                    exec(EventsNoticeConditionsGroupsCase.UC06_PATCH_v2_events_notice_conditions_groups_By_id));

    public static ChainBuilder UC07_PUT_v2_events_notice_conditions_groups_By_id =
            group("UC07_PUT_v2_events_notice_conditions_groups_By_id").on(
                    exec(EventsNoticeConditionsGroupsCase.UC07_PUT_v2_events_notice_conditions_groups_By_id));

    public static ScenarioBuilder scn = scenario("EventsNoticeConditionsGroups")
            .feed(defaultFeeder)
            .feed(eventsNoticeConditionsGroups)
            .feed(eventsNoticeConditionsGroups)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_events_notice_conditions_groups),
                            new Choice.WithWeight(14, UC02_POST_v2_events_notice_conditions_groups),
                            new Choice.WithWeight(14, UC03_PATCH_v2_events_notice_conditions_groups_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_events_notice_conditions_groups_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_events_notice_conditions_groups_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_events_notice_conditions_groups_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_events_notice_conditions_groups_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EventsNoticeConditionsGroups")
            .feed(defaultFeeder)
            .feed(eventsNoticeConditionsGroups)
            .feed(eventsNoticeConditionsGroups)
            .feed(rqUidsFeeder)
            .exec(EventsNoticeConditionsGroupsCase.UC01_GET_v2_events_notice_conditions_groups)
            .exec(EventsNoticeConditionsGroupsCase.UC02_POST_v2_events_notice_conditions_groups)
            .exec(EventsNoticeConditionsGroupsCase.UC03_PATCH_v2_events_notice_conditions_groups_batch_identical)
            .exec(EventsNoticeConditionsGroupsCase.UC04_DELETE_v2_events_notice_conditions_groups_By_id)
            .exec(EventsNoticeConditionsGroupsCase.UC05_GET_v2_events_notice_conditions_groups_By_id)
            .exec(EventsNoticeConditionsGroupsCase.UC06_PATCH_v2_events_notice_conditions_groups_By_id)
            .exec(EventsNoticeConditionsGroupsCase.UC07_PUT_v2_events_notice_conditions_groups_By_id);
}
