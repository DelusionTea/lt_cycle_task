package scenarios.pprbSberrating;

import cases.pprbSberrating.LicensesCaseOTT;
import cases.pprbSberrating.LicensesCase;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.ChainBuilder;
import static feeders.pprbSberrating.Methods.rqUidsFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

public class LicensesScenario {
    //группы для корректного вызова ОТТ

    public static ChainBuilder UC01_POST_Licenses_Summary =
            group("UC01_POST_Licenses_Summary").on(
                    exec(LicensesCaseOTT.UC01_OTT)
                            .exec(LicensesCaseOTT.UC01_POST_Licenses_Summary));

    public static ChainBuilder UC02_POST_Licenses_List =
            group("UC02_POST_Licenses_List").on(
                    exec(LicensesCaseOTT.UC02_OTT)
                            .exec(LicensesCaseOTT.UC02_POST_Licenses_List));

    public static ChainBuilder UC03_POST_Licenses_Details =
            group("UC03_POST_Licenses_Details").on(
                    exec(LicensesCaseOTT.UC03_OTT)
                            .exec(LicensesCaseOTT.UC03_POST_Licenses_Details));

    public static ChainBuilder UC04_POST_Licenses_CustomParams =
            group("UC04_POST_Licenses_CustomParams").on(
                    exec(LicensesCaseOTT.UC04_OTT)
                            .exec(LicensesCaseOTT.UC04_POST_Licenses_CustomParams));
    //OTT DEBUG
    public static ScenarioBuilder scn_ott_debug = scenario("Licenses OTT Debug")
            //.feed(feeders.LicensesFeeder.UC01)
            //.feed(feeders.LicensesFeeder.UC02)
            //.feed(feeders.LicensesFeeder.UC03)
            //.feed(feeders.LicensesFeeder.UC04)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .exec(LicensesCaseOTT.UC01_OTT)
            .exec(LicensesCaseOTT.UC01_POST_Licenses_Summary);

    //OTT STUB
    public static ScenarioBuilder scn_ott = scenario("Licenses Stub")            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(96, UC01_POST_Licenses_Summary),
                            new Choice.WithWeight(1, UC02_POST_Licenses_List),
                            new Choice.WithWeight(1, UC03_POST_Licenses_Details),
                            new Choice.WithWeight(1, UC04_POST_Licenses_CustomParams)
                    )
            );

    public static ScenarioBuilder scn = scenario("Licenses Stub")
            //.feed(feeders.LicensesFeeder.UC01)
            //.feed(feeders.LicensesFeeder.UC02)
            //.feed(feeders.LicensesFeeder.UC03)
            //.feed(feeders.LicensesFeeder.UC04)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            //new Choice.WithWeight(97, exec(LicensesCase.UC01_POST_Licenses_Summary)),
                            //new Choice.WithWeight(1, exec(LicensesCase.UC02_POST_Licenses_List)),
                            //new Choice.WithWeight(1, exec(LicensesCase.UC03_POST_Licenses_Details)),
                            //new Choice.WithWeight(1, exec(LicensesCase.UC04_POST_Licenses_CustomParams))

                            new Choice.WithWeight(25, exec(LicensesCase.UC01_POST_Licenses_Summary)),
                            new Choice.WithWeight(25, exec(LicensesCase.UC02_POST_Licenses_List)),
                            new Choice.WithWeight(20, exec(LicensesCase.UC03_POST_Licenses_Details)),
                            new Choice.WithWeight(30, exec(LicensesCase.UC04_POST_Licenses_CustomParams))
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Licenses")
            //.feed(feeders.LicensesFeeder.UC01)
            //.feed(feeders.LicensesFeeder.UC02)
            //.feed(feeders.LicensesFeeder.UC03)
            //.feed(feeders.LicensesFeeder.UC04)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .exec(LicensesCase.UC01_POST_Licenses_Summary)
            .exec(LicensesCase.UC02_POST_Licenses_List)
            .exec(LicensesCase.UC03_POST_Licenses_Details)
            .exec(LicensesCase.UC04_POST_Licenses_CustomParams);
    

}