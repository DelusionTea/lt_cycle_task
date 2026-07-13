package scenarios.ZK;

import cases.ZK.InfrastructureCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class InfrastructureScenario {

    public static ChainBuilder UC01_GET_v1_infrastructure_entities =
            group("UC01_GET_v1_infrastructure_entities").on(
                    exec(InfrastructureCase.UC01_GET_v1_infrastructure_entities));

    public static ChainBuilder UC02_GET_v1_infrastructure_entities_By_entityName_filters =
            group("UC02_GET_v1_infrastructure_entities_By_entityName_filters").on(
                    exec(InfrastructureCase.UC02_GET_v1_infrastructure_entities_By_entityName_filters));

    public static ChainBuilder UC03_GET_v1_infrastructure_entities_By_entityName_filters_view_all =
            group("UC03_GET_v1_infrastructure_entities_By_entityName_filters_view_all").on(
                    exec(InfrastructureCase.UC03_GET_v1_infrastructure_entities_By_entityName_filters_view_all));

    public static ChainBuilder UC04_GET_v1_infrastructure_entities_By_entityName_filters_By_fieldName =
            group("UC04_GET_v1_infrastructure_entities_By_entityName_filters_By_fieldName").on(
                    exec(InfrastructureCase.UC04_GET_v1_infrastructure_entities_By_entityName_filters_By_fieldName));

    public static ChainBuilder UC05_GET_v1_infrastructure_entities_By_entityName_ordering =
            group("UC05_GET_v1_infrastructure_entities_By_entityName_ordering").on(
                    exec(InfrastructureCase.UC05_GET_v1_infrastructure_entities_By_entityName_ordering));

    public static ScenarioBuilder scn = scenario("Infrastructure")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(20, UC01_GET_v1_infrastructure_entities),
                            new Choice.WithWeight(20, UC02_GET_v1_infrastructure_entities_By_entityName_filters),
                            new Choice.WithWeight(20, UC03_GET_v1_infrastructure_entities_By_entityName_filters_view_all),
                            new Choice.WithWeight(20, UC04_GET_v1_infrastructure_entities_By_entityName_filters_By_fieldName),
                            new Choice.WithWeight(20, UC05_GET_v1_infrastructure_entities_By_entityName_ordering)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Infrastructure")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(InfrastructureCase.UC01_GET_v1_infrastructure_entities)
            .exec(InfrastructureCase.UC02_GET_v1_infrastructure_entities_By_entityName_filters)
            .exec(InfrastructureCase.UC03_GET_v1_infrastructure_entities_By_entityName_filters_view_all)
            .exec(InfrastructureCase.UC04_GET_v1_infrastructure_entities_By_entityName_filters_By_fieldName)
            .exec(InfrastructureCase.UC05_GET_v1_infrastructure_entities_By_entityName_ordering);
}
