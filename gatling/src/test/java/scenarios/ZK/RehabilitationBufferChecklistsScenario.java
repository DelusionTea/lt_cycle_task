package scenarios.ZK;

import cases.ZK.RehabilitationBufferChecklistsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class RehabilitationBufferChecklistsScenario {

    public static ChainBuilder UC01_GET_v2_rehabilitation_buffer_checklists =
            group("UC01_GET_v2_rehabilitation_buffer_checklists").on(
                    exec(RehabilitationBufferChecklistsCase.UC01_GET_v2_rehabilitation_buffer_checklists));

    public static ChainBuilder UC02_POST_v2_rehabilitation_buffer_checklists =
            group("UC02_POST_v2_rehabilitation_buffer_checklists").on(
                    exec(RehabilitationBufferChecklistsCase.UC02_POST_v2_rehabilitation_buffer_checklists));

    public static ChainBuilder UC03_PATCH_v2_rehabilitation_buffer_checklists_batch_identical =
            group("UC03_PATCH_v2_rehabilitation_buffer_checklists_batch_identical").on(
                    exec(RehabilitationBufferChecklistsCase.UC03_PATCH_v2_rehabilitation_buffer_checklists_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_rehabilitation_buffer_checklists_By_id =
            group("UC04_DELETE_v2_rehabilitation_buffer_checklists_By_id").on(
                    exec(RehabilitationBufferChecklistsCase.UC04_DELETE_v2_rehabilitation_buffer_checklists_By_id));

    public static ChainBuilder UC05_GET_v2_rehabilitation_buffer_checklists_By_id =
            group("UC05_GET_v2_rehabilitation_buffer_checklists_By_id").on(
                    exec(RehabilitationBufferChecklistsCase.UC05_GET_v2_rehabilitation_buffer_checklists_By_id));

    public static ChainBuilder UC06_PATCH_v2_rehabilitation_buffer_checklists_By_id =
            group("UC06_PATCH_v2_rehabilitation_buffer_checklists_By_id").on(
                    exec(RehabilitationBufferChecklistsCase.UC06_PATCH_v2_rehabilitation_buffer_checklists_By_id));

    public static ChainBuilder UC07_PUT_v2_rehabilitation_buffer_checklists_By_id =
            group("UC07_PUT_v2_rehabilitation_buffer_checklists_By_id").on(
                    exec(RehabilitationBufferChecklistsCase.UC07_PUT_v2_rehabilitation_buffer_checklists_By_id));

    public static ScenarioBuilder scn = scenario("RehabilitationBufferChecklists")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_rehabilitation_buffer_checklists),
                            new Choice.WithWeight(14, UC02_POST_v2_rehabilitation_buffer_checklists),
                            new Choice.WithWeight(14, UC03_PATCH_v2_rehabilitation_buffer_checklists_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_rehabilitation_buffer_checklists_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_rehabilitation_buffer_checklists_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_rehabilitation_buffer_checklists_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_rehabilitation_buffer_checklists_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug RehabilitationBufferChecklists")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(RehabilitationBufferChecklistsCase.UC01_GET_v2_rehabilitation_buffer_checklists)
            .exec(RehabilitationBufferChecklistsCase.UC02_POST_v2_rehabilitation_buffer_checklists)
            .exec(RehabilitationBufferChecklistsCase.UC03_PATCH_v2_rehabilitation_buffer_checklists_batch_identical)
            .exec(RehabilitationBufferChecklistsCase.UC04_DELETE_v2_rehabilitation_buffer_checklists_By_id)
            .exec(RehabilitationBufferChecklistsCase.UC05_GET_v2_rehabilitation_buffer_checklists_By_id)
            .exec(RehabilitationBufferChecklistsCase.UC06_PATCH_v2_rehabilitation_buffer_checklists_By_id)
            .exec(RehabilitationBufferChecklistsCase.UC07_PUT_v2_rehabilitation_buffer_checklists_By_id);
}
