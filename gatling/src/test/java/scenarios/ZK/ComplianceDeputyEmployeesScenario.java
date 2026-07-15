package scenarios.ZK;

import cases.ZK.ComplianceDeputyEmployeesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceDeputyEmployees;
import static feeders.ZK.ZKFeeder.complianceDeputyEmployees;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceDeputyEmployeesScenario {

    public static ChainBuilder UC01_GET_v2_compliance_deputy_employees =
            group("UC01_GET_v2_compliance_deputy_employees").on(
                    exec(ComplianceDeputyEmployeesCase.UC01_GET_v2_compliance_deputy_employees));

    public static ChainBuilder UC02_POST_v2_compliance_deputy_employees =
            group("UC02_POST_v2_compliance_deputy_employees").on(
                    exec(ComplianceDeputyEmployeesCase.UC02_POST_v2_compliance_deputy_employees));

    public static ChainBuilder UC03_PATCH_v2_compliance_deputy_employees_batch_identical =
            group("UC03_PATCH_v2_compliance_deputy_employees_batch_identical").on(
                    exec(ComplianceDeputyEmployeesCase.UC03_PATCH_v2_compliance_deputy_employees_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_deputy_employees_By_id =
            group("UC04_DELETE_v2_compliance_deputy_employees_By_id").on(
                    exec(ComplianceDeputyEmployeesCase.UC04_DELETE_v2_compliance_deputy_employees_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_deputy_employees_By_id =
            group("UC05_GET_v2_compliance_deputy_employees_By_id").on(
                    exec(ComplianceDeputyEmployeesCase.UC05_GET_v2_compliance_deputy_employees_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_deputy_employees_By_id =
            group("UC06_PATCH_v2_compliance_deputy_employees_By_id").on(
                    exec(ComplianceDeputyEmployeesCase.UC06_PATCH_v2_compliance_deputy_employees_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_deputy_employees_By_id =
            group("UC07_PUT_v2_compliance_deputy_employees_By_id").on(
                    exec(ComplianceDeputyEmployeesCase.UC07_PUT_v2_compliance_deputy_employees_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceDeputyEmployees")
            .feed(defaultFeeder)
            .feed(complianceDeputyEmployees)
            .feed(complianceDeputyEmployees)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_deputy_employees),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_deputy_employees),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_deputy_employees_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_deputy_employees_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_deputy_employees_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_deputy_employees_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_deputy_employees_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceDeputyEmployees")
            .feed(defaultFeeder)
            .feed(complianceDeputyEmployees)
            .feed(complianceDeputyEmployees)
            .feed(rqUidsFeeder)
            .exec(ComplianceDeputyEmployeesCase.UC01_GET_v2_compliance_deputy_employees)
            .exec(ComplianceDeputyEmployeesCase.UC02_POST_v2_compliance_deputy_employees)
            .exec(ComplianceDeputyEmployeesCase.UC03_PATCH_v2_compliance_deputy_employees_batch_identical)
            .exec(ComplianceDeputyEmployeesCase.UC04_DELETE_v2_compliance_deputy_employees_By_id)
            .exec(ComplianceDeputyEmployeesCase.UC05_GET_v2_compliance_deputy_employees_By_id)
            .exec(ComplianceDeputyEmployeesCase.UC06_PATCH_v2_compliance_deputy_employees_By_id)
            .exec(ComplianceDeputyEmployeesCase.UC07_PUT_v2_compliance_deputy_employees_By_id);
}
