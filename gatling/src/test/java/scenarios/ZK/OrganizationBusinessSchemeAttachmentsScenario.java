package scenarios.ZK;

import cases.ZK.OrganizationBusinessSchemeAttachmentsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class OrganizationBusinessSchemeAttachmentsScenario {

    public static ChainBuilder UC01_GET_v1_organization_business_scheme_attachments =
            group("UC01_GET_v1_organization_business_scheme_attachments").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC01_GET_v1_organization_business_scheme_attachments));

    public static ChainBuilder UC02_POST_v1_organization_business_scheme_attachments =
            group("UC02_POST_v1_organization_business_scheme_attachments").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC02_POST_v1_organization_business_scheme_attachments));

    public static ChainBuilder UC03_PATCH_v1_organization_business_scheme_attachments_batch_identical =
            group("UC03_PATCH_v1_organization_business_scheme_attachments_batch_identical").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC03_PATCH_v1_organization_business_scheme_attachments_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_organization_business_scheme_attachments_By_id =
            group("UC04_DELETE_v1_organization_business_scheme_attachments_By_id").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC04_DELETE_v1_organization_business_scheme_attachments_By_id));

    public static ChainBuilder UC05_GET_v1_organization_business_scheme_attachments_By_id =
            group("UC05_GET_v1_organization_business_scheme_attachments_By_id").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC05_GET_v1_organization_business_scheme_attachments_By_id));

    public static ChainBuilder UC06_PATCH_v1_organization_business_scheme_attachments_By_id =
            group("UC06_PATCH_v1_organization_business_scheme_attachments_By_id").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC06_PATCH_v1_organization_business_scheme_attachments_By_id));

    public static ChainBuilder UC07_PUT_v1_organization_business_scheme_attachments_By_id =
            group("UC07_PUT_v1_organization_business_scheme_attachments_By_id").on(
                    exec(OrganizationBusinessSchemeAttachmentsCase.UC07_PUT_v1_organization_business_scheme_attachments_By_id));

    public static ScenarioBuilder scn = scenario("OrganizationBusinessSchemeAttachments")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_organization_business_scheme_attachments),
                            new Choice.WithWeight(14, UC02_POST_v1_organization_business_scheme_attachments),
                            new Choice.WithWeight(14, UC03_PATCH_v1_organization_business_scheme_attachments_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_organization_business_scheme_attachments_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_organization_business_scheme_attachments_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_organization_business_scheme_attachments_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_organization_business_scheme_attachments_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug OrganizationBusinessSchemeAttachments")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC01_GET_v1_organization_business_scheme_attachments)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC02_POST_v1_organization_business_scheme_attachments)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC03_PATCH_v1_organization_business_scheme_attachments_batch_identical)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC04_DELETE_v1_organization_business_scheme_attachments_By_id)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC05_GET_v1_organization_business_scheme_attachments_By_id)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC06_PATCH_v1_organization_business_scheme_attachments_By_id)
            .exec(OrganizationBusinessSchemeAttachmentsCase.UC07_PUT_v1_organization_business_scheme_attachments_By_id);
}
