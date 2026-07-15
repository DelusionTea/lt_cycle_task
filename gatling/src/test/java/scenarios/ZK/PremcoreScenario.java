package scenarios.ZK;

import cases.ZK.PremcoreCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;


import static io.gatling.javaapi.core.CoreDsl.*;

public class PremcoreScenario {

    public static ChainBuilder UC01_GET_v2_premcore_get_request_info =
            group("UC01_GET_v2_premcore_get_request_info").on(
                    exec(PremcoreCase.UC01_GET_v2_premcore_get_request_info));

    public static ScenarioBuilder scn = scenario("Premcore")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v2_premcore_get_request_info)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Premcore")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(PremcoreCase.UC01_GET_v2_premcore_get_request_info);
}
