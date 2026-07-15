package scenarios.ZK;

import cases.ZK.ComplianceAssistantSubscriptionsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceAssistantSubscriptions;
import static feeders.ZK.ZKFeeder.complianceAssistantSubscriptions;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ComplianceAssistantSubscriptionsScenario {

    public static ChainBuilder UC01_GET_v1_compliance_assistant_subscriptions =
            group("UC01_GET_v1_compliance_assistant_subscriptions").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC01_GET_v1_compliance_assistant_subscriptions));

    public static ChainBuilder UC02_POST_v1_compliance_assistant_subscriptions =
            group("UC02_POST_v1_compliance_assistant_subscriptions").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC02_POST_v1_compliance_assistant_subscriptions));

    public static ChainBuilder UC03_PATCH_v1_compliance_assistant_subscriptions_batch_identical =
            group("UC03_PATCH_v1_compliance_assistant_subscriptions_batch_identical").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC03_PATCH_v1_compliance_assistant_subscriptions_batch_identical));

    public static ChainBuilder UC04_DELETE_v1_compliance_assistant_subscriptions_By_id =
            group("UC04_DELETE_v1_compliance_assistant_subscriptions_By_id").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC04_DELETE_v1_compliance_assistant_subscriptions_By_id));

    public static ChainBuilder UC05_GET_v1_compliance_assistant_subscriptions_By_id =
            group("UC05_GET_v1_compliance_assistant_subscriptions_By_id").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC05_GET_v1_compliance_assistant_subscriptions_By_id));

    public static ChainBuilder UC06_PATCH_v1_compliance_assistant_subscriptions_By_id =
            group("UC06_PATCH_v1_compliance_assistant_subscriptions_By_id").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC06_PATCH_v1_compliance_assistant_subscriptions_By_id));

    public static ChainBuilder UC07_PUT_v1_compliance_assistant_subscriptions_By_id =
            group("UC07_PUT_v1_compliance_assistant_subscriptions_By_id").on(
                    exec(ComplianceAssistantSubscriptionsCase.UC07_PUT_v1_compliance_assistant_subscriptions_By_id));

    public static ScenarioBuilder scn = scenario("ComplianceAssistantSubscriptions")
            .feed(defaultFeeder)
            .feed(complianceAssistantSubscriptions)
            .feed(complianceAssistantSubscriptions)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v1_compliance_assistant_subscriptions),
                            new Choice.WithWeight(14, UC02_POST_v1_compliance_assistant_subscriptions),
                            new Choice.WithWeight(14, UC03_PATCH_v1_compliance_assistant_subscriptions_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v1_compliance_assistant_subscriptions_By_id),
                            new Choice.WithWeight(14, UC05_GET_v1_compliance_assistant_subscriptions_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v1_compliance_assistant_subscriptions_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v1_compliance_assistant_subscriptions_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug ComplianceAssistantSubscriptions")
            .feed(defaultFeeder)
            .feed(complianceAssistantSubscriptions)
            .feed(complianceAssistantSubscriptions)
            .feed(rqUidsFeeder)
            .exec(ComplianceAssistantSubscriptionsCase.UC01_GET_v1_compliance_assistant_subscriptions)
            .exec(ComplianceAssistantSubscriptionsCase.UC02_POST_v1_compliance_assistant_subscriptions)
            .exec(ComplianceAssistantSubscriptionsCase.UC03_PATCH_v1_compliance_assistant_subscriptions_batch_identical)
            .exec(ComplianceAssistantSubscriptionsCase.UC04_DELETE_v1_compliance_assistant_subscriptions_By_id)
            .exec(ComplianceAssistantSubscriptionsCase.UC05_GET_v1_compliance_assistant_subscriptions_By_id)
            .exec(ComplianceAssistantSubscriptionsCase.UC06_PATCH_v1_compliance_assistant_subscriptions_By_id)
            .exec(ComplianceAssistantSubscriptionsCase.UC07_PUT_v1_compliance_assistant_subscriptions_By_id);
}
