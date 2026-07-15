package scenarios.ZK;

import cases.ZK.ComplianceTasksCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.tasks;
import static feeders.ZK.ZKFeeder.tasks;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceTasksScenario {

    public static ChainBuilder UC01_GET_v2_compliance_tasks_counters =
            group("UC01_GET_v2_compliance_tasks_counters").on(
                    exec(ComplianceTasksCase.UC01_GET_v2_compliance_tasks_counters));

    public static ChainBuilder UC02_POST_v2_compliance_tasks_documents_checks =
            group("UC02_POST_v2_compliance_tasks_documents_checks").on(
                    exec(ComplianceTasksCase.UC02_POST_v2_compliance_tasks_documents_checks));

    public static ChainBuilder UC03_PUT_v2_compliance_tasks_By_id_close_call =
            group("UC03_PUT_v2_compliance_tasks_By_id_close_call").on(
                    exec(ComplianceTasksCase.UC03_PUT_v2_compliance_tasks_By_id_close_call));

    public static ChainBuilder UC04_PUT_v2_compliance_tasks_By_id_close_documents_check =
            group("UC04_PUT_v2_compliance_tasks_By_id_close_documents_check").on(
                    exec(ComplianceTasksCase.UC04_PUT_v2_compliance_tasks_By_id_close_documents_check));

    public static ScenarioBuilder scn = scenario("ComplianceTasks")
            .feed(defaultFeeder)
            .feed(tasks)
            .feed(tasks)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(25, UC01_GET_v2_compliance_tasks_counters),
                            new Choice.WithWeight(25, UC02_POST_v2_compliance_tasks_documents_checks),
                            new Choice.WithWeight(25, UC03_PUT_v2_compliance_tasks_By_id_close_call),
                            new Choice.WithWeight(25, UC04_PUT_v2_compliance_tasks_By_id_close_documents_check)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceTasks")
            .feed(defaultFeeder)
            .feed(tasks)
            .feed(tasks)
            .feed(rqUidsFeeder)
            .exec(ComplianceTasksCase.UC01_GET_v2_compliance_tasks_counters)
            .exec(ComplianceTasksCase.UC02_POST_v2_compliance_tasks_documents_checks)
            .exec(ComplianceTasksCase.UC03_PUT_v2_compliance_tasks_By_id_close_call)
            .exec(ComplianceTasksCase.UC04_PUT_v2_compliance_tasks_By_id_close_documents_check);
}
