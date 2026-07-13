package scenarios.ZK;

import cases.ZK.FileTransferTasksCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class FileTransferTasksScenario {

    public static ChainBuilder UC01_GET_v1_file_transfer_tasks =
            group("UC01_GET_v1_file_transfer_tasks").on(
                    exec(FileTransferTasksCase.UC01_GET_v1_file_transfer_tasks));

    public static ChainBuilder UC02_POST_v1_file_transfer_tasks =
            group("UC02_POST_v1_file_transfer_tasks").on(
                    exec(FileTransferTasksCase.UC02_POST_v1_file_transfer_tasks));

    public static ChainBuilder UC03_PATCH_v1_file_transfer_tasks_batch_identical =
            group("UC03_PATCH_v1_file_transfer_tasks_batch_identical").on(
                    exec(FileTransferTasksCase.UC03_PATCH_v1_file_transfer_tasks_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_file_transfer_tasks_By_id =
            group("UC04_DELETE_v1_file_transfer_tasks_By_id").on(
                    exec(FileTransferTasksCase.UC04_DELETE_v1_file_transfer_tasks_By_id));

    public static ChainBuilder UC05_GET_v1_file_transfer_tasks_By_id =
            group("UC05_GET_v1_file_transfer_tasks_By_id").on(
                    exec(FileTransferTasksCase.UC05_GET_v1_file_transfer_tasks_By_id));

    public static ChainBuilder UC06_PATCH_v1_file_transfer_tasks_By_id =
            group("UC06_PATCH_v1_file_transfer_tasks_By_id").on(
                    exec(FileTransferTasksCase.UC06_PATCH_v1_file_transfer_tasks_By_id));

    public static ChainBuilder UC07_PUT_v1_file_transfer_tasks_By_id =
            group("UC07_PUT_v1_file_transfer_tasks_By_id").on(
                    exec(FileTransferTasksCase.UC07_PUT_v1_file_transfer_tasks_By_id));

    public static ScenarioBuilder scn = scenario("FileTransferTasks")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_file_transfer_tasks),
                            new Choice.WithWeight(14, UC02_POST_v1_file_transfer_tasks),
                            new Choice.WithWeight(14, UC03_PATCH_v1_file_transfer_tasks_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_file_transfer_tasks_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_file_transfer_tasks_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_file_transfer_tasks_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_file_transfer_tasks_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug FileTransferTasks")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(FileTransferTasksCase.UC01_GET_v1_file_transfer_tasks)
            .exec(FileTransferTasksCase.UC02_POST_v1_file_transfer_tasks)
            .exec(FileTransferTasksCase.UC03_PATCH_v1_file_transfer_tasks_batch_identical)
            .exec(FileTransferTasksCase.UC04_DELETE_v1_file_transfer_tasks_By_id)
            .exec(FileTransferTasksCase.UC05_GET_v1_file_transfer_tasks_By_id)
            .exec(FileTransferTasksCase.UC06_PATCH_v1_file_transfer_tasks_By_id)
            .exec(FileTransferTasksCase.UC07_PUT_v1_file_transfer_tasks_By_id);
}
