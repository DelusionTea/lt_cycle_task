package scenarios.pprbSberrating;

import cases.pprbSberrating.LicensesCaseOTT;
import cases.pprbSberrating.LicensesCase;
import config.ProfileConfig;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.ChainBuilder;
import static feeders.pprbSberrating.Methods.rqUidsFeeder;
import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * Шаблон рефакторинга сценария: веса randomSwitch вынесены из хардкода в
 * ProfileConfig (источник — profile.yaml -> profile.properties). Значения по
 * умолчанию сохранены прежними, чтобы поведение без profile.properties не менялось.
 *
 * Ключ сценария для ProfileConfig — "Licenses" (см. injection.scenarios в profile.yaml).
 */
public class LicensesScenario {

    private static final String SCN = "Licenses";

    // группы для корректного вызова ОТТ
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

    // OTT DEBUG
    public static ScenarioBuilder scn_ott_debug = scenario("Licenses OTT Debug")
            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .exec(LicensesCaseOTT.UC01_OTT)
            .exec(LicensesCaseOTT.UC01_POST_Licenses_Summary);

    // OTT STUB: веса из профиля (дефолты как раньше: 96/1/1/1)
    public static ScenarioBuilder scn_ott = scenario("Licenses Stub")
            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01", 96), UC01_POST_Licenses_Summary),
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC02", 1), UC02_POST_Licenses_List),
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC03", 1), UC03_POST_Licenses_Details),
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC04", 1), UC04_POST_Licenses_CustomParams)
                    )
            );

    // Основной нагрузочный сценарий: веса из профиля (дефолты как раньше: 25/25/20/30)
    public static ScenarioBuilder scn = scenario("Licenses Stub")
            .feed(feeders.pprbSberrating.LicensesFeeder.UC01_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC02_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC03_DB)
            .feed(feeders.pprbSberrating.LicensesFeeder.UC04_DB)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC01", 25), exec(LicensesCase.UC01_POST_Licenses_Summary)),
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC02", 25), exec(LicensesCase.UC02_POST_Licenses_List)),
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC03", 20), exec(LicensesCase.UC03_POST_Licenses_Details)),
                            new Choice.WithWeight(ProfileConfig.getWeight(SCN, "UC04", 30), exec(LicensesCase.UC04_POST_Licenses_CustomParams))
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Licenses")
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
