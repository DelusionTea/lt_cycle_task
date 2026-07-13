package scenarios.ZK;

import cases.ZK.ComplianceRequestsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceRequestsScenario {

    public static ChainBuilder UC01_GET_v3_compliance_requests =
            group("UC01_GET_v3_compliance_requests").on(
                    exec(ComplianceRequestsCase.UC01_GET_v3_compliance_requests));

    public static ChainBuilder UC02_GET_v3_compliance_requests_counters =
            group("UC02_GET_v3_compliance_requests_counters").on(
                    exec(ComplianceRequestsCase.UC02_GET_v3_compliance_requests_counters));

    public static ChainBuilder UC03_PUT_v3_compliance_requests_By_id_assign =
            group("UC03_PUT_v3_compliance_requests_By_id_assign").on(
                    exec(ComplianceRequestsCase.UC03_PUT_v3_compliance_requests_By_id_assign));

    public static ChainBuilder UC04_PUT_v3_compliance_requests_By_id_close =
            group("UC04_PUT_v3_compliance_requests_By_id_close").on(
                    exec(ComplianceRequestsCase.UC04_PUT_v3_compliance_requests_By_id_close));

    public static ChainBuilder UC05_GET_v3_compliance_requests_By_id_detail =
            group("UC05_GET_v3_compliance_requests_By_id_detail").on(
                    exec(ComplianceRequestsCase.UC05_GET_v3_compliance_requests_By_id_detail));

    public static ChainBuilder UC06_PUT_v3_compliance_requests_By_id_from_to_bordo =
            group("UC06_PUT_v3_compliance_requests_By_id_from_to_bordo").on(
                    exec(ComplianceRequestsCase.UC06_PUT_v3_compliance_requests_By_id_from_to_bordo));

    public static ChainBuilder UC07_GET_v3_compliance_requests_By_id_preview =
            group("UC07_GET_v3_compliance_requests_By_id_preview").on(
                    exec(ComplianceRequestsCase.UC07_GET_v3_compliance_requests_By_id_preview));

    public static ScenarioBuilder scn = scenario("ComplianceRequests")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v3_compliance_requests),
                            new Choice.WithWeight(14, UC02_GET_v3_compliance_requests_counters),
                            new Choice.WithWeight(14, UC03_PUT_v3_compliance_requests_By_id_assign),
                            new Choice.WithWeight(14, UC04_PUT_v3_compliance_requests_By_id_close),
                            new Choice.WithWeight(14, UC05_GET_v3_compliance_requests_By_id_detail),
                            new Choice.WithWeight(14, UC06_PUT_v3_compliance_requests_By_id_from_to_bordo),
                            new Choice.WithWeight(14, UC07_GET_v3_compliance_requests_By_id_preview)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceRequests")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceRequestsCase.UC01_GET_v3_compliance_requests)
            .exec(ComplianceRequestsCase.UC02_GET_v3_compliance_requests_counters)
            .exec(ComplianceRequestsCase.UC03_PUT_v3_compliance_requests_By_id_assign)
            .exec(ComplianceRequestsCase.UC04_PUT_v3_compliance_requests_By_id_close)
            .exec(ComplianceRequestsCase.UC05_GET_v3_compliance_requests_By_id_detail)
            .exec(ComplianceRequestsCase.UC06_PUT_v3_compliance_requests_By_id_from_to_bordo)
            .exec(ComplianceRequestsCase.UC07_GET_v3_compliance_requests_By_id_preview);
}
