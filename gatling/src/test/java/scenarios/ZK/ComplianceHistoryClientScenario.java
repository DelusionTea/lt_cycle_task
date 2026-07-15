package scenarios.ZK;

import cases.ZK.ComplianceHistoryClientCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceHistoryClient;
import static feeders.ZK.ZKFeeder.complianceHistoryClient;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceHistoryClientScenario {

    public static ChainBuilder UC01_GET_v2_compliance_history_client_By_ucpId =
            group("UC01_GET_v2_compliance_history_client_By_ucpId").on(
                    exec(ComplianceHistoryClientCase.UC01_GET_v2_compliance_history_client_By_ucpId));

    public static ScenarioBuilder scn = scenario("ComplianceHistoryClient")
            .feed(defaultFeeder)
            .feed(complianceHistoryClient)
            .feed(complianceHistoryClient)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v2_compliance_history_client_By_ucpId)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceHistoryClient")
            .feed(defaultFeeder)
            .feed(complianceHistoryClient)
            .feed(complianceHistoryClient)
            .feed(rqUidsFeeder)
            .exec(ComplianceHistoryClientCase.UC01_GET_v2_compliance_history_client_By_ucpId);
}
