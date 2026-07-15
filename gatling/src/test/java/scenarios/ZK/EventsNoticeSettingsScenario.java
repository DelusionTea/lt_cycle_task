package scenarios.ZK;

import cases.ZK.EventsNoticeSettingsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.eventsNoticeSettings;
import static feeders.ZK.ZKFeeder.eventsNoticeSettings;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EventsNoticeSettingsScenario {

    public static ChainBuilder UC01_GET_v2_events_notice_settings =
            group("UC01_GET_v2_events_notice_settings").on(
                    exec(EventsNoticeSettingsCase.UC01_GET_v2_events_notice_settings));

    public static ChainBuilder UC02_POST_v2_events_notice_settings =
            group("UC02_POST_v2_events_notice_settings").on(
                    exec(EventsNoticeSettingsCase.UC02_POST_v2_events_notice_settings));

    public static ChainBuilder UC03_PATCH_v2_events_notice_settings_batch_identical =
            group("UC03_PATCH_v2_events_notice_settings_batch_identical").on(
                    exec(EventsNoticeSettingsCase.UC03_PATCH_v2_events_notice_settings_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_events_notice_settings_By_id =
            group("UC04_DELETE_v2_events_notice_settings_By_id").on(
                    exec(EventsNoticeSettingsCase.UC04_DELETE_v2_events_notice_settings_By_id));

    public static ChainBuilder UC05_GET_v2_events_notice_settings_By_id =
            group("UC05_GET_v2_events_notice_settings_By_id").on(
                    exec(EventsNoticeSettingsCase.UC05_GET_v2_events_notice_settings_By_id));

    public static ChainBuilder UC06_PATCH_v2_events_notice_settings_By_id =
            group("UC06_PATCH_v2_events_notice_settings_By_id").on(
                    exec(EventsNoticeSettingsCase.UC06_PATCH_v2_events_notice_settings_By_id));

    public static ChainBuilder UC07_PUT_v2_events_notice_settings_By_id =
            group("UC07_PUT_v2_events_notice_settings_By_id").on(
                    exec(EventsNoticeSettingsCase.UC07_PUT_v2_events_notice_settings_By_id));

    public static ScenarioBuilder scn = scenario("EventsNoticeSettings")
            .feed(defaultFeeder)
            .feed(eventsNoticeSettings)
            .feed(eventsNoticeSettings)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_events_notice_settings),
                            new Choice.WithWeight(14, UC02_POST_v2_events_notice_settings),
                            new Choice.WithWeight(14, UC03_PATCH_v2_events_notice_settings_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_events_notice_settings_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_events_notice_settings_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_events_notice_settings_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_events_notice_settings_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EventsNoticeSettings")
            .feed(defaultFeeder)
            .feed(eventsNoticeSettings)
            .feed(eventsNoticeSettings)
            .feed(rqUidsFeeder)
            .exec(EventsNoticeSettingsCase.UC01_GET_v2_events_notice_settings)
            .exec(EventsNoticeSettingsCase.UC02_POST_v2_events_notice_settings)
            .exec(EventsNoticeSettingsCase.UC03_PATCH_v2_events_notice_settings_batch_identical)
            .exec(EventsNoticeSettingsCase.UC04_DELETE_v2_events_notice_settings_By_id)
            .exec(EventsNoticeSettingsCase.UC05_GET_v2_events_notice_settings_By_id)
            .exec(EventsNoticeSettingsCase.UC06_PATCH_v2_events_notice_settings_By_id)
            .exec(EventsNoticeSettingsCase.UC07_PUT_v2_events_notice_settings_By_id);
}
