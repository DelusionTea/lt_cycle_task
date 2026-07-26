package simulations.efsFinmonMob;

import io.gatling.javaapi.core.Simulation;
import scenarios.efsFinmonMob.FinmonMobScenario;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static misc.HttpProtocolsEFS.AuthHttpProtocol_EFSMobile;

public class FinmonMobSimulationDebug extends Simulation {
    {
        setUp(
                FinmonMobScenario.debug
                        .injectOpen(
                                atOnceUsers(5)
                        )
        ).maxDuration(Duration.ofMinutes(1)).protocols(AuthHttpProtocol_EFSMobile);
    }
}
