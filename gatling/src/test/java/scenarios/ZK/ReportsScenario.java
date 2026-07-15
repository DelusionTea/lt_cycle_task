package scenarios.ZK;

import cases.ZK.ReportsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.reports;
import static feeders.ZK.ZKFeeder.reports;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ReportsScenario {

    public static ChainBuilder UC01_GET_v1_reports =
            group("UC01_GET_v1_reports").on(
                    exec(ReportsCase.UC01_GET_v1_reports));

    public static ChainBuilder UC02_POST_v1_reports =
            group("UC02_POST_v1_reports").on(
                    exec(ReportsCase.UC02_POST_v1_reports));

    public static ChainBuilder UC03_PATCH_v1_reports_batch_identical =
            group("UC03_PATCH_v1_reports_batch_identical").on(
                    exec(ReportsCase.UC03_PATCH_v1_reports_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_reports_By_id =
            group("UC04_DELETE_v1_reports_By_id").on(
                    exec(ReportsCase.UC04_DELETE_v1_reports_By_id));

    public static ChainBuilder UC05_GET_v1_reports_By_id =
            group("UC05_GET_v1_reports_By_id").on(
                    exec(ReportsCase.UC05_GET_v1_reports_By_id));

    public static ChainBuilder UC06_PATCH_v1_reports_By_id =
            group("UC06_PATCH_v1_reports_By_id").on(
                    exec(ReportsCase.UC06_PATCH_v1_reports_By_id));

    public static ChainBuilder UC07_PUT_v1_reports_By_id =
            group("UC07_PUT_v1_reports_By_id").on(
                    exec(ReportsCase.UC07_PUT_v1_reports_By_id));

    public static ScenarioBuilder scn = scenario("Reports")
            .feed(defaultFeeder)
            .feed(reports)
            .feed(reports)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_reports),
                            new Choice.WithWeight(14, UC02_POST_v1_reports),
                            new Choice.WithWeight(14, UC03_PATCH_v1_reports_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_reports_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_reports_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_reports_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_reports_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Reports")
            .feed(defaultFeeder)
            .feed(reports)
            .feed(reports)
            .feed(rqUidsFeeder)
            .exec(ReportsCase.UC01_GET_v1_reports)
            .exec(ReportsCase.UC02_POST_v1_reports)
            .exec(ReportsCase.UC03_PATCH_v1_reports_batch_identical)
            .exec(ReportsCase.UC04_DELETE_v1_reports_By_id)
            .exec(ReportsCase.UC05_GET_v1_reports_By_id)
            .exec(ReportsCase.UC06_PATCH_v1_reports_By_id)
            .exec(ReportsCase.UC07_PUT_v1_reports_By_id);
}
