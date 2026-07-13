package scenarios.ZK;

import cases.ZK.FinOperationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class FinOperationsScenario {

    public static ChainBuilder UC01_GET_v3_fin_operations =
            group("UC01_GET_v3_fin_operations").on(
                    exec(FinOperationsCase.UC01_GET_v3_fin_operations));

    public static ChainBuilder UC02_GET_v3_fin_operations_By_id =
            group("UC02_GET_v3_fin_operations_By_id").on(
                    exec(FinOperationsCase.UC02_GET_v3_fin_operations_By_id));

    public static ScenarioBuilder scn = scenario("FinOperations")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(50, UC01_GET_v3_fin_operations),
                            new Choice.WithWeight(50, UC02_GET_v3_fin_operations_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug FinOperations")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(FinOperationsCase.UC01_GET_v3_fin_operations)
            .exec(FinOperationsCase.UC02_GET_v3_fin_operations_By_id);
}
