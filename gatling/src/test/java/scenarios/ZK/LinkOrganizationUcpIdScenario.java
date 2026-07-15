package scenarios.ZK;

import cases.ZK.LinkOrganizationUcpIdCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.linkOrganizationUcpId;
import static feeders.ZK.ZKFeeder.linkOrganizationUcpId;
import static io.gatling.javaapi.core.CoreDsl.*;

public class LinkOrganizationUcpIdScenario {

    public static ChainBuilder UC01_GET_v2_link_organization_ucp_id =
            group("UC01_GET_v2_link_organization_ucp_id").on(
                    exec(LinkOrganizationUcpIdCase.UC01_GET_v2_link_organization_ucp_id));

    public static ChainBuilder UC02_POST_v2_link_organization_ucp_id =
            group("UC02_POST_v2_link_organization_ucp_id").on(
                    exec(LinkOrganizationUcpIdCase.UC02_POST_v2_link_organization_ucp_id));

    public static ChainBuilder UC03_PATCH_v2_link_organization_ucp_id_batch_identical =
            group("UC03_PATCH_v2_link_organization_ucp_id_batch_identical").on(
                    exec(LinkOrganizationUcpIdCase.UC03_PATCH_v2_link_organization_ucp_id_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_link_organization_ucp_id_By_id =
            group("UC04_DELETE_v2_link_organization_ucp_id_By_id").on(
                    exec(LinkOrganizationUcpIdCase.UC04_DELETE_v2_link_organization_ucp_id_By_id));

    public static ChainBuilder UC05_GET_v2_link_organization_ucp_id_By_id =
            group("UC05_GET_v2_link_organization_ucp_id_By_id").on(
                    exec(LinkOrganizationUcpIdCase.UC05_GET_v2_link_organization_ucp_id_By_id));

    public static ChainBuilder UC06_PATCH_v2_link_organization_ucp_id_By_id =
            group("UC06_PATCH_v2_link_organization_ucp_id_By_id").on(
                    exec(LinkOrganizationUcpIdCase.UC06_PATCH_v2_link_organization_ucp_id_By_id));

    public static ChainBuilder UC07_PUT_v2_link_organization_ucp_id_By_id =
            group("UC07_PUT_v2_link_organization_ucp_id_By_id").on(
                    exec(LinkOrganizationUcpIdCase.UC07_PUT_v2_link_organization_ucp_id_By_id));

    public static ScenarioBuilder scn = scenario("LinkOrganizationUcpId")
            .feed(defaultFeeder)
            .feed(linkOrganizationUcpId)
            .feed(linkOrganizationUcpId)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_link_organization_ucp_id),
                            new Choice.WithWeight(14, UC02_POST_v2_link_organization_ucp_id),
                            new Choice.WithWeight(14, UC03_PATCH_v2_link_organization_ucp_id_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_link_organization_ucp_id_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_link_organization_ucp_id_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_link_organization_ucp_id_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_link_organization_ucp_id_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug LinkOrganizationUcpId")
            .feed(defaultFeeder)
            .feed(linkOrganizationUcpId)
            .feed(linkOrganizationUcpId)
            .feed(rqUidsFeeder)
            .exec(LinkOrganizationUcpIdCase.UC01_GET_v2_link_organization_ucp_id)
            .exec(LinkOrganizationUcpIdCase.UC02_POST_v2_link_organization_ucp_id)
            .exec(LinkOrganizationUcpIdCase.UC03_PATCH_v2_link_organization_ucp_id_batch_identical)
            .exec(LinkOrganizationUcpIdCase.UC04_DELETE_v2_link_organization_ucp_id_By_id)
            .exec(LinkOrganizationUcpIdCase.UC05_GET_v2_link_organization_ucp_id_By_id)
            .exec(LinkOrganizationUcpIdCase.UC06_PATCH_v2_link_organization_ucp_id_By_id)
            .exec(LinkOrganizationUcpIdCase.UC07_PUT_v2_link_organization_ucp_id_By_id);
}
