package scenarios.ZK;

import cases.ZK.EmployeeNotificationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.employeeNotifications;
import static feeders.ZK.ZKFeeder.employeeNotifications;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EmployeeNotificationsScenario {

    public static ChainBuilder UC01_GET_v1_employee_notifications =
            group("UC01_GET_v1_employee_notifications").on(
                    exec(EmployeeNotificationsCase.UC01_GET_v1_employee_notifications));

    public static ChainBuilder UC02_POST_v1_employee_notifications =
            group("UC02_POST_v1_employee_notifications").on(
                    exec(EmployeeNotificationsCase.UC02_POST_v1_employee_notifications));

    public static ChainBuilder UC03_PATCH_v1_employee_notifications_batch_identical =
            group("UC03_PATCH_v1_employee_notifications_batch_identical").on(
                    exec(EmployeeNotificationsCase.UC03_PATCH_v1_employee_notifications_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_employee_notifications_By_id =
            group("UC04_DELETE_v1_employee_notifications_By_id").on(
                    exec(EmployeeNotificationsCase.UC04_DELETE_v1_employee_notifications_By_id));

    public static ChainBuilder UC05_GET_v1_employee_notifications_By_id =
            group("UC05_GET_v1_employee_notifications_By_id").on(
                    exec(EmployeeNotificationsCase.UC05_GET_v1_employee_notifications_By_id));

    public static ChainBuilder UC06_PATCH_v1_employee_notifications_By_id =
            group("UC06_PATCH_v1_employee_notifications_By_id").on(
                    exec(EmployeeNotificationsCase.UC06_PATCH_v1_employee_notifications_By_id));

    public static ChainBuilder UC07_PUT_v1_employee_notifications_By_id =
            group("UC07_PUT_v1_employee_notifications_By_id").on(
                    exec(EmployeeNotificationsCase.UC07_PUT_v1_employee_notifications_By_id));

    public static ScenarioBuilder scn = scenario("EmployeeNotifications")
            .feed(defaultFeeder)
            .feed(employeeNotifications)
            .feed(employeeNotifications)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_employee_notifications),
                            new Choice.WithWeight(14, UC02_POST_v1_employee_notifications),
                            new Choice.WithWeight(14, UC03_PATCH_v1_employee_notifications_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_employee_notifications_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_employee_notifications_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_employee_notifications_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_employee_notifications_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EmployeeNotifications")
            .feed(defaultFeeder)
            .feed(employeeNotifications)
            .feed(employeeNotifications)
            .feed(rqUidsFeeder)
            .exec(EmployeeNotificationsCase.UC01_GET_v1_employee_notifications)
            .exec(EmployeeNotificationsCase.UC02_POST_v1_employee_notifications)
            .exec(EmployeeNotificationsCase.UC03_PATCH_v1_employee_notifications_batch_identical)
            .exec(EmployeeNotificationsCase.UC04_DELETE_v1_employee_notifications_By_id)
            .exec(EmployeeNotificationsCase.UC05_GET_v1_employee_notifications_By_id)
            .exec(EmployeeNotificationsCase.UC06_PATCH_v1_employee_notifications_By_id)
            .exec(EmployeeNotificationsCase.UC07_PUT_v1_employee_notifications_By_id);
}
