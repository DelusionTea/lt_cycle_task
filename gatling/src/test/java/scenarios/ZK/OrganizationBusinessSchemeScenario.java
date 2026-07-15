package scenarios.ZK;

import cases.ZK.OrganizationBusinessSchemeCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.organizationBusinessScheme;
import static feeders.ZK.ZKFeeder.organizationBusinessScheme;
import static io.gatling.javaapi.core.CoreDsl.*;

public class OrganizationBusinessSchemeScenario {

    public static ChainBuilder UC01_GET_v2_organization_business_scheme =
            group("UC01_GET_v2_organization_business_scheme").on(
                    exec(OrganizationBusinessSchemeCase.UC01_GET_v2_organization_business_scheme));

    public static ChainBuilder UC02_POST_v2_organization_business_scheme =
            group("UC02_POST_v2_organization_business_scheme").on(
                    exec(OrganizationBusinessSchemeCase.UC02_POST_v2_organization_business_scheme));

    public static ChainBuilder UC03_PATCH_v2_organization_business_scheme_batch_identical =
            group("UC03_PATCH_v2_organization_business_scheme_batch_identical").on(
                    exec(OrganizationBusinessSchemeCase.UC03_PATCH_v2_organization_business_scheme_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_organization_business_scheme_By_id =
            group("UC04_DELETE_v2_organization_business_scheme_By_id").on(
                    exec(OrganizationBusinessSchemeCase.UC04_DELETE_v2_organization_business_scheme_By_id));

    public static ChainBuilder UC05_GET_v2_organization_business_scheme_By_id =
            group("UC05_GET_v2_organization_business_scheme_By_id").on(
                    exec(OrganizationBusinessSchemeCase.UC05_GET_v2_organization_business_scheme_By_id));

    public static ChainBuilder UC06_PATCH_v2_organization_business_scheme_By_id =
            group("UC06_PATCH_v2_organization_business_scheme_By_id").on(
                    exec(OrganizationBusinessSchemeCase.UC06_PATCH_v2_organization_business_scheme_By_id));

    public static ChainBuilder UC07_PUT_v2_organization_business_scheme_By_id =
            group("UC07_PUT_v2_organization_business_scheme_By_id").on(
                    exec(OrganizationBusinessSchemeCase.UC07_PUT_v2_organization_business_scheme_By_id));

    public static ScenarioBuilder scn = scenario("OrganizationBusinessScheme")
            .feed(defaultFeeder)
            .feed(organizationBusinessScheme)
            .feed(organizationBusinessScheme)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_organization_business_scheme),
                            new Choice.WithWeight(14, UC02_POST_v2_organization_business_scheme),
                            new Choice.WithWeight(14, UC03_PATCH_v2_organization_business_scheme_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_organization_business_scheme_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_organization_business_scheme_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_organization_business_scheme_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_organization_business_scheme_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug OrganizationBusinessScheme")
            .feed(defaultFeeder)
            .feed(organizationBusinessScheme)
            .feed(organizationBusinessScheme)
            .feed(rqUidsFeeder)
            .exec(OrganizationBusinessSchemeCase.UC01_GET_v2_organization_business_scheme)
            .exec(OrganizationBusinessSchemeCase.UC02_POST_v2_organization_business_scheme)
            .exec(OrganizationBusinessSchemeCase.UC03_PATCH_v2_organization_business_scheme_batch_identical)
            .exec(OrganizationBusinessSchemeCase.UC04_DELETE_v2_organization_business_scheme_By_id)
            .exec(OrganizationBusinessSchemeCase.UC05_GET_v2_organization_business_scheme_By_id)
            .exec(OrganizationBusinessSchemeCase.UC06_PATCH_v2_organization_business_scheme_By_id)
            .exec(OrganizationBusinessSchemeCase.UC07_PUT_v2_organization_business_scheme_By_id);
}
