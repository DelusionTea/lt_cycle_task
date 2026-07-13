package simulations.ZK.All;

import feeders.ZK.ZKProtocol;

import io.gatling.javaapi.core.Simulation;

import scenarios.ZK.*;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;

/**
 * Debug-симуляция ZK: последовательный прогон всех UC каждого контроллера.
 * Аналог resources/OTT_all_debug.java для домена ZK.
 *
 * Запуск (из gatling/):
 *   mvn gatling:test -Dgatling.simulationClass=simulations.ZK.All.ZK_all_debug \
 *       -DzkBaseUrl=https://host:port
 */
public class ZK_all_debug extends Simulation {

    {
        setUp(
                AccountNumbersScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                AdClientMarkingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                AdCountersScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                AdQueuesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                AdminScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                AllTasksScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                AttachmentsForClientScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                BlackListsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                CatalogRecommendationsCibScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                CkrScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceAssistantSubscriptionsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceCaseMarkingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceCasesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceChecklistsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceDeputyEmployeesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceEmployeesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceHistoryClientScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                CompliancePrintedFormConfigScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceProductMarkingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceRecommendationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceRequestMarkingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceRequestsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceTasksScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceTemplateParametersScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ComplianceTemplatesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ConstructorScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ConstructorPfParametersScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                CounterpartiesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                DboContractScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                DigitalOfficesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                DigitalUserComplianceContactsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                DigitalUserCompliancesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EcmScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EmployeeNotificationWhiteListsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EmployeeNotificationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EmployeesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EventsHistoriesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EventsNoticeConditionsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EventsNoticeConditionsGroupsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EventsNoticeSettingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                EventsNoticesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FeatureFlagsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FileLoadingHistoryScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FileTransferTasksScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FinOperationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FinOperationsAttributesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FinOperationsReferenceMappingScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                FreeFormatLettersScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                GetAllCommunicationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                GetCalendarScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                IndividualRequestHistoryScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                IndividualRequestsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                InfrastructureScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                InfrastructuresScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                IntLocksScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                IntegrationLogReportsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                IvrScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                JobHistoriesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                LinkIndividualOrganizationScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                LinkOrganizationUcpIdScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                LinkTemplateParametersScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                OrganizationAttributesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                OrganizationBusinessSchemeScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                OrganizationBusinessSchemeAttachmentsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                OrganizationExtensionsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                OrganizationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                PilotLogScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                PremcoreScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                PrintFormsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ProactiveAttributesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ProactiveCatalogRecommendationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ProactiveCommunicationsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ProactiveOnboardingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ProactiveTasksScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ProcessSettingsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ReferenceValueLinksScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ReferenceValuesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                RehabilitationBufferChecklistsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                RehabilitationBufferRequestsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                ReportsScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                RequestHistoriesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                SbbolScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol),

                SystemPreferencesScenario.Debug
                        .injectOpen(atOnceUsers(1))
                        .protocols(ZKProtocol.httpProtocol)
        );
    }
}
