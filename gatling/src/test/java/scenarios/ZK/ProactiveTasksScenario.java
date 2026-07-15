package scenarios.ZK;

import cases.ZK.ProactiveTasksCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.proactiveTasks;
import static feeders.ZK.ZKFeeder.proactiveTasks;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ProactiveTasksScenario {

    public static ChainBuilder UC01_GET_v1_proactive_tasks =
            group("UC01_GET_v1_proactive_tasks").on(
                    exec(ProactiveTasksCase.UC01_GET_v1_proactive_tasks));

    public static ChainBuilder UC02_POST_v1_proactive_tasks =
            group("UC02_POST_v1_proactive_tasks").on(
                    exec(ProactiveTasksCase.UC02_POST_v1_proactive_tasks));

    public static ChainBuilder UC03_PATCH_v1_proactive_tasks_batch_identical =
            group("UC03_PATCH_v1_proactive_tasks_batch_identical").on(
                    exec(ProactiveTasksCase.UC03_PATCH_v1_proactive_tasks_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_proactive_tasks_By_id =
            group("UC04_DELETE_v1_proactive_tasks_By_id").on(
                    exec(ProactiveTasksCase.UC04_DELETE_v1_proactive_tasks_By_id));

    public static ChainBuilder UC05_GET_v1_proactive_tasks_By_id =
            group("UC05_GET_v1_proactive_tasks_By_id").on(
                    exec(ProactiveTasksCase.UC05_GET_v1_proactive_tasks_By_id));

    public static ChainBuilder UC06_PATCH_v1_proactive_tasks_By_id =
            group("UC06_PATCH_v1_proactive_tasks_By_id").on(
                    exec(ProactiveTasksCase.UC06_PATCH_v1_proactive_tasks_By_id));

    public static ChainBuilder UC07_PUT_v1_proactive_tasks_By_id =
            group("UC07_PUT_v1_proactive_tasks_By_id").on(
                    exec(ProactiveTasksCase.UC07_PUT_v1_proactive_tasks_By_id));

    public static ScenarioBuilder scn = scenario("ProactiveTasks")
            .feed(defaultFeeder)
            .feed(proactiveTasks)
            .feed(proactiveTasks)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_proactive_tasks),
                            new Choice.WithWeight(14, UC02_POST_v1_proactive_tasks),
                            new Choice.WithWeight(14, UC03_PATCH_v1_proactive_tasks_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_proactive_tasks_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_proactive_tasks_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_proactive_tasks_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_proactive_tasks_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ProactiveTasks")
            .feed(defaultFeeder)
            .feed(proactiveTasks)
            .feed(proactiveTasks)
            .feed(rqUidsFeeder)
            .exec(ProactiveTasksCase.UC01_GET_v1_proactive_tasks)
            .exec(ProactiveTasksCase.UC02_POST_v1_proactive_tasks)
            .exec(ProactiveTasksCase.UC03_PATCH_v1_proactive_tasks_batch_identical)
            .exec(ProactiveTasksCase.UC04_DELETE_v1_proactive_tasks_By_id)
            .exec(ProactiveTasksCase.UC05_GET_v1_proactive_tasks_By_id)
            .exec(ProactiveTasksCase.UC06_PATCH_v1_proactive_tasks_By_id)
            .exec(ProactiveTasksCase.UC07_PUT_v1_proactive_tasks_By_id);
}
