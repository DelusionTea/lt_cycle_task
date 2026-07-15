package scenarios.ZK;

import cases.ZK.AttachmentsForClientCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.attachmentsForClient;
import static feeders.ZK.ZKFeeder.attachmentsForClient;
import static io.gatling.javaapi.core.CoreDsl.*;

public class AttachmentsForClientScenario {

    public static ChainBuilder UC01_GET_v2_attachments_for_client =
            group("UC01_GET_v2_attachments_for_client").on(
                    exec(AttachmentsForClientCase.UC01_GET_v2_attachments_for_client));

    public static ChainBuilder UC02_POST_v2_attachments_for_client =
            group("UC02_POST_v2_attachments_for_client").on(
                    exec(AttachmentsForClientCase.UC02_POST_v2_attachments_for_client));

    public static ChainBuilder UC03_PATCH_v2_attachments_for_client_batch_identical =
            group("UC03_PATCH_v2_attachments_for_client_batch_identical").on(
                    exec(AttachmentsForClientCase.UC03_PATCH_v2_attachments_for_client_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_attachments_for_client_By_id =
            group("UC04_DELETE_v2_attachments_for_client_By_id").on(
                    exec(AttachmentsForClientCase.UC04_DELETE_v2_attachments_for_client_By_id));

    public static ChainBuilder UC05_GET_v2_attachments_for_client_By_id =
            group("UC05_GET_v2_attachments_for_client_By_id").on(
                    exec(AttachmentsForClientCase.UC05_GET_v2_attachments_for_client_By_id));

    public static ChainBuilder UC06_PATCH_v2_attachments_for_client_By_id =
            group("UC06_PATCH_v2_attachments_for_client_By_id").on(
                    exec(AttachmentsForClientCase.UC06_PATCH_v2_attachments_for_client_By_id));

    public static ChainBuilder UC07_PUT_v2_attachments_for_client_By_id =
            group("UC07_PUT_v2_attachments_for_client_By_id").on(
                    exec(AttachmentsForClientCase.UC07_PUT_v2_attachments_for_client_By_id));

    public static ScenarioBuilder scn = scenario("AttachmentsForClient")
            .feed(defaultFeeder)
            .feed(attachmentsForClient)
            .feed(attachmentsForClient)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_attachments_for_client),
                            new Choice.WithWeight(14, UC02_POST_v2_attachments_for_client),
                            new Choice.WithWeight(14, UC03_PATCH_v2_attachments_for_client_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_attachments_for_client_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_attachments_for_client_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_attachments_for_client_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_attachments_for_client_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug AttachmentsForClient")
            .feed(defaultFeeder)
            .feed(attachmentsForClient)
            .feed(attachmentsForClient)
            .feed(rqUidsFeeder)
            .exec(AttachmentsForClientCase.UC01_GET_v2_attachments_for_client)
            .exec(AttachmentsForClientCase.UC02_POST_v2_attachments_for_client)
            .exec(AttachmentsForClientCase.UC03_PATCH_v2_attachments_for_client_batch_identical)
            .exec(AttachmentsForClientCase.UC04_DELETE_v2_attachments_for_client_By_id)
            .exec(AttachmentsForClientCase.UC05_GET_v2_attachments_for_client_By_id)
            .exec(AttachmentsForClientCase.UC06_PATCH_v2_attachments_for_client_By_id)
            .exec(AttachmentsForClientCase.UC07_PUT_v2_attachments_for_client_By_id);
}
