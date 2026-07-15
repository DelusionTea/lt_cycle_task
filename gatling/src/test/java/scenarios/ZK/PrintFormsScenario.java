package scenarios.ZK;

import cases.ZK.PrintFormsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.constructor;
import static feeders.ZK.ZKFeeder.constructor;
import static io.gatling.javaapi.core.CoreDsl.*;

public class PrintFormsScenario {

    public static ChainBuilder UC01_GET_v3_print_forms =
            group("UC01_GET_v3_print_forms").on(
                    exec(PrintFormsCase.UC01_GET_v3_print_forms));

    public static ChainBuilder UC02_GET_v3_print_forms_download =
            group("UC02_GET_v3_print_forms_download").on(
                    exec(PrintFormsCase.UC02_GET_v3_print_forms_download));

    public static ChainBuilder UC03_POST_v3_print_forms_download_explanatory_note =
            group("UC03_POST_v3_print_forms_download_explanatory_note").on(
                    exec(PrintFormsCase.UC03_POST_v3_print_forms_download_explanatory_note));

    public static ChainBuilder UC04_POST_v3_print_forms_explanatory_note =
            group("UC04_POST_v3_print_forms_explanatory_note").on(
                    exec(PrintFormsCase.UC04_POST_v3_print_forms_explanatory_note));

    public static ScenarioBuilder scn = scenario("PrintForms")
            .feed(defaultFeeder)
            .feed(constructor)
            .feed(constructor)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(25, UC01_GET_v3_print_forms),
                            new Choice.WithWeight(25, UC02_GET_v3_print_forms_download),
                            new Choice.WithWeight(25, UC03_POST_v3_print_forms_download_explanatory_note),
                            new Choice.WithWeight(25, UC04_POST_v3_print_forms_explanatory_note)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug PrintForms")
            .feed(defaultFeeder)
            .feed(constructor)
            .feed(constructor)
            .feed(rqUidsFeeder)
            .exec(PrintFormsCase.UC01_GET_v3_print_forms)
            .exec(PrintFormsCase.UC02_GET_v3_print_forms_download)
            .exec(PrintFormsCase.UC03_POST_v3_print_forms_download_explanatory_note)
            .exec(PrintFormsCase.UC04_POST_v3_print_forms_explanatory_note);
}
