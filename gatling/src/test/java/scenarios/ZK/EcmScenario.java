package scenarios.ZK;

import cases.ZK.EcmCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceRequests;
import static feeders.ZK.ZKFeeder.complianceRequests;
import static io.gatling.javaapi.core.CoreDsl.*;

public class EcmScenario {

    public static ChainBuilder UC01_POST_v2_ecm_create_contents =
            group("UC01_POST_v2_ecm_create_contents").on(
                    exec(EcmCase.UC01_POST_v2_ecm_create_contents));

    public static ChainBuilder UC02_POST_v2_ecm_create_structure =
            group("UC02_POST_v2_ecm_create_structure").on(
                    exec(EcmCase.UC02_POST_v2_ecm_create_structure));

    public static ChainBuilder UC03_POST_v2_ecm_delete_objects =
            group("UC03_POST_v2_ecm_delete_objects").on(
                    exec(EcmCase.UC03_POST_v2_ecm_delete_objects));

    public static ScenarioBuilder scn = scenario("Ecm")
            .feed(defaultFeeder)
            .feed(complianceRequests)
            .feed(complianceRequests)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(33, UC01_POST_v2_ecm_create_contents),
                            new Choice.WithWeight(33, UC02_POST_v2_ecm_create_structure),
                            new Choice.WithWeight(33, UC03_POST_v2_ecm_delete_objects)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Ecm")
            .feed(defaultFeeder)
            .feed(complianceRequests)
            .feed(complianceRequests)
            .feed(rqUidsFeeder)
            .exec(EcmCase.UC01_POST_v2_ecm_create_contents)
            .exec(EcmCase.UC02_POST_v2_ecm_create_structure)
            .exec(EcmCase.UC03_POST_v2_ecm_delete_objects);
}
