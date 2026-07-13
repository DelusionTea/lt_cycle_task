package scenarios.ZK;

import cases.ZK.RehabilitationBufferRequestsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class RehabilitationBufferRequestsScenario {

    public static ChainBuilder UC01_GET_v2_rehabilitation_buffer_requests =
            group("UC01_GET_v2_rehabilitation_buffer_requests").on(
                    exec(RehabilitationBufferRequestsCase.UC01_GET_v2_rehabilitation_buffer_requests));

    public static ChainBuilder UC02_POST_v2_rehabilitation_buffer_requests =
            group("UC02_POST_v2_rehabilitation_buffer_requests").on(
                    exec(RehabilitationBufferRequestsCase.UC02_POST_v2_rehabilitation_buffer_requests));

    public static ChainBuilder UC03_PATCH_v2_rehabilitation_buffer_requests_batch_identical =
            group("UC03_PATCH_v2_rehabilitation_buffer_requests_batch_identical").on(
                    exec(RehabilitationBufferRequestsCase.UC03_PATCH_v2_rehabilitation_buffer_requests_batch_identical));

    public static ChainBuilder UC04_POST_v2_rehabilitation_buffer_requests_process_rehabilitation_request_By_id =
            group("UC04_POST_v2_rehabilitation_buffer_requests_process_rehabilitation_request_By_id").on(
                    exec(RehabilitationBufferRequestsCase.UC04_POST_v2_rehabilitation_buffer_requests_process_rehabilitation_request_By_id));

    public static ChainBuilder UC05_DELETE_v2_rehabilitation_buffer_requests_By_id =
            group("UC05_DELETE_v2_rehabilitation_buffer_requests_By_id").on(
                    exec(RehabilitationBufferRequestsCase.UC05_DELETE_v2_rehabilitation_buffer_requests_By_id));

    public static ChainBuilder UC06_GET_v2_rehabilitation_buffer_requests_By_id =
            group("UC06_GET_v2_rehabilitation_buffer_requests_By_id").on(
                    exec(RehabilitationBufferRequestsCase.UC06_GET_v2_rehabilitation_buffer_requests_By_id));

    public static ChainBuilder UC07_PATCH_v2_rehabilitation_buffer_requests_By_id =
            group("UC07_PATCH_v2_rehabilitation_buffer_requests_By_id").on(
                    exec(RehabilitationBufferRequestsCase.UC07_PATCH_v2_rehabilitation_buffer_requests_By_id));

    public static ChainBuilder UC08_PUT_v2_rehabilitation_buffer_requests_By_id =
            group("UC08_PUT_v2_rehabilitation_buffer_requests_By_id").on(
                    exec(RehabilitationBufferRequestsCase.UC08_PUT_v2_rehabilitation_buffer_requests_By_id));

    public static ScenarioBuilder scn = scenario("RehabilitationBufferRequests")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(12, UC01_GET_v2_rehabilitation_buffer_requests),
                            new Choice.WithWeight(12, UC02_POST_v2_rehabilitation_buffer_requests),
                            new Choice.WithWeight(12, UC03_PATCH_v2_rehabilitation_buffer_requests_batch_identical),
                            new Choice.WithWeight(12, UC04_POST_v2_rehabilitation_buffer_requests_process_rehabilitation_request_By_id),
                            new Choice.WithWeight(12, UC05_DELETE_v2_rehabilitation_buffer_requests_By_id),
                            new Choice.WithWeight(12, UC06_GET_v2_rehabilitation_buffer_requests_By_id),
                            new Choice.WithWeight(12, UC07_PATCH_v2_rehabilitation_buffer_requests_By_id),
                            new Choice.WithWeight(12, UC08_PUT_v2_rehabilitation_buffer_requests_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug RehabilitationBufferRequests")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(RehabilitationBufferRequestsCase.UC01_GET_v2_rehabilitation_buffer_requests)
            .exec(RehabilitationBufferRequestsCase.UC02_POST_v2_rehabilitation_buffer_requests)
            .exec(RehabilitationBufferRequestsCase.UC03_PATCH_v2_rehabilitation_buffer_requests_batch_identical)
            .exec(RehabilitationBufferRequestsCase.UC04_POST_v2_rehabilitation_buffer_requests_process_rehabilitation_request_By_id)
            .exec(RehabilitationBufferRequestsCase.UC05_DELETE_v2_rehabilitation_buffer_requests_By_id)
            .exec(RehabilitationBufferRequestsCase.UC06_GET_v2_rehabilitation_buffer_requests_By_id)
            .exec(RehabilitationBufferRequestsCase.UC07_PATCH_v2_rehabilitation_buffer_requests_By_id)
            .exec(RehabilitationBufferRequestsCase.UC08_PUT_v2_rehabilitation_buffer_requests_By_id);
}
