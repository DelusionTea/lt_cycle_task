package scenarios.ZK;

import cases.ZK.OrganizationExtensionsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class OrganizationExtensionsScenario {

    public static ChainBuilder UC01_GET_v2_organization_extensions =
            group("UC01_GET_v2_organization_extensions").on(
                    exec(OrganizationExtensionsCase.UC01_GET_v2_organization_extensions));

    public static ChainBuilder UC02_POST_v2_organization_extensions =
            group("UC02_POST_v2_organization_extensions").on(
                    exec(OrganizationExtensionsCase.UC02_POST_v2_organization_extensions));

    public static ChainBuilder UC03_DELETE_v2_organization_extensions_batch =
            group("UC03_DELETE_v2_organization_extensions_batch").on(
                    exec(OrganizationExtensionsCase.UC03_DELETE_v2_organization_extensions_batch));

    public static ChainBuilder UC04_PATCH_v2_organization_extensions_batch =
            group("UC04_PATCH_v2_organization_extensions_batch").on(
                    exec(OrganizationExtensionsCase.UC04_PATCH_v2_organization_extensions_batch));

    public static ChainBuilder UC05_POST_v2_organization_extensions_batch =
            group("UC05_POST_v2_organization_extensions_batch").on(
                    exec(OrganizationExtensionsCase.UC05_POST_v2_organization_extensions_batch));

    public static ChainBuilder UC06_PUT_v2_organization_extensions_batch =
            group("UC06_PUT_v2_organization_extensions_batch").on(
                    exec(OrganizationExtensionsCase.UC06_PUT_v2_organization_extensions_batch));

    public static ChainBuilder UC07_PATCH_v2_organization_extensions_batch_identical =
            group("UC07_PATCH_v2_organization_extensions_batch_identical").on(
                    exec(OrganizationExtensionsCase.UC07_PATCH_v2_organization_extensions_batch_identical));

    public static ChainBuilder UC08_DELETE_v2_organization_extensions_By_id =
            group("UC08_DELETE_v2_organization_extensions_By_id").on(
                    exec(OrganizationExtensionsCase.UC08_DELETE_v2_organization_extensions_By_id));

    public static ChainBuilder UC09_GET_v2_organization_extensions_By_id =
            group("UC09_GET_v2_organization_extensions_By_id").on(
                    exec(OrganizationExtensionsCase.UC09_GET_v2_organization_extensions_By_id));

    public static ChainBuilder UC10_PATCH_v2_organization_extensions_By_id =
            group("UC10_PATCH_v2_organization_extensions_By_id").on(
                    exec(OrganizationExtensionsCase.UC10_PATCH_v2_organization_extensions_By_id));

    public static ChainBuilder UC11_PUT_v2_organization_extensions_By_id =
            group("UC11_PUT_v2_organization_extensions_By_id").on(
                    exec(OrganizationExtensionsCase.UC11_PUT_v2_organization_extensions_By_id));

    public static ScenarioBuilder scn = scenario("OrganizationExtensions")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(9, UC01_GET_v2_organization_extensions),
                            new Choice.WithWeight(9, UC02_POST_v2_organization_extensions),
                            new Choice.WithWeight(9, UC03_DELETE_v2_organization_extensions_batch),
                            new Choice.WithWeight(9, UC04_PATCH_v2_organization_extensions_batch),
                            new Choice.WithWeight(9, UC05_POST_v2_organization_extensions_batch),
                            new Choice.WithWeight(9, UC06_PUT_v2_organization_extensions_batch),
                            new Choice.WithWeight(9, UC07_PATCH_v2_organization_extensions_batch_identical),
                            new Choice.WithWeight(9, UC08_DELETE_v2_organization_extensions_By_id),
                            new Choice.WithWeight(9, UC09_GET_v2_organization_extensions_By_id),
                            new Choice.WithWeight(9, UC10_PATCH_v2_organization_extensions_By_id),
                            new Choice.WithWeight(9, UC11_PUT_v2_organization_extensions_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug OrganizationExtensions")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(OrganizationExtensionsCase.UC01_GET_v2_organization_extensions)
            .exec(OrganizationExtensionsCase.UC02_POST_v2_organization_extensions)
            .exec(OrganizationExtensionsCase.UC03_DELETE_v2_organization_extensions_batch)
            .exec(OrganizationExtensionsCase.UC04_PATCH_v2_organization_extensions_batch)
            .exec(OrganizationExtensionsCase.UC05_POST_v2_organization_extensions_batch)
            .exec(OrganizationExtensionsCase.UC06_PUT_v2_organization_extensions_batch)
            .exec(OrganizationExtensionsCase.UC07_PATCH_v2_organization_extensions_batch_identical)
            .exec(OrganizationExtensionsCase.UC08_DELETE_v2_organization_extensions_By_id)
            .exec(OrganizationExtensionsCase.UC09_GET_v2_organization_extensions_By_id)
            .exec(OrganizationExtensionsCase.UC10_PATCH_v2_organization_extensions_By_id)
            .exec(OrganizationExtensionsCase.UC11_PUT_v2_organization_extensions_By_id);
}
