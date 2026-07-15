package scenarios.ZK;

import cases.ZK.OrganizationsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;


import static io.gatling.javaapi.core.CoreDsl.*;

public class OrganizationsScenario {

    public static ChainBuilder UC01_GET_v4_organizations =
            group("UC01_GET_v4_organizations").on(
                    exec(OrganizationsCase.UC01_GET_v4_organizations));

    public static ScenarioBuilder scn = scenario("Organizations")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(100, UC01_GET_v4_organizations)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Organizations")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(OrganizationsCase.UC01_GET_v4_organizations);
}
