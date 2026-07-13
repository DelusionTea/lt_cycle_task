package scenarios.ZK;

import cases.ZK.OrganizationAttributesCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class OrganizationAttributesScenario {

    public static ChainBuilder UC01_GET_v1_organization_attributes =
            group("UC01_GET_v1_organization_attributes").on(
                    exec(OrganizationAttributesCase.UC01_GET_v1_organization_attributes));

    public static ChainBuilder UC02_POST_v1_organization_attributes =
            group("UC02_POST_v1_organization_attributes").on(
                    exec(OrganizationAttributesCase.UC02_POST_v1_organization_attributes));

    public static ChainBuilder UC03_DELETE_v1_organization_attributes_batch =
            group("UC03_DELETE_v1_organization_attributes_batch").on(
                    exec(OrganizationAttributesCase.UC03_DELETE_v1_organization_attributes_batch));

    public static ChainBuilder UC04_PATCH_v1_organization_attributes_batch =
            group("UC04_PATCH_v1_organization_attributes_batch").on(
                    exec(OrganizationAttributesCase.UC04_PATCH_v1_organization_attributes_batch));

    public static ChainBuilder UC05_POST_v1_organization_attributes_batch =
            group("UC05_POST_v1_organization_attributes_batch").on(
                    exec(OrganizationAttributesCase.UC05_POST_v1_organization_attributes_batch));

    public static ChainBuilder UC06_PUT_v1_organization_attributes_batch =
            group("UC06_PUT_v1_organization_attributes_batch").on(
                    exec(OrganizationAttributesCase.UC06_PUT_v1_organization_attributes_batch));

    public static ChainBuilder UC07_PATCH_v1_organization_attributes_batch_identical =
            group("UC07_PATCH_v1_organization_attributes_batch_identical").on(
                    exec(OrganizationAttributesCase.UC07_PATCH_v1_organization_attributes_batch_identical));

    public static ChainBuilder UC08_DELETE_v1_organization_attributes_By_id =
            group("UC08_DELETE_v1_organization_attributes_By_id").on(
                    exec(OrganizationAttributesCase.UC08_DELETE_v1_organization_attributes_By_id));

    public static ChainBuilder UC09_GET_v1_organization_attributes_By_id =
            group("UC09_GET_v1_organization_attributes_By_id").on(
                    exec(OrganizationAttributesCase.UC09_GET_v1_organization_attributes_By_id));

    public static ChainBuilder UC10_PATCH_v1_organization_attributes_By_id =
            group("UC10_PATCH_v1_organization_attributes_By_id").on(
                    exec(OrganizationAttributesCase.UC10_PATCH_v1_organization_attributes_By_id));

    public static ChainBuilder UC11_PUT_v1_organization_attributes_By_id =
            group("UC11_PUT_v1_organization_attributes_By_id").on(
                    exec(OrganizationAttributesCase.UC11_PUT_v1_organization_attributes_By_id));

    public static ScenarioBuilder scn = scenario("OrganizationAttributes")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(9, UC01_GET_v1_organization_attributes),
                            new Choice.WithWeight(9, UC02_POST_v1_organization_attributes),
                            new Choice.WithWeight(9, UC03_DELETE_v1_organization_attributes_batch),
                            new Choice.WithWeight(9, UC04_PATCH_v1_organization_attributes_batch),
                            new Choice.WithWeight(9, UC05_POST_v1_organization_attributes_batch),
                            new Choice.WithWeight(9, UC06_PUT_v1_organization_attributes_batch),
                            new Choice.WithWeight(9, UC07_PATCH_v1_organization_attributes_batch_identical),
                            new Choice.WithWeight(9, UC08_DELETE_v1_organization_attributes_By_id),
                            new Choice.WithWeight(9, UC09_GET_v1_organization_attributes_By_id),
                            new Choice.WithWeight(9, UC10_PATCH_v1_organization_attributes_By_id),
                            new Choice.WithWeight(9, UC11_PUT_v1_organization_attributes_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug OrganizationAttributes")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(OrganizationAttributesCase.UC01_GET_v1_organization_attributes)
            .exec(OrganizationAttributesCase.UC02_POST_v1_organization_attributes)
            .exec(OrganizationAttributesCase.UC03_DELETE_v1_organization_attributes_batch)
            .exec(OrganizationAttributesCase.UC04_PATCH_v1_organization_attributes_batch)
            .exec(OrganizationAttributesCase.UC05_POST_v1_organization_attributes_batch)
            .exec(OrganizationAttributesCase.UC06_PUT_v1_organization_attributes_batch)
            .exec(OrganizationAttributesCase.UC07_PATCH_v1_organization_attributes_batch_identical)
            .exec(OrganizationAttributesCase.UC08_DELETE_v1_organization_attributes_By_id)
            .exec(OrganizationAttributesCase.UC09_GET_v1_organization_attributes_By_id)
            .exec(OrganizationAttributesCase.UC10_PATCH_v1_organization_attributes_By_id)
            .exec(OrganizationAttributesCase.UC11_PUT_v1_organization_attributes_By_id);
}
