package scenarios.ZK;

import cases.ZK.SbbolCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.complianceRequests;
import static feeders.ZK.ZKFeeder.complianceRequests;
import static io.gatling.javaapi.core.CoreDsl.*;

public class SbbolScenario {

    public static ChainBuilder UC01_GET_v1_sbbol_compliance_requests_callback_cancel =
            group("UC01_GET_v1_sbbol_compliance_requests_callback_cancel").on(
                    exec(SbbolCase.UC01_GET_v1_sbbol_compliance_requests_callback_cancel));

    public static ChainBuilder UC02_POST_v1_sbbol_compliance_requests_callback_save =
            group("UC02_POST_v1_sbbol_compliance_requests_callback_save").on(
                    exec(SbbolCase.UC02_POST_v1_sbbol_compliance_requests_callback_save));

    public static ChainBuilder UC03_POST_v1_sbbol_compliance_requests_callback_update =
            group("UC03_POST_v1_sbbol_compliance_requests_callback_update").on(
                    exec(SbbolCase.UC03_POST_v1_sbbol_compliance_requests_callback_update));

    public static ChainBuilder UC04_GET_v1_sbbol_compliance_requests_mop_one =
            group("UC04_GET_v1_sbbol_compliance_requests_mop_one").on(
                    exec(SbbolCase.UC04_GET_v1_sbbol_compliance_requests_mop_one));

    public static ChainBuilder UC05_GET_v1_sbbol_compliance_requests_rehabilitation =
            group("UC05_GET_v1_sbbol_compliance_requests_rehabilitation").on(
                    exec(SbbolCase.UC05_GET_v1_sbbol_compliance_requests_rehabilitation));

    public static ChainBuilder UC06_GET_v1_sbbol_compliance_requests_start_rehabilitation_By_requestId =
            group("UC06_GET_v1_sbbol_compliance_requests_start_rehabilitation_By_requestId").on(
                    exec(SbbolCase.UC06_GET_v1_sbbol_compliance_requests_start_rehabilitation_By_requestId));

    public static ChainBuilder UC07_GET_v1_sbbol_free_format_letters_detail =
            group("UC07_GET_v1_sbbol_free_format_letters_detail").on(
                    exec(SbbolCase.UC07_GET_v1_sbbol_free_format_letters_detail));

    public static ChainBuilder UC08_POST_v2_sbbol_compliance_requests_attachments =
            group("UC08_POST_v2_sbbol_compliance_requests_attachments").on(
                    exec(SbbolCase.UC08_POST_v2_sbbol_compliance_requests_attachments));

    public static ChainBuilder UC09_POST_v2_sbbol_compliance_requests_attachments_batch =
            group("UC09_POST_v2_sbbol_compliance_requests_attachments_batch").on(
                    exec(SbbolCase.UC09_POST_v2_sbbol_compliance_requests_attachments_batch));

    public static ChainBuilder UC10_POST_v2_sbbol_compliance_requests_attachments_with_transfer =
            group("UC10_POST_v2_sbbol_compliance_requests_attachments_with_transfer").on(
                    exec(SbbolCase.UC10_POST_v2_sbbol_compliance_requests_attachments_with_transfer));

    public static ChainBuilder UC11_DELETE_v2_sbbol_compliance_requests_attachments_By_id =
            group("UC11_DELETE_v2_sbbol_compliance_requests_attachments_By_id").on(
                    exec(SbbolCase.UC11_DELETE_v2_sbbol_compliance_requests_attachments_By_id));

    public static ChainBuilder UC12_POST_v2_sbbol_compliance_requests_callback_detail =
            group("UC12_POST_v2_sbbol_compliance_requests_callback_detail").on(
                    exec(SbbolCase.UC12_POST_v2_sbbol_compliance_requests_callback_detail));

    public static ChainBuilder UC13_POST_v2_sbbol_compliance_requests_callback_info =
            group("UC13_POST_v2_sbbol_compliance_requests_callback_info").on(
                    exec(SbbolCase.UC13_POST_v2_sbbol_compliance_requests_callback_info));

    public static ChainBuilder UC14_GET_v2_sbbol_compliance_requests_compliance_profile =
            group("UC14_GET_v2_sbbol_compliance_requests_compliance_profile").on(
                    exec(SbbolCase.UC14_GET_v2_sbbol_compliance_requests_compliance_profile));

    public static ChainBuilder UC15_DELETE_v2_sbbol_compliance_requests_compliance_profile_alert =
            group("UC15_DELETE_v2_sbbol_compliance_requests_compliance_profile_alert").on(
                    exec(SbbolCase.UC15_DELETE_v2_sbbol_compliance_requests_compliance_profile_alert));

    public static ChainBuilder UC16_GET_v2_sbbol_compliance_requests_compliance_profile_contacts =
            group("UC16_GET_v2_sbbol_compliance_requests_compliance_profile_contacts").on(
                    exec(SbbolCase.UC16_GET_v2_sbbol_compliance_requests_compliance_profile_contacts));

    public static ChainBuilder UC17_POST_v2_sbbol_compliance_requests_compliance_profile_contacts =
            group("UC17_POST_v2_sbbol_compliance_requests_compliance_profile_contacts").on(
                    exec(SbbolCase.UC17_POST_v2_sbbol_compliance_requests_compliance_profile_contacts));

    public static ChainBuilder UC18_GET_v2_sbbol_compliance_requests_compliance_profile_counterparties =
            group("UC18_GET_v2_sbbol_compliance_requests_compliance_profile_counterparties").on(
                    exec(SbbolCase.UC18_GET_v2_sbbol_compliance_requests_compliance_profile_counterparties));

    public static ChainBuilder UC19_POST_v2_sbbol_compliance_requests_compliance_profile_counterparties =
            group("UC19_POST_v2_sbbol_compliance_requests_compliance_profile_counterparties").on(
                    exec(SbbolCase.UC19_POST_v2_sbbol_compliance_requests_compliance_profile_counterparties));

    public static ChainBuilder UC20_GET_v2_sbbol_compliance_requests_compliance_profile_detail =
            group("UC20_GET_v2_sbbol_compliance_requests_compliance_profile_detail").on(
                    exec(SbbolCase.UC20_GET_v2_sbbol_compliance_requests_compliance_profile_detail));

    public static ChainBuilder UC21_GET_v2_sbbol_compliance_requests_compliance_profile_dopInfo =
            group("UC21_GET_v2_sbbol_compliance_requests_compliance_profile_dopInfo").on(
                    exec(SbbolCase.UC21_GET_v2_sbbol_compliance_requests_compliance_profile_dopInfo));

    public static ChainBuilder UC22_POST_v2_sbbol_compliance_requests_compliance_profile_dopInfo =
            group("UC22_POST_v2_sbbol_compliance_requests_compliance_profile_dopInfo").on(
                    exec(SbbolCase.UC22_POST_v2_sbbol_compliance_requests_compliance_profile_dopInfo));

    public static ChainBuilder UC23_GET_v2_sbbol_compliance_requests_compliance_profile_mtb =
            group("UC23_GET_v2_sbbol_compliance_requests_compliance_profile_mtb").on(
                    exec(SbbolCase.UC23_GET_v2_sbbol_compliance_requests_compliance_profile_mtb));

    public static ChainBuilder UC24_POST_v2_sbbol_compliance_requests_compliance_profile_mtb =
            group("UC24_POST_v2_sbbol_compliance_requests_compliance_profile_mtb").on(
                    exec(SbbolCase.UC24_POST_v2_sbbol_compliance_requests_compliance_profile_mtb));

    public static ChainBuilder UC25_GET_v2_sbbol_compliance_requests_compliance_profile_organization =
            group("UC25_GET_v2_sbbol_compliance_requests_compliance_profile_organization").on(
                    exec(SbbolCase.UC25_GET_v2_sbbol_compliance_requests_compliance_profile_organization));

    public static ChainBuilder UC26_POST_v2_sbbol_compliance_requests_compliance_profile_organization =
            group("UC26_POST_v2_sbbol_compliance_requests_compliance_profile_organization").on(
                    exec(SbbolCase.UC26_POST_v2_sbbol_compliance_requests_compliance_profile_organization));

    public static ChainBuilder UC27_GET_v2_sbbol_compliance_requests_organization_by_ucp_id_By_ucpId =
            group("UC27_GET_v2_sbbol_compliance_requests_organization_by_ucp_id_By_ucpId").on(
                    exec(SbbolCase.UC27_GET_v2_sbbol_compliance_requests_organization_by_ucp_id_By_ucpId));

    public static ChainBuilder UC28_GET_v2_sbbol_compliance_requests_proactive =
            group("UC28_GET_v2_sbbol_compliance_requests_proactive").on(
                    exec(SbbolCase.UC28_GET_v2_sbbol_compliance_requests_proactive));

    public static ChainBuilder UC29_GET_v2_sbbol_compliance_requests_proactive_by_ucp =
            group("UC29_GET_v2_sbbol_compliance_requests_proactive_by_ucp").on(
                    exec(SbbolCase.UC29_GET_v2_sbbol_compliance_requests_proactive_by_ucp));

    public static ChainBuilder UC30_GET_v2_sbbol_compliance_requests_proactive_mop2 =
            group("UC30_GET_v2_sbbol_compliance_requests_proactive_mop2").on(
                    exec(SbbolCase.UC30_GET_v2_sbbol_compliance_requests_proactive_mop2));

    public static ChainBuilder UC31_GET_v2_sbbol_compliance_requests_proactive_onboarding =
            group("UC31_GET_v2_sbbol_compliance_requests_proactive_onboarding").on(
                    exec(SbbolCase.UC31_GET_v2_sbbol_compliance_requests_proactive_onboarding));

    public static ChainBuilder UC32_GET_v2_sbbol_compliance_requests_request_online_control =
            group("UC32_GET_v2_sbbol_compliance_requests_request_online_control").on(
                    exec(SbbolCase.UC32_GET_v2_sbbol_compliance_requests_request_online_control));

    public static ChainBuilder UC33_POST_v2_sbbol_compliance_requests_send_to_bank_By_requestId =
            group("UC33_POST_v2_sbbol_compliance_requests_send_to_bank_By_requestId").on(
                    exec(SbbolCase.UC33_POST_v2_sbbol_compliance_requests_send_to_bank_By_requestId));

    public static ChainBuilder UC34_GET_v2_sbbol_compliance_requests_unread_posts_counter =
            group("UC34_GET_v2_sbbol_compliance_requests_unread_posts_counter").on(
                    exec(SbbolCase.UC34_GET_v2_sbbol_compliance_requests_unread_posts_counter));

    public static ChainBuilder UC35_GET_v2_sbbol_free_format_letters_detail_By_freeFormatLetterId =
            group("UC35_GET_v2_sbbol_free_format_letters_detail_By_freeFormatLetterId").on(
                    exec(SbbolCase.UC35_GET_v2_sbbol_free_format_letters_detail_By_freeFormatLetterId));

    public static ChainBuilder UC36_GET_v2_sbbol_free_format_letters_list =
            group("UC36_GET_v2_sbbol_free_format_letters_list").on(
                    exec(SbbolCase.UC36_GET_v2_sbbol_free_format_letters_list));

    public static ChainBuilder UC37_POST_v2_sbbol_free_format_letters_send_to_bank_By_freeFormatLetterId =
            group("UC37_POST_v2_sbbol_free_format_letters_send_to_bank_By_freeFormatLetterId").on(
                    exec(SbbolCase.UC37_POST_v2_sbbol_free_format_letters_send_to_bank_By_freeFormatLetterId));

    public static ChainBuilder UC38_POST_v3_sbbol_compliance_requests_callback_detail =
            group("UC38_POST_v3_sbbol_compliance_requests_callback_detail").on(
                    exec(SbbolCase.UC38_POST_v3_sbbol_compliance_requests_callback_detail));

    public static ChainBuilder UC39_POST_v3_sbbol_compliance_requests_detail =
            group("UC39_POST_v3_sbbol_compliance_requests_detail").on(
                    exec(SbbolCase.UC39_POST_v3_sbbol_compliance_requests_detail));

    public static ChainBuilder UC40_GET_v3_sbbol_compliance_requests_fin_operation_list =
            group("UC40_GET_v3_sbbol_compliance_requests_fin_operation_list").on(
                    exec(SbbolCase.UC40_GET_v3_sbbol_compliance_requests_fin_operation_list));

    public static ChainBuilder UC41_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_case =
            group("UC41_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_case").on(
                    exec(SbbolCase.UC41_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_case));

    public static ChainBuilder UC42_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_id =
            group("UC42_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_id").on(
                    exec(SbbolCase.UC42_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_id));

    public static ChainBuilder UC43_GET_v3_sbbol_compliance_requests_fin_operation_operations_by_request =
            group("UC43_GET_v3_sbbol_compliance_requests_fin_operation_operations_by_request").on(
                    exec(SbbolCase.UC43_GET_v3_sbbol_compliance_requests_fin_operation_operations_by_request));

    public static ChainBuilder UC44_POST_v4_sbbol_compliance_requests_detail =
            group("UC44_POST_v4_sbbol_compliance_requests_detail").on(
                    exec(SbbolCase.UC44_POST_v4_sbbol_compliance_requests_detail));

    public static ChainBuilder UC45_GET_v5_sbbol_compliance_requests_request_list =
            group("UC45_GET_v5_sbbol_compliance_requests_request_list").on(
                    exec(SbbolCase.UC45_GET_v5_sbbol_compliance_requests_request_list));

    public static ChainBuilder UC46_GET_v7_sbbol_compliance_requests_request_list =
            group("UC46_GET_v7_sbbol_compliance_requests_request_list").on(
                    exec(SbbolCase.UC46_GET_v7_sbbol_compliance_requests_request_list));

    public static ScenarioBuilder scn = scenario("Sbbol")
            .feed(defaultFeeder)
            .feed(complianceRequests)
            .feed(complianceRequests)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(2, UC01_GET_v1_sbbol_compliance_requests_callback_cancel),
                            new Choice.WithWeight(2, UC02_POST_v1_sbbol_compliance_requests_callback_save),
                            new Choice.WithWeight(2, UC03_POST_v1_sbbol_compliance_requests_callback_update),
                            new Choice.WithWeight(2, UC04_GET_v1_sbbol_compliance_requests_mop_one),
                            new Choice.WithWeight(2, UC05_GET_v1_sbbol_compliance_requests_rehabilitation),
                            new Choice.WithWeight(2, UC06_GET_v1_sbbol_compliance_requests_start_rehabilitation_By_requestId),
                            new Choice.WithWeight(2, UC07_GET_v1_sbbol_free_format_letters_detail),
                            new Choice.WithWeight(2, UC08_POST_v2_sbbol_compliance_requests_attachments),
                            new Choice.WithWeight(2, UC09_POST_v2_sbbol_compliance_requests_attachments_batch),
                            new Choice.WithWeight(2, UC10_POST_v2_sbbol_compliance_requests_attachments_with_transfer),
                            new Choice.WithWeight(2, UC11_DELETE_v2_sbbol_compliance_requests_attachments_By_id),
                            new Choice.WithWeight(2, UC12_POST_v2_sbbol_compliance_requests_callback_detail),
                            new Choice.WithWeight(2, UC13_POST_v2_sbbol_compliance_requests_callback_info),
                            new Choice.WithWeight(2, UC14_GET_v2_sbbol_compliance_requests_compliance_profile),
                            new Choice.WithWeight(2, UC15_DELETE_v2_sbbol_compliance_requests_compliance_profile_alert),
                            new Choice.WithWeight(2, UC16_GET_v2_sbbol_compliance_requests_compliance_profile_contacts),
                            new Choice.WithWeight(2, UC17_POST_v2_sbbol_compliance_requests_compliance_profile_contacts),
                            new Choice.WithWeight(2, UC18_GET_v2_sbbol_compliance_requests_compliance_profile_counterparties),
                            new Choice.WithWeight(2, UC19_POST_v2_sbbol_compliance_requests_compliance_profile_counterparties),
                            new Choice.WithWeight(2, UC20_GET_v2_sbbol_compliance_requests_compliance_profile_detail),
                            new Choice.WithWeight(2, UC21_GET_v2_sbbol_compliance_requests_compliance_profile_dopInfo),
                            new Choice.WithWeight(2, UC22_POST_v2_sbbol_compliance_requests_compliance_profile_dopInfo),
                            new Choice.WithWeight(2, UC23_GET_v2_sbbol_compliance_requests_compliance_profile_mtb),
                            new Choice.WithWeight(2, UC24_POST_v2_sbbol_compliance_requests_compliance_profile_mtb),
                            new Choice.WithWeight(2, UC25_GET_v2_sbbol_compliance_requests_compliance_profile_organization),
                            new Choice.WithWeight(2, UC26_POST_v2_sbbol_compliance_requests_compliance_profile_organization),
                            new Choice.WithWeight(2, UC27_GET_v2_sbbol_compliance_requests_organization_by_ucp_id_By_ucpId),
                            new Choice.WithWeight(2, UC28_GET_v2_sbbol_compliance_requests_proactive),
                            new Choice.WithWeight(2, UC29_GET_v2_sbbol_compliance_requests_proactive_by_ucp),
                            new Choice.WithWeight(2, UC30_GET_v2_sbbol_compliance_requests_proactive_mop2),
                            new Choice.WithWeight(2, UC31_GET_v2_sbbol_compliance_requests_proactive_onboarding),
                            new Choice.WithWeight(2, UC32_GET_v2_sbbol_compliance_requests_request_online_control),
                            new Choice.WithWeight(2, UC33_POST_v2_sbbol_compliance_requests_send_to_bank_By_requestId),
                            new Choice.WithWeight(2, UC34_GET_v2_sbbol_compliance_requests_unread_posts_counter),
                            new Choice.WithWeight(2, UC35_GET_v2_sbbol_free_format_letters_detail_By_freeFormatLetterId),
                            new Choice.WithWeight(2, UC36_GET_v2_sbbol_free_format_letters_list),
                            new Choice.WithWeight(2, UC37_POST_v2_sbbol_free_format_letters_send_to_bank_By_freeFormatLetterId),
                            new Choice.WithWeight(2, UC38_POST_v3_sbbol_compliance_requests_callback_detail),
                            new Choice.WithWeight(2, UC39_POST_v3_sbbol_compliance_requests_detail),
                            new Choice.WithWeight(2, UC40_GET_v3_sbbol_compliance_requests_fin_operation_list),
                            new Choice.WithWeight(2, UC41_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_case),
                            new Choice.WithWeight(2, UC42_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_id),
                            new Choice.WithWeight(2, UC43_GET_v3_sbbol_compliance_requests_fin_operation_operations_by_request),
                            new Choice.WithWeight(2, UC44_POST_v4_sbbol_compliance_requests_detail),
                            new Choice.WithWeight(2, UC45_GET_v5_sbbol_compliance_requests_request_list),
                            new Choice.WithWeight(2, UC46_GET_v7_sbbol_compliance_requests_request_list)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug Sbbol")
            .feed(defaultFeeder)
            .feed(complianceRequests)
            .feed(complianceRequests)
            .feed(rqUidsFeeder)
            .exec(SbbolCase.UC01_GET_v1_sbbol_compliance_requests_callback_cancel)
            .exec(SbbolCase.UC02_POST_v1_sbbol_compliance_requests_callback_save)
            .exec(SbbolCase.UC03_POST_v1_sbbol_compliance_requests_callback_update)
            .exec(SbbolCase.UC04_GET_v1_sbbol_compliance_requests_mop_one)
            .exec(SbbolCase.UC05_GET_v1_sbbol_compliance_requests_rehabilitation)
            .exec(SbbolCase.UC06_GET_v1_sbbol_compliance_requests_start_rehabilitation_By_requestId)
            .exec(SbbolCase.UC07_GET_v1_sbbol_free_format_letters_detail)
            .exec(SbbolCase.UC08_POST_v2_sbbol_compliance_requests_attachments)
            .exec(SbbolCase.UC09_POST_v2_sbbol_compliance_requests_attachments_batch)
            .exec(SbbolCase.UC10_POST_v2_sbbol_compliance_requests_attachments_with_transfer)
            .exec(SbbolCase.UC11_DELETE_v2_sbbol_compliance_requests_attachments_By_id)
            .exec(SbbolCase.UC12_POST_v2_sbbol_compliance_requests_callback_detail)
            .exec(SbbolCase.UC13_POST_v2_sbbol_compliance_requests_callback_info)
            .exec(SbbolCase.UC14_GET_v2_sbbol_compliance_requests_compliance_profile)
            .exec(SbbolCase.UC15_DELETE_v2_sbbol_compliance_requests_compliance_profile_alert)
            .exec(SbbolCase.UC16_GET_v2_sbbol_compliance_requests_compliance_profile_contacts)
            .exec(SbbolCase.UC17_POST_v2_sbbol_compliance_requests_compliance_profile_contacts)
            .exec(SbbolCase.UC18_GET_v2_sbbol_compliance_requests_compliance_profile_counterparties)
            .exec(SbbolCase.UC19_POST_v2_sbbol_compliance_requests_compliance_profile_counterparties)
            .exec(SbbolCase.UC20_GET_v2_sbbol_compliance_requests_compliance_profile_detail)
            .exec(SbbolCase.UC21_GET_v2_sbbol_compliance_requests_compliance_profile_dopInfo)
            .exec(SbbolCase.UC22_POST_v2_sbbol_compliance_requests_compliance_profile_dopInfo)
            .exec(SbbolCase.UC23_GET_v2_sbbol_compliance_requests_compliance_profile_mtb)
            .exec(SbbolCase.UC24_POST_v2_sbbol_compliance_requests_compliance_profile_mtb)
            .exec(SbbolCase.UC25_GET_v2_sbbol_compliance_requests_compliance_profile_organization)
            .exec(SbbolCase.UC26_POST_v2_sbbol_compliance_requests_compliance_profile_organization)
            .exec(SbbolCase.UC27_GET_v2_sbbol_compliance_requests_organization_by_ucp_id_By_ucpId)
            .exec(SbbolCase.UC28_GET_v2_sbbol_compliance_requests_proactive)
            .exec(SbbolCase.UC29_GET_v2_sbbol_compliance_requests_proactive_by_ucp)
            .exec(SbbolCase.UC30_GET_v2_sbbol_compliance_requests_proactive_mop2)
            .exec(SbbolCase.UC31_GET_v2_sbbol_compliance_requests_proactive_onboarding)
            .exec(SbbolCase.UC32_GET_v2_sbbol_compliance_requests_request_online_control)
            .exec(SbbolCase.UC33_POST_v2_sbbol_compliance_requests_send_to_bank_By_requestId)
            .exec(SbbolCase.UC34_GET_v2_sbbol_compliance_requests_unread_posts_counter)
            .exec(SbbolCase.UC35_GET_v2_sbbol_free_format_letters_detail_By_freeFormatLetterId)
            .exec(SbbolCase.UC36_GET_v2_sbbol_free_format_letters_list)
            .exec(SbbolCase.UC37_POST_v2_sbbol_free_format_letters_send_to_bank_By_freeFormatLetterId)
            .exec(SbbolCase.UC38_POST_v3_sbbol_compliance_requests_callback_detail)
            .exec(SbbolCase.UC39_POST_v3_sbbol_compliance_requests_detail)
            .exec(SbbolCase.UC40_GET_v3_sbbol_compliance_requests_fin_operation_list)
            .exec(SbbolCase.UC41_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_case)
            .exec(SbbolCase.UC42_GET_v3_sbbol_compliance_requests_fin_operation_operation_by_id)
            .exec(SbbolCase.UC43_GET_v3_sbbol_compliance_requests_fin_operation_operations_by_request)
            .exec(SbbolCase.UC44_POST_v4_sbbol_compliance_requests_detail)
            .exec(SbbolCase.UC45_GET_v5_sbbol_compliance_requests_request_list)
            .exec(SbbolCase.UC46_GET_v7_sbbol_compliance_requests_request_list);
}
