package scenarios.ZK;

import cases.ZK.DboContractCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.dboContract;
import static feeders.ZK.ZKFeeder.dboContract;
import static io.gatling.javaapi.core.CoreDsl.*;

public class DboContractScenario {

    public static ChainBuilder UC01_GET_v2_dbo_contract =
            group("UC01_GET_v2_dbo_contract").on(
                    exec(DboContractCase.UC01_GET_v2_dbo_contract));

    public static ChainBuilder UC02_POST_v2_dbo_contract =
            group("UC02_POST_v2_dbo_contract").on(
                    exec(DboContractCase.UC02_POST_v2_dbo_contract));

    public static ChainBuilder UC03_PATCH_v2_dbo_contract_batch_identical =
            group("UC03_PATCH_v2_dbo_contract_batch_identical").on(
                    exec(DboContractCase.UC03_PATCH_v2_dbo_contract_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_dbo_contract_By_id =
            group("UC04_DELETE_v2_dbo_contract_By_id").on(
                    exec(DboContractCase.UC04_DELETE_v2_dbo_contract_By_id));

    public static ChainBuilder UC05_GET_v2_dbo_contract_By_id =
            group("UC05_GET_v2_dbo_contract_By_id").on(
                    exec(DboContractCase.UC05_GET_v2_dbo_contract_By_id));

    public static ChainBuilder UC06_PATCH_v2_dbo_contract_By_id =
            group("UC06_PATCH_v2_dbo_contract_By_id").on(
                    exec(DboContractCase.UC06_PATCH_v2_dbo_contract_By_id));

    public static ChainBuilder UC07_PUT_v2_dbo_contract_By_id =
            group("UC07_PUT_v2_dbo_contract_By_id").on(
                    exec(DboContractCase.UC07_PUT_v2_dbo_contract_By_id));

    public static ScenarioBuilder scn = scenario("DboContract")
            .feed(defaultFeeder)
            .feed(dboContract)
            .feed(dboContract)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_dbo_contract),
                            new Choice.WithWeight(14, UC02_POST_v2_dbo_contract),
                            new Choice.WithWeight(14, UC03_PATCH_v2_dbo_contract_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_dbo_contract_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_dbo_contract_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_dbo_contract_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_dbo_contract_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug DboContract")
            .feed(defaultFeeder)
            .feed(dboContract)
            .feed(dboContract)
            .feed(rqUidsFeeder)
            .exec(DboContractCase.UC01_GET_v2_dbo_contract)
            .exec(DboContractCase.UC02_POST_v2_dbo_contract)
            .exec(DboContractCase.UC03_PATCH_v2_dbo_contract_batch_identical)
            .exec(DboContractCase.UC04_DELETE_v2_dbo_contract_By_id)
            .exec(DboContractCase.UC05_GET_v2_dbo_contract_By_id)
            .exec(DboContractCase.UC06_PATCH_v2_dbo_contract_By_id)
            .exec(DboContractCase.UC07_PUT_v2_dbo_contract_By_id);
}
