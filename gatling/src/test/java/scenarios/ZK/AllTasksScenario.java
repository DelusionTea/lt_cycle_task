package scenarios.ZK;

import cases.ZK.AllTasksCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class AllTasksScenario {

    public static ChainBuilder UC01_GET_v1_all_tasks_opened_tasks_By_ucpId =
            group("UC01_GET_v1_all_tasks_opened_tasks_By_ucpId").on(
                    exec(AllTasksCase.UC01_GET_v1_all_tasks_opened_tasks_By_ucpId));

    public static ScenarioBuilder scn = scenario("AllTasks")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v1_all_tasks_opened_tasks_By_ucpId)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug AllTasks")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(AllTasksCase.UC01_GET_v1_all_tasks_opened_tasks_By_ucpId);
}
