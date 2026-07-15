package scenarios.ZK;

import cases.ZK.GetCalendarCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.tasks;
import static feeders.ZK.ZKFeeder.tasks;
import static io.gatling.javaapi.core.CoreDsl.*;

public class GetCalendarScenario {

    public static ChainBuilder UC01_GET_v2_get_calendar =
            group("UC01_GET_v2_get_calendar").on(
                    exec(GetCalendarCase.UC01_GET_v2_get_calendar));

    public static ScenarioBuilder scn = scenario("GetCalendar")
            .feed(defaultFeeder)
            .feed(tasks)
            .feed(tasks)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v2_get_calendar)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug GetCalendar")
            .feed(defaultFeeder)
            .feed(tasks)
            .feed(tasks)
            .feed(rqUidsFeeder)
            .exec(GetCalendarCase.UC01_GET_v2_get_calendar);
}
