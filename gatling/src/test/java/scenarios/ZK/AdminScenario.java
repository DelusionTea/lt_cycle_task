package scenarios.ZK;

import cases.ZK.AdminCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;


import static io.gatling.javaapi.core.CoreDsl.*;

public class AdminScenario {

    public static ChainBuilder UC01_GET_v2_admin_attribute_display_configs =
            group("UC01_GET_v2_admin_attribute_display_configs").on(
                    exec(AdminCase.UC01_GET_v2_admin_attribute_display_configs));

    public static ChainBuilder UC02_POST_v2_admin_attribute_display_configs =
            group("UC02_POST_v2_admin_attribute_display_configs").on(
                    exec(AdminCase.UC02_POST_v2_admin_attribute_display_configs));

    public static ChainBuilder UC03_PATCH_v2_admin_attribute_display_configs_batch_identical =
            group("UC03_PATCH_v2_admin_attribute_display_configs_batch_identical").on(
                    exec(AdminCase.UC03_PATCH_v2_admin_attribute_display_configs_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_admin_attribute_display_configs_By_id =
            group("UC04_DELETE_v2_admin_attribute_display_configs_By_id").on(
                    exec(AdminCase.UC04_DELETE_v2_admin_attribute_display_configs_By_id));

    public static ChainBuilder UC05_GET_v2_admin_attribute_display_configs_By_id =
            group("UC05_GET_v2_admin_attribute_display_configs_By_id").on(
                    exec(AdminCase.UC05_GET_v2_admin_attribute_display_configs_By_id));

    public static ChainBuilder UC06_PATCH_v2_admin_attribute_display_configs_By_id =
            group("UC06_PATCH_v2_admin_attribute_display_configs_By_id").on(
                    exec(AdminCase.UC06_PATCH_v2_admin_attribute_display_configs_By_id));

    public static ChainBuilder UC07_PUT_v2_admin_attribute_display_configs_By_id =
            group("UC07_PUT_v2_admin_attribute_display_configs_By_id").on(
                    exec(AdminCase.UC07_PUT_v2_admin_attribute_display_configs_By_id));

    public static ChainBuilder UC08_GET_v2_admin_compliance_case_decisions =
            group("UC08_GET_v2_admin_compliance_case_decisions").on(
                    exec(AdminCase.UC08_GET_v2_admin_compliance_case_decisions));

    public static ChainBuilder UC09_POST_v2_admin_compliance_case_decisions =
            group("UC09_POST_v2_admin_compliance_case_decisions").on(
                    exec(AdminCase.UC09_POST_v2_admin_compliance_case_decisions));

    public static ChainBuilder UC10_PATCH_v2_admin_compliance_case_decisions_batch_identical =
            group("UC10_PATCH_v2_admin_compliance_case_decisions_batch_identical").on(
                    exec(AdminCase.UC10_PATCH_v2_admin_compliance_case_decisions_batch_identical));

    public static ChainBuilder UC11_DELETE_v2_admin_compliance_case_decisions_By_id =
            group("UC11_DELETE_v2_admin_compliance_case_decisions_By_id").on(
                    exec(AdminCase.UC11_DELETE_v2_admin_compliance_case_decisions_By_id));

    public static ChainBuilder UC12_GET_v2_admin_compliance_case_decisions_By_id =
            group("UC12_GET_v2_admin_compliance_case_decisions_By_id").on(
                    exec(AdminCase.UC12_GET_v2_admin_compliance_case_decisions_By_id));

    public static ChainBuilder UC13_PATCH_v2_admin_compliance_case_decisions_By_id =
            group("UC13_PATCH_v2_admin_compliance_case_decisions_By_id").on(
                    exec(AdminCase.UC13_PATCH_v2_admin_compliance_case_decisions_By_id));

    public static ChainBuilder UC14_PUT_v2_admin_compliance_case_decisions_By_id =
            group("UC14_PUT_v2_admin_compliance_case_decisions_By_id").on(
                    exec(AdminCase.UC14_PUT_v2_admin_compliance_case_decisions_By_id));

    public static ChainBuilder UC15_GET_v2_admin_compliance_checklists =
            group("UC15_GET_v2_admin_compliance_checklists").on(
                    exec(AdminCase.UC15_GET_v2_admin_compliance_checklists));

    public static ChainBuilder UC16_POST_v2_admin_compliance_checklists =
            group("UC16_POST_v2_admin_compliance_checklists").on(
                    exec(AdminCase.UC16_POST_v2_admin_compliance_checklists));

    public static ChainBuilder UC17_PATCH_v2_admin_compliance_checklists_batch_identical =
            group("UC17_PATCH_v2_admin_compliance_checklists_batch_identical").on(
                    exec(AdminCase.UC17_PATCH_v2_admin_compliance_checklists_batch_identical));

    public static ChainBuilder UC18_DELETE_v2_admin_compliance_checklists_By_id =
            group("UC18_DELETE_v2_admin_compliance_checklists_By_id").on(
                    exec(AdminCase.UC18_DELETE_v2_admin_compliance_checklists_By_id));

    public static ChainBuilder UC19_GET_v2_admin_compliance_checklists_By_id =
            group("UC19_GET_v2_admin_compliance_checklists_By_id").on(
                    exec(AdminCase.UC19_GET_v2_admin_compliance_checklists_By_id));

    public static ChainBuilder UC20_PATCH_v2_admin_compliance_checklists_By_id =
            group("UC20_PATCH_v2_admin_compliance_checklists_By_id").on(
                    exec(AdminCase.UC20_PATCH_v2_admin_compliance_checklists_By_id));

    public static ChainBuilder UC21_PUT_v2_admin_compliance_checklists_By_id =
            group("UC21_PUT_v2_admin_compliance_checklists_By_id").on(
                    exec(AdminCase.UC21_PUT_v2_admin_compliance_checklists_By_id));

    public static ChainBuilder UC22_GET_v2_admin_compliance_communications =
            group("UC22_GET_v2_admin_compliance_communications").on(
                    exec(AdminCase.UC22_GET_v2_admin_compliance_communications));

    public static ChainBuilder UC23_POST_v2_admin_compliance_communications =
            group("UC23_POST_v2_admin_compliance_communications").on(
                    exec(AdminCase.UC23_POST_v2_admin_compliance_communications));

    public static ChainBuilder UC24_PATCH_v2_admin_compliance_communications_batch_identical =
            group("UC24_PATCH_v2_admin_compliance_communications_batch_identical").on(
                    exec(AdminCase.UC24_PATCH_v2_admin_compliance_communications_batch_identical));

    public static ChainBuilder UC25_DELETE_v2_admin_compliance_communications_By_id =
            group("UC25_DELETE_v2_admin_compliance_communications_By_id").on(
                    exec(AdminCase.UC25_DELETE_v2_admin_compliance_communications_By_id));

    public static ChainBuilder UC26_GET_v2_admin_compliance_communications_By_id =
            group("UC26_GET_v2_admin_compliance_communications_By_id").on(
                    exec(AdminCase.UC26_GET_v2_admin_compliance_communications_By_id));

    public static ChainBuilder UC27_PATCH_v2_admin_compliance_communications_By_id =
            group("UC27_PATCH_v2_admin_compliance_communications_By_id").on(
                    exec(AdminCase.UC27_PATCH_v2_admin_compliance_communications_By_id));

    public static ChainBuilder UC28_PUT_v2_admin_compliance_communications_By_id =
            group("UC28_PUT_v2_admin_compliance_communications_By_id").on(
                    exec(AdminCase.UC28_PUT_v2_admin_compliance_communications_By_id));

    public static ChainBuilder UC29_GET_v2_admin_compliance_tasks =
            group("UC29_GET_v2_admin_compliance_tasks").on(
                    exec(AdminCase.UC29_GET_v2_admin_compliance_tasks));

    public static ChainBuilder UC30_POST_v2_admin_compliance_tasks =
            group("UC30_POST_v2_admin_compliance_tasks").on(
                    exec(AdminCase.UC30_POST_v2_admin_compliance_tasks));

    public static ChainBuilder UC31_PATCH_v2_admin_compliance_tasks_batch_identical =
            group("UC31_PATCH_v2_admin_compliance_tasks_batch_identical").on(
                    exec(AdminCase.UC31_PATCH_v2_admin_compliance_tasks_batch_identical));

    public static ChainBuilder UC32_DELETE_v2_admin_compliance_tasks_By_id =
            group("UC32_DELETE_v2_admin_compliance_tasks_By_id").on(
                    exec(AdminCase.UC32_DELETE_v2_admin_compliance_tasks_By_id));

    public static ChainBuilder UC33_GET_v2_admin_compliance_tasks_By_id =
            group("UC33_GET_v2_admin_compliance_tasks_By_id").on(
                    exec(AdminCase.UC33_GET_v2_admin_compliance_tasks_By_id));

    public static ChainBuilder UC34_PATCH_v2_admin_compliance_tasks_By_id =
            group("UC34_PATCH_v2_admin_compliance_tasks_By_id").on(
                    exec(AdminCase.UC34_PATCH_v2_admin_compliance_tasks_By_id));

    public static ChainBuilder UC35_PUT_v2_admin_compliance_tasks_By_id =
            group("UC35_PUT_v2_admin_compliance_tasks_By_id").on(
                    exec(AdminCase.UC35_PUT_v2_admin_compliance_tasks_By_id));

    public static ChainBuilder UC36_GET_v2_admin_fin_operations_settings =
            group("UC36_GET_v2_admin_fin_operations_settings").on(
                    exec(AdminCase.UC36_GET_v2_admin_fin_operations_settings));

    public static ChainBuilder UC37_POST_v2_admin_fin_operations_settings =
            group("UC37_POST_v2_admin_fin_operations_settings").on(
                    exec(AdminCase.UC37_POST_v2_admin_fin_operations_settings));

    public static ChainBuilder UC38_PATCH_v2_admin_fin_operations_settings_batch_identical =
            group("UC38_PATCH_v2_admin_fin_operations_settings_batch_identical").on(
                    exec(AdminCase.UC38_PATCH_v2_admin_fin_operations_settings_batch_identical));

    public static ChainBuilder UC39_DELETE_v2_admin_fin_operations_settings_By_id =
            group("UC39_DELETE_v2_admin_fin_operations_settings_By_id").on(
                    exec(AdminCase.UC39_DELETE_v2_admin_fin_operations_settings_By_id));

    public static ChainBuilder UC40_GET_v2_admin_fin_operations_settings_By_id =
            group("UC40_GET_v2_admin_fin_operations_settings_By_id").on(
                    exec(AdminCase.UC40_GET_v2_admin_fin_operations_settings_By_id));

    public static ChainBuilder UC41_PATCH_v2_admin_fin_operations_settings_By_id =
            group("UC41_PATCH_v2_admin_fin_operations_settings_By_id").on(
                    exec(AdminCase.UC41_PATCH_v2_admin_fin_operations_settings_By_id));

    public static ChainBuilder UC42_PUT_v2_admin_fin_operations_settings_By_id =
            group("UC42_PUT_v2_admin_fin_operations_settings_By_id").on(
                    exec(AdminCase.UC42_PUT_v2_admin_fin_operations_settings_By_id));

    public static ChainBuilder UC43_GET_v2_admin_organizations =
            group("UC43_GET_v2_admin_organizations").on(
                    exec(AdminCase.UC43_GET_v2_admin_organizations));

    public static ChainBuilder UC44_POST_v2_admin_organizations =
            group("UC44_POST_v2_admin_organizations").on(
                    exec(AdminCase.UC44_POST_v2_admin_organizations));

    public static ChainBuilder UC45_PATCH_v2_admin_organizations_batch_identical =
            group("UC45_PATCH_v2_admin_organizations_batch_identical").on(
                    exec(AdminCase.UC45_PATCH_v2_admin_organizations_batch_identical));

    public static ChainBuilder UC46_GET_v2_admin_organizations_check_actual_id =
            group("UC46_GET_v2_admin_organizations_check_actual_id").on(
                    exec(AdminCase.UC46_GET_v2_admin_organizations_check_actual_id));

    public static ChainBuilder UC47_POST_v2_admin_organizations_check_actual_id_or_create =
            group("UC47_POST_v2_admin_organizations_check_actual_id_or_create").on(
                    exec(AdminCase.UC47_POST_v2_admin_organizations_check_actual_id_or_create));

    public static ChainBuilder UC48_DELETE_v2_admin_organizations_By_id =
            group("UC48_DELETE_v2_admin_organizations_By_id").on(
                    exec(AdminCase.UC48_DELETE_v2_admin_organizations_By_id));

    public static ChainBuilder UC49_GET_v2_admin_organizations_By_id =
            group("UC49_GET_v2_admin_organizations_By_id").on(
                    exec(AdminCase.UC49_GET_v2_admin_organizations_By_id));

    public static ChainBuilder UC50_PATCH_v2_admin_organizations_By_id =
            group("UC50_PATCH_v2_admin_organizations_By_id").on(
                    exec(AdminCase.UC50_PATCH_v2_admin_organizations_By_id));

    public static ChainBuilder UC51_PUT_v2_admin_organizations_By_id =
            group("UC51_PUT_v2_admin_organizations_By_id").on(
                    exec(AdminCase.UC51_PUT_v2_admin_organizations_By_id));

    public static ChainBuilder UC52_GET_v3_admin_compliance_requests =
            group("UC52_GET_v3_admin_compliance_requests").on(
                    exec(AdminCase.UC52_GET_v3_admin_compliance_requests));

    public static ChainBuilder UC53_POST_v3_admin_compliance_requests =
            group("UC53_POST_v3_admin_compliance_requests").on(
                    exec(AdminCase.UC53_POST_v3_admin_compliance_requests));

    public static ChainBuilder UC54_PATCH_v3_admin_compliance_requests_batch_identical =
            group("UC54_PATCH_v3_admin_compliance_requests_batch_identical").on(
                    exec(AdminCase.UC54_PATCH_v3_admin_compliance_requests_batch_identical));

    public static ChainBuilder UC55_GET_v3_admin_compliance_requests_by_ucp_id_for_mmb =
            group("UC55_GET_v3_admin_compliance_requests_by_ucp_id_for_mmb").on(
                    exec(AdminCase.UC55_GET_v3_admin_compliance_requests_by_ucp_id_for_mmb));

    public static ChainBuilder UC56_DELETE_v3_admin_compliance_requests_By_id =
            group("UC56_DELETE_v3_admin_compliance_requests_By_id").on(
                    exec(AdminCase.UC56_DELETE_v3_admin_compliance_requests_By_id));

    public static ChainBuilder UC57_GET_v3_admin_compliance_requests_By_id =
            group("UC57_GET_v3_admin_compliance_requests_By_id").on(
                    exec(AdminCase.UC57_GET_v3_admin_compliance_requests_By_id));

    public static ChainBuilder UC58_PATCH_v3_admin_compliance_requests_By_id =
            group("UC58_PATCH_v3_admin_compliance_requests_By_id").on(
                    exec(AdminCase.UC58_PATCH_v3_admin_compliance_requests_By_id));

    public static ChainBuilder UC59_PUT_v3_admin_compliance_requests_By_id =
            group("UC59_PUT_v3_admin_compliance_requests_By_id").on(
                    exec(AdminCase.UC59_PUT_v3_admin_compliance_requests_By_id));

    public static ChainBuilder UC60_GET_v3_admin_fin_operations =
            group("UC60_GET_v3_admin_fin_operations").on(
                    exec(AdminCase.UC60_GET_v3_admin_fin_operations));

    public static ChainBuilder UC61_POST_v3_admin_fin_operations =
            group("UC61_POST_v3_admin_fin_operations").on(
                    exec(AdminCase.UC61_POST_v3_admin_fin_operations));

    public static ChainBuilder UC62_PATCH_v3_admin_fin_operations_batch_identical =
            group("UC62_PATCH_v3_admin_fin_operations_batch_identical").on(
                    exec(AdminCase.UC62_PATCH_v3_admin_fin_operations_batch_identical));

    public static ChainBuilder UC63_GET_v3_admin_fin_operations_by_ucp_id_for_mmb =
            group("UC63_GET_v3_admin_fin_operations_by_ucp_id_for_mmb").on(
                    exec(AdminCase.UC63_GET_v3_admin_fin_operations_by_ucp_id_for_mmb));

    public static ChainBuilder UC64_DELETE_v3_admin_fin_operations_By_id =
            group("UC64_DELETE_v3_admin_fin_operations_By_id").on(
                    exec(AdminCase.UC64_DELETE_v3_admin_fin_operations_By_id));

    public static ChainBuilder UC65_GET_v3_admin_fin_operations_By_id =
            group("UC65_GET_v3_admin_fin_operations_By_id").on(
                    exec(AdminCase.UC65_GET_v3_admin_fin_operations_By_id));

    public static ChainBuilder UC66_PATCH_v3_admin_fin_operations_By_id =
            group("UC66_PATCH_v3_admin_fin_operations_By_id").on(
                    exec(AdminCase.UC66_PATCH_v3_admin_fin_operations_By_id));

    public static ChainBuilder UC67_PUT_v3_admin_fin_operations_By_id =
            group("UC67_PUT_v3_admin_fin_operations_By_id").on(
                    exec(AdminCase.UC67_PUT_v3_admin_fin_operations_By_id));

    public static ScenarioBuilder scn = scenario("Admin")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(1, UC01_GET_v2_admin_attribute_display_configs),
                            new Choice.WithWeight(1, UC02_POST_v2_admin_attribute_display_configs),
                            new Choice.WithWeight(1, UC03_PATCH_v2_admin_attribute_display_configs_batch_identical),
                            new Choice.WithWeight(1, UC04_DELETE_v2_admin_attribute_display_configs_By_id),
                            new Choice.WithWeight(1, UC05_GET_v2_admin_attribute_display_configs_By_id),
                            new Choice.WithWeight(1, UC06_PATCH_v2_admin_attribute_display_configs_By_id),
                            new Choice.WithWeight(1, UC07_PUT_v2_admin_attribute_display_configs_By_id),
                            new Choice.WithWeight(1, UC08_GET_v2_admin_compliance_case_decisions),
                            new Choice.WithWeight(1, UC09_POST_v2_admin_compliance_case_decisions),
                            new Choice.WithWeight(1, UC10_PATCH_v2_admin_compliance_case_decisions_batch_identical),
                            new Choice.WithWeight(1, UC11_DELETE_v2_admin_compliance_case_decisions_By_id),
                            new Choice.WithWeight(1, UC12_GET_v2_admin_compliance_case_decisions_By_id),
                            new Choice.WithWeight(1, UC13_PATCH_v2_admin_compliance_case_decisions_By_id),
                            new Choice.WithWeight(1, UC14_PUT_v2_admin_compliance_case_decisions_By_id),
                            new Choice.WithWeight(1, UC15_GET_v2_admin_compliance_checklists),
                            new Choice.WithWeight(1, UC16_POST_v2_admin_compliance_checklists),
                            new Choice.WithWeight(1, UC17_PATCH_v2_admin_compliance_checklists_batch_identical),
                            new Choice.WithWeight(1, UC18_DELETE_v2_admin_compliance_checklists_By_id),
                            new Choice.WithWeight(1, UC19_GET_v2_admin_compliance_checklists_By_id),
                            new Choice.WithWeight(1, UC20_PATCH_v2_admin_compliance_checklists_By_id),
                            new Choice.WithWeight(1, UC21_PUT_v2_admin_compliance_checklists_By_id),
                            new Choice.WithWeight(1, UC22_GET_v2_admin_compliance_communications),
                            new Choice.WithWeight(1, UC23_POST_v2_admin_compliance_communications),
                            new Choice.WithWeight(1, UC24_PATCH_v2_admin_compliance_communications_batch_identical),
                            new Choice.WithWeight(1, UC25_DELETE_v2_admin_compliance_communications_By_id),
                            new Choice.WithWeight(1, UC26_GET_v2_admin_compliance_communications_By_id),
                            new Choice.WithWeight(1, UC27_PATCH_v2_admin_compliance_communications_By_id),
                            new Choice.WithWeight(1, UC28_PUT_v2_admin_compliance_communications_By_id),
                            new Choice.WithWeight(1, UC29_GET_v2_admin_compliance_tasks),
                            new Choice.WithWeight(1, UC30_POST_v2_admin_compliance_tasks),
                            new Choice.WithWeight(1, UC31_PATCH_v2_admin_compliance_tasks_batch_identical),
                            new Choice.WithWeight(1, UC32_DELETE_v2_admin_compliance_tasks_By_id),
                            new Choice.WithWeight(1, UC33_GET_v2_admin_compliance_tasks_By_id),
                            new Choice.WithWeight(1, UC34_PATCH_v2_admin_compliance_tasks_By_id),
                            new Choice.WithWeight(1, UC35_PUT_v2_admin_compliance_tasks_By_id),
                            new Choice.WithWeight(1, UC36_GET_v2_admin_fin_operations_settings),
                            new Choice.WithWeight(1, UC37_POST_v2_admin_fin_operations_settings),
                            new Choice.WithWeight(1, UC38_PATCH_v2_admin_fin_operations_settings_batch_identical),
                            new Choice.WithWeight(1, UC39_DELETE_v2_admin_fin_operations_settings_By_id),
                            new Choice.WithWeight(1, UC40_GET_v2_admin_fin_operations_settings_By_id),
                            new Choice.WithWeight(1, UC41_PATCH_v2_admin_fin_operations_settings_By_id),
                            new Choice.WithWeight(1, UC42_PUT_v2_admin_fin_operations_settings_By_id),
                            new Choice.WithWeight(1, UC43_GET_v2_admin_organizations),
                            new Choice.WithWeight(1, UC44_POST_v2_admin_organizations),
                            new Choice.WithWeight(1, UC45_PATCH_v2_admin_organizations_batch_identical),
                            new Choice.WithWeight(1, UC46_GET_v2_admin_organizations_check_actual_id),
                            new Choice.WithWeight(1, UC47_POST_v2_admin_organizations_check_actual_id_or_create),
                            new Choice.WithWeight(1, UC48_DELETE_v2_admin_organizations_By_id),
                            new Choice.WithWeight(1, UC49_GET_v2_admin_organizations_By_id),
                            new Choice.WithWeight(1, UC50_PATCH_v2_admin_organizations_By_id),
                            new Choice.WithWeight(1, UC51_PUT_v2_admin_organizations_By_id),
                            new Choice.WithWeight(1, UC52_GET_v3_admin_compliance_requests),
                            new Choice.WithWeight(1, UC53_POST_v3_admin_compliance_requests),
                            new Choice.WithWeight(1, UC54_PATCH_v3_admin_compliance_requests_batch_identical),
                            new Choice.WithWeight(1, UC55_GET_v3_admin_compliance_requests_by_ucp_id_for_mmb),
                            new Choice.WithWeight(1, UC56_DELETE_v3_admin_compliance_requests_By_id),
                            new Choice.WithWeight(1, UC57_GET_v3_admin_compliance_requests_By_id),
                            new Choice.WithWeight(1, UC58_PATCH_v3_admin_compliance_requests_By_id),
                            new Choice.WithWeight(1, UC59_PUT_v3_admin_compliance_requests_By_id),
                            new Choice.WithWeight(1, UC60_GET_v3_admin_fin_operations),
                            new Choice.WithWeight(1, UC61_POST_v3_admin_fin_operations),
                            new Choice.WithWeight(1, UC62_PATCH_v3_admin_fin_operations_batch_identical),
                            new Choice.WithWeight(1, UC63_GET_v3_admin_fin_operations_by_ucp_id_for_mmb),
                            new Choice.WithWeight(1, UC64_DELETE_v3_admin_fin_operations_By_id),
                            new Choice.WithWeight(1, UC65_GET_v3_admin_fin_operations_By_id),
                            new Choice.WithWeight(1, UC66_PATCH_v3_admin_fin_operations_By_id),
                            new Choice.WithWeight(1, UC67_PUT_v3_admin_fin_operations_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Admin")
            .feed(defaultFeeder)
            .feed(rqUidsFeeder)
            .exec(AdminCase.UC01_GET_v2_admin_attribute_display_configs)
            .exec(AdminCase.UC02_POST_v2_admin_attribute_display_configs)
            .exec(AdminCase.UC03_PATCH_v2_admin_attribute_display_configs_batch_identical)
            .exec(AdminCase.UC04_DELETE_v2_admin_attribute_display_configs_By_id)
            .exec(AdminCase.UC05_GET_v2_admin_attribute_display_configs_By_id)
            .exec(AdminCase.UC06_PATCH_v2_admin_attribute_display_configs_By_id)
            .exec(AdminCase.UC07_PUT_v2_admin_attribute_display_configs_By_id)
            .exec(AdminCase.UC08_GET_v2_admin_compliance_case_decisions)
            .exec(AdminCase.UC09_POST_v2_admin_compliance_case_decisions)
            .exec(AdminCase.UC10_PATCH_v2_admin_compliance_case_decisions_batch_identical)
            .exec(AdminCase.UC11_DELETE_v2_admin_compliance_case_decisions_By_id)
            .exec(AdminCase.UC12_GET_v2_admin_compliance_case_decisions_By_id)
            .exec(AdminCase.UC13_PATCH_v2_admin_compliance_case_decisions_By_id)
            .exec(AdminCase.UC14_PUT_v2_admin_compliance_case_decisions_By_id)
            .exec(AdminCase.UC15_GET_v2_admin_compliance_checklists)
            .exec(AdminCase.UC16_POST_v2_admin_compliance_checklists)
            .exec(AdminCase.UC17_PATCH_v2_admin_compliance_checklists_batch_identical)
            .exec(AdminCase.UC18_DELETE_v2_admin_compliance_checklists_By_id)
            .exec(AdminCase.UC19_GET_v2_admin_compliance_checklists_By_id)
            .exec(AdminCase.UC20_PATCH_v2_admin_compliance_checklists_By_id)
            .exec(AdminCase.UC21_PUT_v2_admin_compliance_checklists_By_id)
            .exec(AdminCase.UC22_GET_v2_admin_compliance_communications)
            .exec(AdminCase.UC23_POST_v2_admin_compliance_communications)
            .exec(AdminCase.UC24_PATCH_v2_admin_compliance_communications_batch_identical)
            .exec(AdminCase.UC25_DELETE_v2_admin_compliance_communications_By_id)
            .exec(AdminCase.UC26_GET_v2_admin_compliance_communications_By_id)
            .exec(AdminCase.UC27_PATCH_v2_admin_compliance_communications_By_id)
            .exec(AdminCase.UC28_PUT_v2_admin_compliance_communications_By_id)
            .exec(AdminCase.UC29_GET_v2_admin_compliance_tasks)
            .exec(AdminCase.UC30_POST_v2_admin_compliance_tasks)
            .exec(AdminCase.UC31_PATCH_v2_admin_compliance_tasks_batch_identical)
            .exec(AdminCase.UC32_DELETE_v2_admin_compliance_tasks_By_id)
            .exec(AdminCase.UC33_GET_v2_admin_compliance_tasks_By_id)
            .exec(AdminCase.UC34_PATCH_v2_admin_compliance_tasks_By_id)
            .exec(AdminCase.UC35_PUT_v2_admin_compliance_tasks_By_id)
            .exec(AdminCase.UC36_GET_v2_admin_fin_operations_settings)
            .exec(AdminCase.UC37_POST_v2_admin_fin_operations_settings)
            .exec(AdminCase.UC38_PATCH_v2_admin_fin_operations_settings_batch_identical)
            .exec(AdminCase.UC39_DELETE_v2_admin_fin_operations_settings_By_id)
            .exec(AdminCase.UC40_GET_v2_admin_fin_operations_settings_By_id)
            .exec(AdminCase.UC41_PATCH_v2_admin_fin_operations_settings_By_id)
            .exec(AdminCase.UC42_PUT_v2_admin_fin_operations_settings_By_id)
            .exec(AdminCase.UC43_GET_v2_admin_organizations)
            .exec(AdminCase.UC44_POST_v2_admin_organizations)
            .exec(AdminCase.UC45_PATCH_v2_admin_organizations_batch_identical)
            .exec(AdminCase.UC46_GET_v2_admin_organizations_check_actual_id)
            .exec(AdminCase.UC47_POST_v2_admin_organizations_check_actual_id_or_create)
            .exec(AdminCase.UC48_DELETE_v2_admin_organizations_By_id)
            .exec(AdminCase.UC49_GET_v2_admin_organizations_By_id)
            .exec(AdminCase.UC50_PATCH_v2_admin_organizations_By_id)
            .exec(AdminCase.UC51_PUT_v2_admin_organizations_By_id)
            .exec(AdminCase.UC52_GET_v3_admin_compliance_requests)
            .exec(AdminCase.UC53_POST_v3_admin_compliance_requests)
            .exec(AdminCase.UC54_PATCH_v3_admin_compliance_requests_batch_identical)
            .exec(AdminCase.UC55_GET_v3_admin_compliance_requests_by_ucp_id_for_mmb)
            .exec(AdminCase.UC56_DELETE_v3_admin_compliance_requests_By_id)
            .exec(AdminCase.UC57_GET_v3_admin_compliance_requests_By_id)
            .exec(AdminCase.UC58_PATCH_v3_admin_compliance_requests_By_id)
            .exec(AdminCase.UC59_PUT_v3_admin_compliance_requests_By_id)
            .exec(AdminCase.UC60_GET_v3_admin_fin_operations)
            .exec(AdminCase.UC61_POST_v3_admin_fin_operations)
            .exec(AdminCase.UC62_PATCH_v3_admin_fin_operations_batch_identical)
            .exec(AdminCase.UC63_GET_v3_admin_fin_operations_by_ucp_id_for_mmb)
            .exec(AdminCase.UC64_DELETE_v3_admin_fin_operations_By_id)
            .exec(AdminCase.UC65_GET_v3_admin_fin_operations_By_id)
            .exec(AdminCase.UC66_PATCH_v3_admin_fin_operations_By_id)
            .exec(AdminCase.UC67_PUT_v3_admin_fin_operations_By_id);
}
