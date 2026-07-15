package scenarios.ZK;

import cases.ZK.FileLoadingHistoryCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.fileLoadingHistory;
import static feeders.ZK.ZKFeeder.fileLoadingHistory;
import static io.gatling.javaapi.core.CoreDsl.*;

public class FileLoadingHistoryScenario {

    public static ChainBuilder UC01_GET_v4_file_loading_history =
            group("UC01_GET_v4_file_loading_history").on(
                    exec(FileLoadingHistoryCase.UC01_GET_v4_file_loading_history));

    public static ChainBuilder UC02_POST_v4_file_loading_history =
            group("UC02_POST_v4_file_loading_history").on(
                    exec(FileLoadingHistoryCase.UC02_POST_v4_file_loading_history));

    public static ChainBuilder UC03_PATCH_v4_file_loading_history_batch_identical =
            group("UC03_PATCH_v4_file_loading_history_batch_identical").on(
                    exec(FileLoadingHistoryCase.UC03_PATCH_v4_file_loading_history_batch_identical));

    public static ChainBuilder UC04_DELETE_v4_file_loading_history_By_id =
            group("UC04_DELETE_v4_file_loading_history_By_id").on(
                    exec(FileLoadingHistoryCase.UC04_DELETE_v4_file_loading_history_By_id));

    public static ChainBuilder UC05_GET_v4_file_loading_history_By_id =
            group("UC05_GET_v4_file_loading_history_By_id").on(
                    exec(FileLoadingHistoryCase.UC05_GET_v4_file_loading_history_By_id));

    public static ChainBuilder UC06_PATCH_v4_file_loading_history_By_id =
            group("UC06_PATCH_v4_file_loading_history_By_id").on(
                    exec(FileLoadingHistoryCase.UC06_PATCH_v4_file_loading_history_By_id));

    public static ChainBuilder UC07_PUT_v4_file_loading_history_By_id =
            group("UC07_PUT_v4_file_loading_history_By_id").on(
                    exec(FileLoadingHistoryCase.UC07_PUT_v4_file_loading_history_By_id));

    public static ScenarioBuilder scn = scenario("FileLoadingHistory")
            .feed(defaultFeeder)
            .feed(fileLoadingHistory)
            .feed(fileLoadingHistory)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v4_file_loading_history),
                            new Choice.WithWeight(14, UC02_POST_v4_file_loading_history),
                            new Choice.WithWeight(14, UC03_PATCH_v4_file_loading_history_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v4_file_loading_history_By_id),
                            new Choice.WithWeight(14, UC05_GET_v4_file_loading_history_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v4_file_loading_history_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v4_file_loading_history_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug FileLoadingHistory")
            .feed(defaultFeeder)
            .feed(fileLoadingHistory)
            .feed(fileLoadingHistory)
            .feed(rqUidsFeeder)
            .exec(FileLoadingHistoryCase.UC01_GET_v4_file_loading_history)
            .exec(FileLoadingHistoryCase.UC02_POST_v4_file_loading_history)
            .exec(FileLoadingHistoryCase.UC03_PATCH_v4_file_loading_history_batch_identical)
            .exec(FileLoadingHistoryCase.UC04_DELETE_v4_file_loading_history_By_id)
            .exec(FileLoadingHistoryCase.UC05_GET_v4_file_loading_history_By_id)
            .exec(FileLoadingHistoryCase.UC06_PATCH_v4_file_loading_history_By_id)
            .exec(FileLoadingHistoryCase.UC07_PUT_v4_file_loading_history_By_id);
}
