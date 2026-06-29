package simulations.pprbSberrating.All;

import feeders.pprbSberrating.SberratingPPRB;
import io.gatling.javaapi.core.Simulation;
import scenarios.pprbSberrating.*;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;

public class OTT_all_debug extends Simulation{

    {
        setUp(
                AccessControlScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.AccessControlProtocol_balacovo),

                ArbitrScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.arbitrProtocol_balacovo),

                AssistantScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.assistantProtocol_balacovo),

                B2B_feedbacksScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.b2bProtocol_balacovo),

                CreditHistoryScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.creditHistoryProtocol_balacovo),

                FinancesScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.financeProtocol_balacovo),

                InspectionsScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.inspectionsProtocol_balacovo),

                LicensesScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.licensesProtocol_balacovo),

                RatingScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.ratingProtocol_balacovo),

                RiskScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.riskProtocol_balacovo),

                RegdataScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.regDataProtocol_balacovo),

                StatecontractsScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(SberratingPPRB.stateContractsProtocol_balacovo),

                StatisticsScenario.scn_ott_debug_1
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.statisticsProtocol_balacovo_1),

                StatisticsScenario.scn_ott_debug_2
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.statisticsProtocol_balacovo_2),

                SubscriptionsScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.subscriptionsProtocol_balacovo),

                SuspiciousFlowsScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.suspiciousFlowsProtocol_balacovo),

                TaxesScenario.scn_ott_debug
                        .injectOpen(atOnceUsers(5))
                        .protocols(SberratingPPRB.taxesProtocol_balacovo)
        );
    }
}
