package scenarios.ZK;

import cases.ZK.CkrCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.ckr;
import static feeders.ZK.ZKFeeder.ckr;
import static io.gatling.javaapi.core.CoreDsl.*;

public class CkrScenario {

    public static ChainBuilder UC01_POST_v2_ckr_get_client_info =
            group("UC01_POST_v2_ckr_get_client_info").on(
                    exec(CkrCase.UC01_POST_v2_ckr_get_client_info));

    public static ChainBuilder UC02_POST_v2_ckr_get_request_info =
            group("UC02_POST_v2_ckr_get_request_info").on(
                    exec(CkrCase.UC02_POST_v2_ckr_get_request_info));

    public static ScenarioBuilder scn = scenario("Ckr")
            .feed(defaultFeeder)
            .feed(ckr)
            .feed(ckr)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(50, UC01_POST_v2_ckr_get_client_info),
                            new Choice.WithWeight(50, UC02_POST_v2_ckr_get_request_info)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Ckr")
            .feed(defaultFeeder)
            .feed(ckr)
            .feed(ckr)
            .feed(rqUidsFeeder)
            .exec(CkrCase.UC01_POST_v2_ckr_get_client_info)
            .exec(CkrCase.UC02_POST_v2_ckr_get_request_info);
}
