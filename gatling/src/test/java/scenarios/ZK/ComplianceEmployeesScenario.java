package scenarios.ZK;

import cases.ZK.ComplianceEmployeesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceEmployeesScenario {

    public static ChainBuilder UC01_GET_v2_compliance_employees =
            group("UC01_GET_v2_compliance_employees").on(
                    exec(ComplianceEmployeesCase.UC01_GET_v2_compliance_employees));

    public static ChainBuilder UC02_POST_v2_compliance_employees =
            group("UC02_POST_v2_compliance_employees").on(
                    exec(ComplianceEmployeesCase.UC02_POST_v2_compliance_employees));

    public static ChainBuilder UC03_PATCH_v2_compliance_employees_batch_identical =
            group("UC03_PATCH_v2_compliance_employees_batch_identical").on(
                    exec(ComplianceEmployeesCase.UC03_PATCH_v2_compliance_employees_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_compliance_employees_By_id =
            group("UC04_DELETE_v2_compliance_employees_By_id").on(
                    exec(ComplianceEmployeesCase.UC04_DELETE_v2_compliance_employees_By_id));

    public static ChainBuilder UC05_GET_v2_compliance_employees_By_id =
            group("UC05_GET_v2_compliance_employees_By_id").on(
                    exec(ComplianceEmployeesCase.UC05_GET_v2_compliance_employees_By_id));

    public static ChainBuilder UC06_PATCH_v2_compliance_employees_By_id =
            group("UC06_PATCH_v2_compliance_employees_By_id").on(
                    exec(ComplianceEmployeesCase.UC06_PATCH_v2_compliance_employees_By_id));

    public static ChainBuilder UC07_PUT_v2_compliance_employees_By_id =
            group("UC07_PUT_v2_compliance_employees_By_id").on(
                    exec(ComplianceEmployeesCase.UC07_PUT_v2_compliance_employees_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceEmployees")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_compliance_employees),
                            new Choice.WithWeight(14, UC02_POST_v2_compliance_employees),
                            new Choice.WithWeight(14, UC03_PATCH_v2_compliance_employees_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_compliance_employees_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_compliance_employees_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_compliance_employees_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_compliance_employees_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceEmployees")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(ComplianceEmployeesCase.UC01_GET_v2_compliance_employees)
            .exec(ComplianceEmployeesCase.UC02_POST_v2_compliance_employees)
            .exec(ComplianceEmployeesCase.UC03_PATCH_v2_compliance_employees_batch_identical)
            .exec(ComplianceEmployeesCase.UC04_DELETE_v2_compliance_employees_By_id)
            .exec(ComplianceEmployeesCase.UC05_GET_v2_compliance_employees_By_id)
            .exec(ComplianceEmployeesCase.UC06_PATCH_v2_compliance_employees_By_id)
            .exec(ComplianceEmployeesCase.UC07_PUT_v2_compliance_employees_By_id);
}
