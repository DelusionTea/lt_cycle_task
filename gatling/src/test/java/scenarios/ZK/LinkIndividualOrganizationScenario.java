package scenarios.ZK;

import cases.ZK.LinkIndividualOrganizationCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.linkIndividualOrganization;
import static feeders.ZK.ZKFeeder.linkIndividualOrganization;
import static io.gatling.javaapi.core.CoreDsl.*;

public class LinkIndividualOrganizationScenario {

    public static ChainBuilder UC01_GET_v2_link_individual_organization =
            group("UC01_GET_v2_link_individual_organization").on(
                    exec(LinkIndividualOrganizationCase.UC01_GET_v2_link_individual_organization));

    public static ChainBuilder UC02_POST_v2_link_individual_organization =
            group("UC02_POST_v2_link_individual_organization").on(
                    exec(LinkIndividualOrganizationCase.UC02_POST_v2_link_individual_organization));

    public static ChainBuilder UC03_PATCH_v2_link_individual_organization_batch_identical =
            group("UC03_PATCH_v2_link_individual_organization_batch_identical").on(
                    exec(LinkIndividualOrganizationCase.UC03_PATCH_v2_link_individual_organization_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_link_individual_organization_By_id =
            group("UC04_DELETE_v2_link_individual_organization_By_id").on(
                    exec(LinkIndividualOrganizationCase.UC04_DELETE_v2_link_individual_organization_By_id));

    public static ChainBuilder UC05_GET_v2_link_individual_organization_By_id =
            group("UC05_GET_v2_link_individual_organization_By_id").on(
                    exec(LinkIndividualOrganizationCase.UC05_GET_v2_link_individual_organization_By_id));

    public static ChainBuilder UC06_PATCH_v2_link_individual_organization_By_id =
            group("UC06_PATCH_v2_link_individual_organization_By_id").on(
                    exec(LinkIndividualOrganizationCase.UC06_PATCH_v2_link_individual_organization_By_id));

    public static ChainBuilder UC07_PUT_v2_link_individual_organization_By_id =
            group("UC07_PUT_v2_link_individual_organization_By_id").on(
                    exec(LinkIndividualOrganizationCase.UC07_PUT_v2_link_individual_organization_By_id));

    public static ScenarioBuilder scn = scenario("LinkIndividualOrganization")
            .feed(defaultFeeder)
            .feed(linkIndividualOrganization)
            .feed(linkIndividualOrganization)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_link_individual_organization),
                            new Choice.WithWeight(14, UC02_POST_v2_link_individual_organization),
                            new Choice.WithWeight(14, UC03_PATCH_v2_link_individual_organization_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_link_individual_organization_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_link_individual_organization_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_link_individual_organization_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_link_individual_organization_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug LinkIndividualOrganization")
            .feed(defaultFeeder)
            .feed(linkIndividualOrganization)
            .feed(linkIndividualOrganization)
            .feed(rqUidsFeeder)
            .exec(LinkIndividualOrganizationCase.UC01_GET_v2_link_individual_organization)
            .exec(LinkIndividualOrganizationCase.UC02_POST_v2_link_individual_organization)
            .exec(LinkIndividualOrganizationCase.UC03_PATCH_v2_link_individual_organization_batch_identical)
            .exec(LinkIndividualOrganizationCase.UC04_DELETE_v2_link_individual_organization_By_id)
            .exec(LinkIndividualOrganizationCase.UC05_GET_v2_link_individual_organization_By_id)
            .exec(LinkIndividualOrganizationCase.UC06_PATCH_v2_link_individual_organization_By_id)
            .exec(LinkIndividualOrganizationCase.UC07_PUT_v2_link_individual_organization_By_id);
}
