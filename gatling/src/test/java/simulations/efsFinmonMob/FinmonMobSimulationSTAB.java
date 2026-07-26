package simulations.efsFinmonMob;

import io.gatling.javaapi.core.Simulation;
import scenarios.efsFinmonMob.FinmonMobScenario;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.holdFor;
import static misc.HttpProtocolsEFS.AuthHttpProtocol_EFSMobile;

public class FinmonMobSimulationSTAB extends Simulation {

    private static final int BASE_RPS = 48; // ваш 100% TPS
    {


        setUp(
                FinmonMobScenario.scn
                        .injectClosed(
                                rampConcurrentUsers(0).to(46).during(Duration.ofSeconds(30)),
                                constantConcurrentUsers(46).during(Duration.ofHours(72))
                        )
                        .throttle(
                                reachRps(76).in(Duration.ofSeconds(30)), holdFor(Duration.ofHours(72)) // 300%
                        )
        ).maxDuration(Duration.ofHours(72)).protocols(AuthHttpProtocol_EFSMobile);
    }
}