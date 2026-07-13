package scenarios.ZK;

import cases.ZK.IvrCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class IvrScenario {

    public static ChainBuilder UC01_POST_v2_ivr_get_request_info =
            group("UC01_POST_v2_ivr_get_request_info").on(
                    exec(IvrCase.UC01_POST_v2_ivr_get_request_info));

    public static ScenarioBuilder scn = scenario("Ivr")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_POST_v2_ivr_get_request_info)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Ivr")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(IvrCase.UC01_POST_v2_ivr_get_request_info);
}
