package scenarios.ZK;

import cases.ZK.DigitalUserComplianceContactsCase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import static feeders.ZK.Methods.rqUidsFeeder;
import static feeders.ZK.ZKFeeder.defaultFeeder;
import static feeders.ZK.ZKFeeder.digitalUserComplianceContacts;
import static feeders.ZK.ZKFeeder.digitalUserComplianceContacts;
import static io.gatling.javaapi.core.CoreDsl.*;

public class DigitalUserComplianceContactsScenario {

    public static ChainBuilder UC01_GET_v2_digital_user_compliance_contacts =
            group("UC01_GET_v2_digital_user_compliance_contacts").on(
                    exec(DigitalUserComplianceContactsCase.UC01_GET_v2_digital_user_compliance_contacts));

    public static ChainBuilder UC02_POST_v2_digital_user_compliance_contacts =
            group("UC02_POST_v2_digital_user_compliance_contacts").on(
                    exec(DigitalUserComplianceContactsCase.UC02_POST_v2_digital_user_compliance_contacts));

    public static ChainBuilder UC03_PATCH_v2_digital_user_compliance_contacts_batch_identical =
            group("UC03_PATCH_v2_digital_user_compliance_contacts_batch_identical").on(
                    exec(DigitalUserComplianceContactsCase.UC03_PATCH_v2_digital_user_compliance_contacts_batch_identical));

    public static ChainBuilder UC04_DELETE_v2_digital_user_compliance_contacts_By_id =
            group("UC04_DELETE_v2_digital_user_compliance_contacts_By_id").on(
                    exec(DigitalUserComplianceContactsCase.UC04_DELETE_v2_digital_user_compliance_contacts_By_id));

    public static ChainBuilder UC05_GET_v2_digital_user_compliance_contacts_By_id =
            group("UC05_GET_v2_digital_user_compliance_contacts_By_id").on(
                    exec(DigitalUserComplianceContactsCase.UC05_GET_v2_digital_user_compliance_contacts_By_id));

    public static ChainBuilder UC06_PATCH_v2_digital_user_compliance_contacts_By_id =
            group("UC06_PATCH_v2_digital_user_compliance_contacts_By_id").on(
                    exec(DigitalUserComplianceContactsCase.UC06_PATCH_v2_digital_user_compliance_contacts_By_id));

    public static ChainBuilder UC07_PUT_v2_digital_user_compliance_contacts_By_id =
            group("UC07_PUT_v2_digital_user_compliance_contacts_By_id").on(
                    exec(DigitalUserComplianceContactsCase.UC07_PUT_v2_digital_user_compliance_contacts_By_id));

    public static ScenarioBuilder scn = scenario("DigitalUserComplianceContacts")
            .feed(defaultFeeder)
            .feed(digitalUserComplianceContacts)
            .feed(digitalUserComplianceContacts)
            .feed(rqUidsFeeder)
            .forever().on(
                    randomSwitch().on(
                            new Choice.WithWeight(14, UC01_GET_v2_digital_user_compliance_contacts),
                            new Choice.WithWeight(14, UC02_POST_v2_digital_user_compliance_contacts),
                            new Choice.WithWeight(14, UC03_PATCH_v2_digital_user_compliance_contacts_batch_identical),
                            new Choice.WithWeight(14, UC04_DELETE_v2_digital_user_compliance_contacts_By_id),
                            new Choice.WithWeight(14, UC05_GET_v2_digital_user_compliance_contacts_By_id),
                            new Choice.WithWeight(14, UC06_PATCH_v2_digital_user_compliance_contacts_By_id),
                            new Choice.WithWeight(14, UC07_PUT_v2_digital_user_compliance_contacts_By_id)
                    )
            );

    public static ScenarioBuilder Debug = scenario("Debug DigitalUserComplianceContacts")
            .feed(defaultFeeder)
            .feed(digitalUserComplianceContacts)
            .feed(digitalUserComplianceContacts)
            .feed(rqUidsFeeder)
            .exec(DigitalUserComplianceContactsCase.UC01_GET_v2_digital_user_compliance_contacts)
            .exec(DigitalUserComplianceContactsCase.UC02_POST_v2_digital_user_compliance_contacts)
            .exec(DigitalUserComplianceContactsCase.UC03_PATCH_v2_digital_user_compliance_contacts_batch_identical)
            .exec(DigitalUserComplianceContactsCase.UC04_DELETE_v2_digital_user_compliance_contacts_By_id)
            .exec(DigitalUserComplianceContactsCase.UC05_GET_v2_digital_user_compliance_contacts_By_id)
            .exec(DigitalUserComplianceContactsCase.UC06_PATCH_v2_digital_user_compliance_contacts_By_id)
            .exec(DigitalUserComplianceContactsCase.UC07_PUT_v2_digital_user_compliance_contacts_By_id);
}
