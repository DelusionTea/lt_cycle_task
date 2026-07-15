package scenarios.ZK;

import cases.ZK.EmployeeNotificationWhiteListsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.employeeNotificationWhiteLists;
import static feeders.ZK.ZKFeeder.employeeNotificationWhiteLists;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EmployeeNotificationWhiteListsScenario {

    public static ChainBuilder UC01_GET_v1_employee_notification_white_lists =
            group("UC01_GET_v1_employee_notification_white_lists").on(
                    exec(EmployeeNotificationWhiteListsCase.UC01_GET_v1_employee_notification_white_lists));

    public static ChainBuilder UC02_POST_v1_employee_notification_white_lists =
            group("UC02_POST_v1_employee_notification_white_lists").on(
                    exec(EmployeeNotificationWhiteListsCase.UC02_POST_v1_employee_notification_white_lists));

    public static ChainBuilder UC03_PATCH_v1_employee_notification_white_lists_batch_identical =
            group("UC03_PATCH_v1_employee_notification_white_lists_batch_identical").on(
                    exec(EmployeeNotificationWhiteListsCase.UC03_PATCH_v1_employee_notification_white_lists_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_employee_notification_white_lists_By_id =
            group("UC04_DELETE_v1_employee_notification_white_lists_By_id").on(
                    exec(EmployeeNotificationWhiteListsCase.UC04_DELETE_v1_employee_notification_white_lists_By_id));

    public static ChainBuilder UC05_GET_v1_employee_notification_white_lists_By_id =
            group("UC05_GET_v1_employee_notification_white_lists_By_id").on(
                    exec(EmployeeNotificationWhiteListsCase.UC05_GET_v1_employee_notification_white_lists_By_id));

    public static ChainBuilder UC06_PATCH_v1_employee_notification_white_lists_By_id =
            group("UC06_PATCH_v1_employee_notification_white_lists_By_id").on(
                    exec(EmployeeNotificationWhiteListsCase.UC06_PATCH_v1_employee_notification_white_lists_By_id));

    public static ChainBuilder UC07_PUT_v1_employee_notification_white_lists_By_id =
            group("UC07_PUT_v1_employee_notification_white_lists_By_id").on(
                    exec(EmployeeNotificationWhiteListsCase.UC07_PUT_v1_employee_notification_white_lists_By_id));

    public static ScenarioBuilder scn = scenario("EmployeeNotificationWhiteLists")
            .feed(defaultFeeder)
            .feed(employeeNotificationWhiteLists)
            .feed(employeeNotificationWhiteLists)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_employee_notification_white_lists),
                            new Choice.WithWeight(14, UC02_POST_v1_employee_notification_white_lists),
                            new Choice.WithWeight(14, UC03_PATCH_v1_employee_notification_white_lists_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_employee_notification_white_lists_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_employee_notification_white_lists_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_employee_notification_white_lists_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_employee_notification_white_lists_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug EmployeeNotificationWhiteLists")
            .feed(defaultFeeder)
            .feed(employeeNotificationWhiteLists)
            .feed(employeeNotificationWhiteLists)
            .feed(rqUidsFeeder)
            .exec(EmployeeNotificationWhiteListsCase.UC01_GET_v1_employee_notification_white_lists)
            .exec(EmployeeNotificationWhiteListsCase.UC02_POST_v1_employee_notification_white_lists)
            .exec(EmployeeNotificationWhiteListsCase.UC03_PATCH_v1_employee_notification_white_lists_batch_identical)
            .exec(EmployeeNotificationWhiteListsCase.UC04_DELETE_v1_employee_notification_white_lists_By_id)
            .exec(EmployeeNotificationWhiteListsCase.UC05_GET_v1_employee_notification_white_lists_By_id)
            .exec(EmployeeNotificationWhiteListsCase.UC06_PATCH_v1_employee_notification_white_lists_By_id)
            .exec(EmployeeNotificationWhiteListsCase.UC07_PUT_v1_employee_notification_white_lists_By_id);
}
