package simulations.efsFinmonMob;

import io.gatling.javaapi.core.Simulation;
import scenarios.efsFinmonMob.FinmonMobScenario;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static misc.HttpProtocolsEFS.AuthHttpProtocol_EFSMobile;

public class FinmonMobSimulationLMAX extends Simulation {
    private static final int BASE_USERS = 24; // или 40, как решите
    private static final int BASE_RPS = 48; // ваш 100% TPS
    private static final int LEVELS = 11; // 100..500%
    private static final int RAMP_UP_TIME_MINUTES= 1;
    private static final int HOLD_FOR_TIME_MINUTES= 21;
    private static final double Multiplier = 1.5;

    {
        setUp(
                FinmonMobScenario.scn.injectClosed(
                        incrementConcurrentUsers(BASE_USERS)
                                .times(LEVELS)
                                .eachLevelLasting(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES))
                                .separatedByRampsLasting(Duration.ofMinutes(RAMP_UP_TIME_MINUTES))
                                .startingFrom(BASE_USERS) // первая полка = 100%
                )
        ).throttle(
                        reachRps(BASE_RPS).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 100%
                        reachRps(58).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 200%
                        reachRps(68).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 300%
                        reachRps(77).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 400%
                        reachRps(88).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 500%
                        reachRps(96).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 600%
                        reachRps(106).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 700%
                        reachRps(116).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 800%
                        reachRps(125).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)), // 900%
                        reachRps(135).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)),
                        reachRps(144).in(Duration.ofMinutes(RAMP_UP_TIME_MINUTES)), holdFor(Duration.ofMinutes(HOLD_FOR_TIME_MINUTES)) // 1000%
// 1000%


                ).maxDuration(Duration.ofMinutes(LEVELS * 21))
                .protocols(AuthHttpProtocol_EFSMobile)
                ;
    }

}
