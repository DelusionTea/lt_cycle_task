package scenarios.ZK;

import cases.ZK.EmployeesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EmployeesScenario {

    public static ChainBuilder UC01_GET_v2_employees_By_employeeNumber =
            group("UC01_GET_v2_employees_By_employeeNumber").on(
                    exec(EmployeesCase.UC01_GET_v2_employees_By_employeeNumber));

    public static ScenarioBuilder scn = scenario("Employees")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v2_employees_By_employeeNumber)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Employees")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(EmployeesCase.UC01_GET_v2_employees_By_employeeNumber);
}
