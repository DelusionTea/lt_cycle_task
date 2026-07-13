package scenarios.ZK;

import cases.ZK.GetAllCommunicationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class GetAllCommunicationsScenario {

    public static ChainBuilder UC01_GET_v1_get_all_communications =
            group("UC01_GET_v1_get_all_communications").on(
                    exec(GetAllCommunicationsCase.UC01_GET_v1_get_all_communications));

    public static ScenarioBuilder scn = scenario("GetAllCommunications")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v1_get_all_communications)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug GetAllCommunications")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(GetAllCommunicationsCase.UC01_GET_v1_get_all_communications);
}
