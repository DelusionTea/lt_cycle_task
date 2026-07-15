package scenarios.ZK;

import cases.ZK.InfrastructuresCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.infrastructures;
import static feeders.ZK.ZKFeeder.infrastructures;
import static io.gatling.javaapi.core.CoreDsl.*;

public class InfrastructuresScenario {

    public static ChainBuilder UC01_GET_v2_infrastructures =
            group("UC01_GET_v2_infrastructures").on(
                    exec(InfrastructuresCase.UC01_GET_v2_infrastructures));

    public static ChainBuilder UC02_POST_v2_infrastructures =
            group("UC02_POST_v2_infrastructures").on(
                    exec(InfrastructuresCase.UC02_POST_v2_infrastructures));

    public static ChainBuilder UC03_DELETE_v2_infrastructures_batch =
            group("UC03_DELETE_v2_infrastructures_batch").on(
                    exec(InfrastructuresCase.UC03_DELETE_v2_infrastructures_batch));

    public static ChainBuilder UC04_PATCH_v2_infrastructures_batch =
            group("UC04_PATCH_v2_infrastructures_batch").on(
                    exec(InfrastructuresCase.UC04_PATCH_v2_infrastructures_batch));

    public static ChainBuilder UC05_POST_v2_infrastructures_batch =
            group("UC05_POST_v2_infrastructures_batch").on(
                    exec(InfrastructuresCase.UC05_POST_v2_infrastructures_batch));

    public static ChainBuilder UC06_PUT_v2_infrastructures_batch =
            group("UC06_PUT_v2_infrastructures_batch").on(
                    exec(InfrastructuresCase.UC06_PUT_v2_infrastructures_batch));

    public static ChainBuilder UC07_PATCH_v2_infrastructures_batch_identical =
            group("UC07_PATCH_v2_infrastructures_batch_identical").on(
                    exec(InfrastructuresCase.UC07_PATCH_v2_infrastructures_batch_identical));

    public static ChainBuilder UC08_DELETE_v2_infrastructures_By_id =
            group("UC08_DELETE_v2_infrastructures_By_id").on(
                    exec(InfrastructuresCase.UC08_DELETE_v2_infrastructures_By_id));

    public static ChainBuilder UC09_GET_v2_infrastructures_By_id =
            group("UC09_GET_v2_infrastructures_By_id").on(
                    exec(InfrastructuresCase.UC09_GET_v2_infrastructures_By_id));

    public static ChainBuilder UC10_PATCH_v2_infrastructures_By_id =
            group("UC10_PATCH_v2_infrastructures_By_id").on(
                    exec(InfrastructuresCase.UC10_PATCH_v2_infrastructures_By_id));

    public static ChainBuilder UC11_PUT_v2_infrastructures_By_id =
            group("UC11_PUT_v2_infrastructures_By_id").on(
                    exec(InfrastructuresCase.UC11_PUT_v2_infrastructures_By_id));

    public static ScenarioBuilder scn = scenario("Infrastructures")
            .feed(defaultFeeder)
            .feed(infrastructures)
            .feed(infrastructures)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(9, UC01_GET_v2_infrastructures),
                            new Choice.WithWeight(9, UC02_POST_v2_infrastructures),
                            new Choice.WithWeight(9, UC03_DELETE_v2_infrastructures_batch),
                            new Choice.WithWeight(9, UC04_PATCH_v2_infrastructures_batch),
                            new Choice.WithWeight(9, UC05_POST_v2_infrastructures_batch),
                            new Choice.WithWeight(9, UC06_PUT_v2_infrastructures_batch),
                            new Choice.WithWeight(9, UC07_PATCH_v2_infrastructures_batch_identical),
                            new Choice.WithWeight(9, UC08_DELETE_v2_infrastructures_By_id),
                            new Choice.WithWeight(9, UC09_GET_v2_infrastructures_By_id),
                            new Choice.WithWeight(9, UC10_PATCH_v2_infrastructures_By_id),
                            new Choice.WithWeight(9, UC11_PUT_v2_infrastructures_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Infrastructures")
            .feed(defaultFeeder)
            .feed(infrastructures)
            .feed(infrastructures)
            .feed(rqUidsFeeder)
            .exec(InfrastructuresCase.UC01_GET_v2_infrastructures)
            .exec(InfrastructuresCase.UC02_POST_v2_infrastructures)
            .exec(InfrastructuresCase.UC03_DELETE_v2_infrastructures_batch)
            .exec(InfrastructuresCase.UC04_PATCH_v2_infrastructures_batch)
            .exec(InfrastructuresCase.UC05_POST_v2_infrastructures_batch)
            .exec(InfrastructuresCase.UC06_PUT_v2_infrastructures_batch)
            .exec(InfrastructuresCase.UC07_PATCH_v2_infrastructures_batch_identical)
            .exec(InfrastructuresCase.UC08_DELETE_v2_infrastructures_By_id)
            .exec(InfrastructuresCase.UC09_GET_v2_infrastructures_By_id)
            .exec(InfrastructuresCase.UC10_PATCH_v2_infrastructures_By_id)
            .exec(InfrastructuresCase.UC11_PUT_v2_infrastructures_By_id);
}
