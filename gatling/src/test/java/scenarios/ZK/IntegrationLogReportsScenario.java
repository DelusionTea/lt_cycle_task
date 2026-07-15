package scenarios.ZK;

import cases.ZK.IntegrationLogReportsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.integrationLogReports;
import static feeders.ZK.ZKFeeder.integrationLogReports;
import static io.gatling.javaapi.core.CoreDsl.*;

public class IntegrationLogReportsScenario {

    public static ChainBuilder UC01_GET_v2_integration_log_reports =
            group("UC01_GET_v2_integration_log_reports").on(
                    exec(IntegrationLogReportsCase.UC01_GET_v2_integration_log_reports));

    public static ChainBuilder UC02_POST_v2_integration_log_reports =
            group("UC02_POST_v2_integration_log_reports").on(
                    exec(IntegrationLogReportsCase.UC02_POST_v2_integration_log_reports));

    public static ChainBuilder UC03_PATCH_v2_integration_log_reports_batch_identical =
            group("UC03_PATCH_v2_integration_log_reports_batch_identical").on(
                    exec(IntegrationLogReportsCase.UC03_PATCH_v2_integration_log_reports_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_integration_log_reports_By_id =
            group("UC04_DELETE_v2_integration_log_reports_By_id").on(
                    exec(IntegrationLogReportsCase.UC04_DELETE_v2_integration_log_reports_By_id));

    public static ChainBuilder UC05_GET_v2_integration_log_reports_By_id =
            group("UC05_GET_v2_integration_log_reports_By_id").on(
                    exec(IntegrationLogReportsCase.UC05_GET_v2_integration_log_reports_By_id));

    public static ChainBuilder UC06_PATCH_v2_integration_log_reports_By_id =
            group("UC06_PATCH_v2_integration_log_reports_By_id").on(
                    exec(IntegrationLogReportsCase.UC06_PATCH_v2_integration_log_reports_By_id));

    public static ChainBuilder UC07_PUT_v2_integration_log_reports_By_id =
            group("UC07_PUT_v2_integration_log_reports_By_id").on(
                    exec(IntegrationLogReportsCase.UC07_PUT_v2_integration_log_reports_By_id));

    public static ScenarioBuilder scn = scenario("IntegrationLogReports")
            .feed(defaultFeeder)
            .feed(integrationLogReports)
            .feed(integrationLogReports)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_integration_log_reports),
                            new Choice.WithWeight(14, UC02_POST_v2_integration_log_reports),
                            new Choice.WithWeight(14, UC03_PATCH_v2_integration_log_reports_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_integration_log_reports_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_integration_log_reports_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_integration_log_reports_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_integration_log_reports_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug IntegrationLogReports")
            .feed(defaultFeeder)
            .feed(integrationLogReports)
            .feed(integrationLogReports)
            .feed(rqUidsFeeder)
            .exec(IntegrationLogReportsCase.UC01_GET_v2_integration_log_reports)
            .exec(IntegrationLogReportsCase.UC02_POST_v2_integration_log_reports)
            .exec(IntegrationLogReportsCase.UC03_PATCH_v2_integration_log_reports_batch_identical)
            .exec(IntegrationLogReportsCase.UC04_DELETE_v2_integration_log_reports_By_id)
            .exec(IntegrationLogReportsCase.UC05_GET_v2_integration_log_reports_By_id)
            .exec(IntegrationLogReportsCase.UC06_PATCH_v2_integration_log_reports_By_id)
            .exec(IntegrationLogReportsCase.UC07_PUT_v2_integration_log_reports_By_id);
}
