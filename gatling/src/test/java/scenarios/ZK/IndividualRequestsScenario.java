package scenarios.ZK;

import cases.ZK.IndividualRequestsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.ckr;
import static feeders.ZK.ZKFeeder.ckr;
import static io.gatling.javaapi.core.CoreDsl.*;

public class IndividualRequestsScenario {

    public static ChainBuilder UC01_GET_v2_individual_requests =
            group("UC01_GET_v2_individual_requests").on(
                    exec(IndividualRequestsCase.UC01_GET_v2_individual_requests));

    public static ChainBuilder UC02_POST_v2_individual_requests =
            group("UC02_POST_v2_individual_requests").on(
                    exec(IndividualRequestsCase.UC02_POST_v2_individual_requests));

    public static ChainBuilder UC03_PATCH_v2_individual_requests_batch_identical =
            group("UC03_PATCH_v2_individual_requests_batch_identical").on(
                    exec(IndividualRequestsCase.UC03_PATCH_v2_individual_requests_batch_identical));

    public static ChainBuilder UC04_GET_v2_individual_requests_filter =
            group("UC04_GET_v2_individual_requests_filter").on(
                    exec(IndividualRequestsCase.UC04_GET_v2_individual_requests_filter));

    public static ChainBuilder UC05_GET_v2_individual_requests_get_individual_request_for_mmb =
            group("UC05_GET_v2_individual_requests_get_individual_request_for_mmb").on(
                    exec(IndividualRequestsCase.UC05_GET_v2_individual_requests_get_individual_request_for_mmb));

    public static ChainBuilder UC06_PUT_v2_individual_requests_process_sfl_ties_By_individualRequestId =
            group("UC06_PUT_v2_individual_requests_process_sfl_ties_By_individualRequestId").on(
                    exec(IndividualRequestsCase.UC06_PUT_v2_individual_requests_process_sfl_ties_By_individualRequestId));

    public static ChainBuilder UC07_GET_v2_individual_requests_with_task =
            group("UC07_GET_v2_individual_requests_with_task").on(
                    exec(IndividualRequestsCase.UC07_GET_v2_individual_requests_with_task));

    public static ChainBuilder UC08_DELETE_v2_individual_requests_By_id =
            group("UC08_DELETE_v2_individual_requests_By_id").on(
                    exec(IndividualRequestsCase.UC08_DELETE_v2_individual_requests_By_id));

    public static ChainBuilder UC09_GET_v2_individual_requests_By_id =
            group("UC09_GET_v2_individual_requests_By_id").on(
                    exec(IndividualRequestsCase.UC09_GET_v2_individual_requests_By_id));

    public static ChainBuilder UC10_PATCH_v2_individual_requests_By_id =
            group("UC10_PATCH_v2_individual_requests_By_id").on(
                    exec(IndividualRequestsCase.UC10_PATCH_v2_individual_requests_By_id));

    public static ChainBuilder UC11_PUT_v2_individual_requests_By_id =
            group("UC11_PUT_v2_individual_requests_By_id").on(
                    exec(IndividualRequestsCase.UC11_PUT_v2_individual_requests_By_id));

    public static ChainBuilder UC12_PUT_v2_individual_requests_By_individualRequestId_assign =
            group("UC12_PUT_v2_individual_requests_By_individualRequestId_assign").on(
                    exec(IndividualRequestsCase.UC12_PUT_v2_individual_requests_By_individualRequestId_assign));

    public static ScenarioBuilder scn = scenario("IndividualRequests")
            .feed(defaultFeeder)
            .feed(ckr)
            .feed(ckr)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(8, UC01_GET_v2_individual_requests),
                            new Choice.WithWeight(8, UC02_POST_v2_individual_requests),
                            new Choice.WithWeight(8, UC03_PATCH_v2_individual_requests_batch_identical),
                            new Choice.WithWeight(8, UC04_GET_v2_individual_requests_filter),
                            new Choice.WithWeight(8, UC05_GET_v2_individual_requests_get_individual_request_for_mmb),
                            new Choice.WithWeight(8, UC06_PUT_v2_individual_requests_process_sfl_ties_By_individualRequestId),
                            new Choice.WithWeight(8, UC07_GET_v2_individual_requests_with_task),
                            new Choice.WithWeight(8, UC08_DELETE_v2_individual_requests_By_id),
                            new Choice.WithWeight(8, UC09_GET_v2_individual_requests_By_id),
                            new Choice.WithWeight(8, UC10_PATCH_v2_individual_requests_By_id),
                            new Choice.WithWeight(8, UC11_PUT_v2_individual_requests_By_id),
                            new Choice.WithWeight(8, UC12_PUT_v2_individual_requests_By_individualRequestId_assign)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug IndividualRequests")
            .feed(defaultFeeder)
            .feed(ckr)
            .feed(ckr)
            .feed(rqUidsFeeder)
            .exec(IndividualRequestsCase.UC01_GET_v2_individual_requests)
            .exec(IndividualRequestsCase.UC02_POST_v2_individual_requests)
            .exec(IndividualRequestsCase.UC03_PATCH_v2_individual_requests_batch_identical)
            .exec(IndividualRequestsCase.UC04_GET_v2_individual_requests_filter)
            .exec(IndividualRequestsCase.UC05_GET_v2_individual_requests_get_individual_request_for_mmb)
            .exec(IndividualRequestsCase.UC06_PUT_v2_individual_requests_process_sfl_ties_By_individualRequestId)
            .exec(IndividualRequestsCase.UC07_GET_v2_individual_requests_with_task)
            .exec(IndividualRequestsCase.UC08_DELETE_v2_individual_requests_By_id)
            .exec(IndividualRequestsCase.UC09_GET_v2_individual_requests_By_id)
            .exec(IndividualRequestsCase.UC10_PATCH_v2_individual_requests_By_id)
            .exec(IndividualRequestsCase.UC11_PUT_v2_individual_requests_By_id)
            .exec(IndividualRequestsCase.UC12_PUT_v2_individual_requests_By_individualRequestId_assign);
}
