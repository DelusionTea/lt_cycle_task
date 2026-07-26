package scenarios.efsFinmonMob;

import feeders.CommonMethods;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static cases.efsFinmonMob.FinmonMobCases.*;
import static feeders.efsSberbusiness.EfsSberbussinesFeeder.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static scenarios.efsSberbusinessAuth.LoginMob.loginMob;

public class FinmonMobScenario {

    static ChainBuilder groupUc15 =
            exec(UC15_GET_v2_request_state);
    static ChainBuilder groupMedium =
            randomSwitch().on(
                    new Choice.WithWeight(14.617864, exec(UC05_POST_rejections_list)),
                    new Choice.WithWeight(21.869245, exec(UC09_GET_v1_support)),
                    new Choice.WithWeight(17.265193, exec(UC33_POST_informing_list_v2)),
                    new Choice.WithWeight(24.378453, exec(UC16_POST_v2_request_list)),
                    new Choice.WithWeight(21.869245, exec(UC20_GET_compliance_profile_widget))

            );
    static ChainBuilder groupTail =
            randomSwitch().on(
// 26 хвостовых UC, у всех одинаково внутри группы
                    new Choice.WithWeight(7.692307, exec(UC03_GET_request_online_pprbId)),
                    new Choice.WithWeight(7.692307, exec(UC04_GET_rejections_pprbId)),
                    new Choice.WithWeight(7.692307, exec(UC11_GET_ffl_id)),
                    new Choice.WithWeight(7.692307, exec(UC18_GET_v1_rejections_startRehabilitation_uuid)),
                    new Choice.WithWeight(7.692307, exec(UC19_GET_v1_request_rehabilitation)),
                    new Choice.WithWeight(7.692307, exec(UC22_GET_compliance_profile_schemeBusiness_partners)),
                    new Choice.WithWeight(7.692307, exec(UC24_DELETE_compliance_profile_schemeBusiness_detail_alert)),
                    new Choice.WithWeight(7.692307, exec(UC27_POST_compliance_profile_schemeBusiness_save_mtb)),
                    new Choice.WithWeight(7.692307, exec(UC28_POST_compliance_profile_schemeBusiness_save_other)),
                    new Choice.WithWeight(7.692307, exec(UC29_POST_compliance_profile_schemeBusiness_save_aboutCompany)),
                    new Choice.WithWeight(7.692307, exec(UC30_POST_compliance_profile_schemeBusiness_save_contacts)),
                    new Choice.WithWeight(7.692307, exec(UC34_POST_request_UUID_reply)),
                    new Choice.WithWeight(7.692307, exec(UC35_POST_request_UUID_saveAttachments))
            );
    static ChainBuilder groupLoli=
            randomSwitch().on(
                    new Choice.WithWeight(12.87500, exec(UC01_POST_UPLOAD_V1)),
                    new Choice.WithWeight(11.87500, exec(UC06_GET_REQUEST_V2)),
                    new Choice.WithWeight(6.81250, exec(UC08_GET_files_params)),
                    new Choice.WithWeight(2.12500, exec(UC10_GET_print)),
                    new Choice.WithWeight(13.81250, exec(UC12_GET_download_ecmId)),
                    new Choice.WithWeight(5.68750, exec(UC13_GET_informing_id)),
                    new Choice.WithWeight(8.37500, exec(UC17_GET_v2_rejections_uuid)),
                    new Choice.WithWeight(10.93750, exec(UC21_GET_compliance_profile_schemeBusiness)),
                    new Choice.WithWeight(1.18750, exec(UC23_GET_compliance_profile_schemeBusiness_mtb)),
                    new Choice.WithWeight(4.56250, exec(UC25_POST_compliance_profile_schemeBusiness_detail)),
                    new Choice.WithWeight(1.25000, exec(UC26_POST_compliance_profile_schemeBusiness_save_partners)),
                    new Choice.WithWeight(18.12500, exec(UC31_GET_proactive_mop2_pprbId)),
                    new Choice.WithWeight(2.37500, exec(UC32_GET_proactive_onboarding_pprbId))
            );


    public static ScenarioBuilder scn = scenario("scn")

            .feed(finmonMobAuthFeeder)
            .feed(finmonMobPPRBFeeder)
            .exec(session -> {
                String newUuid = CommonMethods.generateUUID();
                return session.set("uuid", newUuid);
            })
            .exec(session -> {
                String currentTime = CommonMethods.generateCurrentTimestamp();
                return session.set("currentTimestamp", currentTime);
            })
            .exec(session -> {
                String generateRandomIP = CommonMethods.generateRandomIP();
                return session.set("randIP", generateRandomIP);
            })
            .exec(loginMob)
            .exitBlockOnFail(forever().on(
            randomSwitch().on(
                            new Choice.WithWeight(93.302730, groupUc15),
                            new Choice.WithWeight(2.999994, groupMedium),
                            new Choice.WithWeight(2.702047, groupTail),
                            new Choice.WithWeight(0.995228, groupLoli)
            )));

//            .randomSwitch().on(
//                    new Choice.WithWeight(0.21715460, exec(UC01_POST_UPLOAD_V1)),
//                    new Choice.WithWeight(0.86861840, exec(UC02_POST_request_list)),
//                    new Choice.WithWeight(0.21715460, exec(UC03_GET_request_online_pprbId)),
//                    new Choice.WithWeight(0.21715460, exec(UC04_GET_rejections_pprbId)),
//                    new Choice.WithWeight(0.65146380, exec(UC05_POST_rejections_list)),
//                    new Choice.WithWeight(0.21715460, exec(UC06_GET_REQUEST_V2)),
//                    new Choice.WithWeight(3.72967120, exec(UC07_GET_request_state)),
//                    new Choice.WithWeight(0.21715460, exec(UC08_GET_files_params)),
//                    new Choice.WithWeight(0.65146380, exec(UC09_GET_v1_support)),
//                    new Choice.WithWeight(0.21715460, exec(UC10_GET_print)),
//                    new Choice.WithWeight(0.21715460, exec(UC11_GET_ffl_id)),
//                    new Choice.WithWeight(0.21715460, exec(UC12_GET_download_ecmId)),
//                    new Choice.WithWeight(0.21715460, exec(UC13_GET_informing_id)),
//                    new Choice.WithWeight(0.65146380, exec(UC14_POST_informing_list)),
//                    new Choice.WithWeight(87.80129940, exec(UC15_GET_v2_request_state)),
//                    new Choice.WithWeight(0.21715460, exec(UC16_POST_v2_request_list)),
//                    new Choice.WithWeight(0.21715460, exec(UC17_GET_v2_rejections_uuid)),
//                    new Choice.WithWeight(0.21715460, exec(UC18_GET_v1_rejections_startRehabilitation_uuid)),
//                    new Choice.WithWeight(0.21715460, exec(UC19_GET_v1_request_rehabilitation)),
//                    new Choice.WithWeight(0.21715460, exec(UC20_GET_compliance_profile_widget)),
//                    new Choice.WithWeight(0.21715460, exec(UC21_GET_compliance_profile_schemeBusiness)),
//                    new Choice.WithWeight(0.21715460, exec(UC22_GET_compliance_profile_schemeBusiness_partners)),
//                    new Choice.WithWeight(0.21715460, exec(UC23_GET_compliance_profile_schemeBusiness_mtb)),
//                    new Choice.WithWeight(0.21715460, exec(UC24_DELETE_compliance_profile_schemeBusiness_detail_alert)),
//                    new Choice.WithWeight(0.21715460, exec(UC25_POST_compliance_profile_schemeBusiness_detail)),
//                    new Choice.WithWeight(0.21715460, exec(UC26_POST_compliance_profile_schemeBusiness_save_partners)),
//                    new Choice.WithWeight(0.21715460, exec(UC27_POST_compliance_profile_schemeBusiness_save_mtb)),
//                    new Choice.WithWeight(0.21715460, exec(UC28_POST_compliance_profile_schemeBusiness_save_other)),
//                    new Choice.WithWeight(0.21715460, exec(UC29_POST_compliance_profile_schemeBusiness_save_aboutCompany)),
//                    new Choice.WithWeight(0.21715460, exec(UC30_POST_compliance_profile_schemeBusiness_save_contacts)),
//                    new Choice.WithWeight(0.21715460, exec(UC31_GET_proactive_mop2_pprbId)),
//                    new Choice.WithWeight(0.21715460, exec(UC32_GET_proactive_onboarding_pprbId))
//                            );


    public static ScenarioBuilder debug = scenario("finmonMobDebug")
            .feed(finmonMobAuthFeeder)
            .feed(finmonMobPPRBFeeder)
            .exec(session -> {
                String newUuid = CommonMethods.generateUUID();
                return session.set("uuid", newUuid);
            })
            .exec(session -> {
                String currentTime = CommonMethods.generateCurrentTimestamp();
                return session.set("currentTimestamp", currentTime);
            })
            .exec(session -> {
                String generateRandomIP = CommonMethods.generateRandomIP();
                return session.set("randIP", generateRandomIP);
            })
            .exec(loginMob)
            .exec(UC01_POST_UPLOAD_V1);
//            .exec(UC03_GET_request_online_pprbId)
//            .exec(UC04_GET_rejections_pprbId)
//            .exec(UC05_POST_rejections_list)
//            .exec(UC06_GET_REQUEST_V2)
//            .exec(UC08_GET_files_params)
//            .exec(UC09_GET_v1_support)
//            .exec(UC10_GET_print)
//            .exec(UC11_GET_ffl_id)
//            .exec(UC12_GET_download_ecmId)
//            .exec(UC13_GET_informing_id)
//            .exec(UC15_GET_v2_request_state)
//            .exec(UC16_POST_v2_request_list)
//            .exec(UC17_GET_v2_rejections_uuid)
//            .exec(UC18_GET_v1_rejections_startRehabilitation_uuid)
//            .exec(UC19_GET_v1_request_rehabilitation)
//            .exec(UC20_GET_compliance_profile_widget)
//            .exec(UC21_GET_compliance_profile_schemeBusiness)
//            .exec(UC22_GET_compliance_profile_schemeBusiness_partners)
//            .exec(UC23_GET_compliance_profile_schemeBusiness_mtb)
//            .exec(UC24_DELETE_compliance_profile_schemeBusiness_detail_alert)
//            .exec(UC25_POST_compliance_profile_schemeBusiness_detail)
//            .exec(UC26_POST_compliance_profile_schemeBusiness_save_partners)
//            .exec(UC27_POST_compliance_profile_schemeBusiness_save_mtb)
//            .exec(UC28_POST_compliance_profile_schemeBusiness_save_other)
//            .exec(UC29_POST_compliance_profile_schemeBusiness_save_aboutCompany)
//            .exec(UC30_POST_compliance_profile_schemeBusiness_save_contacts)
//            .exec(UC31_GET_proactive_mop2_pprbId)
//            .exec(UC33_POST_informing_list_v2)
//            .exec(UC34_POST_request_UUID_reply)
//            .exec(UC35_POST_request_UUID_saveAttachments);


}
