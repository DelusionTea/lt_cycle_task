package scenarios.ZK;

import cases.ZK.IntLocksCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class IntLocksScenario {

    public static ChainBuilder UC01_DELETE_v2_int_locks =
            group("UC01_DELETE_v2_int_locks").on(
                    exec(IntLocksCase.UC01_DELETE_v2_int_locks));

    public static ChainBuilder UC02_PATCH_v2_int_locks =
            group("UC02_PATCH_v2_int_locks").on(
                    exec(IntLocksCase.UC02_PATCH_v2_int_locks));

    public static ChainBuilder UC03_POST_v2_int_locks =
            group("UC03_POST_v2_int_locks").on(
                    exec(IntLocksCase.UC03_POST_v2_int_locks));

    public static ChainBuilder UC04_PUT_v2_int_locks =
            group("UC04_PUT_v2_int_locks").on(
                    exec(IntLocksCase.UC04_PUT_v2_int_locks));

    public static ChainBuilder UC05_DELETE_v2_int_locks_all =
            group("UC05_DELETE_v2_int_locks_all").on(
                    exec(IntLocksCase.UC05_DELETE_v2_int_locks_all));

    public static ChainBuilder UC06_GET_v2_int_locks_count =
            group("UC06_GET_v2_int_locks_count").on(
                    exec(IntLocksCase.UC06_GET_v2_int_locks_count));

    public static ChainBuilder UC07_DELETE_v2_int_locks_expired =
            group("UC07_DELETE_v2_int_locks_expired").on(
                    exec(IntLocksCase.UC07_DELETE_v2_int_locks_expired));

    public static ScenarioBuilder scn = scenario("IntLocks")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_DELETE_v2_int_locks),
                            new Choice.WithWeight(14, UC02_PATCH_v2_int_locks),
                            new Choice.WithWeight(14, UC03_POST_v2_int_locks),
                            new Choice.WithWeight(14, UC04_PUT_v2_int_locks),
                            new Choice.WithWeight(14, UC05_DELETE_v2_int_locks_all),
                            new Choice.WithWeight(14, UC06_GET_v2_int_locks_count),
                            new Choice.WithWeight(14, UC07_DELETE_v2_int_locks_expired)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug IntLocks")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(IntLocksCase.UC01_DELETE_v2_int_locks)
            .exec(IntLocksCase.UC02_PATCH_v2_int_locks)
            .exec(IntLocksCase.UC03_POST_v2_int_locks)
            .exec(IntLocksCase.UC04_PUT_v2_int_locks)
            .exec(IntLocksCase.UC05_DELETE_v2_int_locks_all)
            .exec(IntLocksCase.UC06_GET_v2_int_locks_count)
            .exec(IntLocksCase.UC07_DELETE_v2_int_locks_expired);
}
