package scenarios.ZK;

import cases.ZK.JobHistoriesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.jobHistories;
import static feeders.ZK.ZKFeeder.jobHistories;
import static io.gatling.javaapi.core.CoreDsl.*;

public class JobHistoriesScenario {

    public static ChainBuilder UC01_GET_v2_job_histories =
            group("UC01_GET_v2_job_histories").on(
                    exec(JobHistoriesCase.UC01_GET_v2_job_histories));

    public static ChainBuilder UC02_POST_v2_job_histories =
            group("UC02_POST_v2_job_histories").on(
                    exec(JobHistoriesCase.UC02_POST_v2_job_histories));

    public static ChainBuilder UC03_PATCH_v2_job_histories_batch_identical =
            group("UC03_PATCH_v2_job_histories_batch_identical").on(
                    exec(JobHistoriesCase.UC03_PATCH_v2_job_histories_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_job_histories_By_id =
            group("UC04_DELETE_v2_job_histories_By_id").on(
                    exec(JobHistoriesCase.UC04_DELETE_v2_job_histories_By_id));

    public static ChainBuilder UC05_GET_v2_job_histories_By_id =
            group("UC05_GET_v2_job_histories_By_id").on(
                    exec(JobHistoriesCase.UC05_GET_v2_job_histories_By_id));

    public static ChainBuilder UC06_PATCH_v2_job_histories_By_id =
            group("UC06_PATCH_v2_job_histories_By_id").on(
                    exec(JobHistoriesCase.UC06_PATCH_v2_job_histories_By_id));

    public static ChainBuilder UC07_PUT_v2_job_histories_By_id =
            group("UC07_PUT_v2_job_histories_By_id").on(
                    exec(JobHistoriesCase.UC07_PUT_v2_job_histories_By_id));

    public static ScenarioBuilder scn = scenario("JobHistories")
            .feed(defaultFeeder)
            .feed(jobHistories)
            .feed(jobHistories)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_job_histories),
                            new Choice.WithWeight(14, UC02_POST_v2_job_histories),
                            new Choice.WithWeight(14, UC03_PATCH_v2_job_histories_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_job_histories_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_job_histories_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_job_histories_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_job_histories_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug JobHistories")
            .feed(defaultFeeder)
            .feed(jobHistories)
            .feed(jobHistories)
            .feed(rqUidsFeeder)
            .exec(JobHistoriesCase.UC01_GET_v2_job_histories)
            .exec(JobHistoriesCase.UC02_POST_v2_job_histories)
            .exec(JobHistoriesCase.UC03_PATCH_v2_job_histories_batch_identical)
            .exec(JobHistoriesCase.UC04_DELETE_v2_job_histories_By_id)
            .exec(JobHistoriesCase.UC05_GET_v2_job_histories_By_id)
            .exec(JobHistoriesCase.UC06_PATCH_v2_job_histories_By_id)
            .exec(JobHistoriesCase.UC07_PUT_v2_job_histories_By_id);
}
