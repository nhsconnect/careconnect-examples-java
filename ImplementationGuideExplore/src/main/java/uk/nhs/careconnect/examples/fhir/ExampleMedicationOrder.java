package uk.nhs.careconnect.examples.fhir;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.valueset.MedicationOrderStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.TimingAbbreviationEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kevinmayfield on 26/05/2017.
 */
public class ExampleMedicationOrder {

    public static MedicationOrder buildCareConnectFHIRMedicationOrder() {

        //http://dmd.medicines.org.uk/DesktopDefault.aspx?VMP=10097211000001102&toc=nofloat

        MedicationOrder prescription = new MedicationOrder();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<IdDt> profiles = new ArrayList<IdDt>();
        profiles.add(new IdDt(CareConnectSystem.ProfileMedicationOrder));
        ResourceMetadataKeyEnum.PROFILES.put(prescription, profiles);

        ExtensionDt supplyType = new ExtensionDt();
        supplyType.setUrl(CareConnectSystem.ExtUrlMedicationSupplyType);
        CodeableConceptDt supplyCode = new CodeableConceptDt();
        supplyCode.addCoding()
                .setCode("394823007")
                .setSystem(CareConnectSystem.SNOMEDCT)
                .setDisplay("NHS Prescription");
        supplyType.setValue(supplyCode);
        prescription.addUndeclaredExtension(supplyType);

        prescription.setPatient(new ResourceReferenceDt("https://pds.proxy.nhs.uk/Patient/9876543210"));
        prescription.getPatient().setDisplay("Bernie Kanfeld");

        Date issueDate;
        try {
            issueDate = dateFormat.parse("2017-05-25");
            prescription.setDateWritten(new DateTimeDt(issueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        prescription.setStatus(MedicationOrderStatusEnum.ACTIVE);

        prescription.setPrescriber(new ResourceReferenceDt("https://sds.proxy.nhs.uk/Practitioner/G8133438"));
        prescription.getPrescriber().setDisplay("Dr AA Bhatia");

        prescription.setNote("Please explain to Bernie how to use injector.");

        CodeableConceptDt drugCode = new CodeableConceptDt();
        drugCode.addCoding()
                .setCode("10097211000001102")
                .setSystem(CareConnectSystem.SNOMEDCT)
                .setDisplay("Insulin glulisine 100units/ml solution for injection 3ml pre-filled disposable devices");

        prescription.setMedication(drugCode);


        MedicationOrder.DosageInstruction dosage = prescription.addDosageInstruction();
        dosage.setText("Three times a day");
        CodeableConceptDt additionalIns = new CodeableConceptDt();
        additionalIns.addCoding()
                .setCode("1521000175104")
                .setSystem(CareConnectSystem.SNOMEDCT)
                .setDisplay("After dinner");
        dosage.setAdditionalInstructions(additionalIns);


        TimingDt timing = new TimingDt();
        timing.setCode(TimingAbbreviationEnum.TID);
        dosage.setTiming(timing);

        MedicationOrder.DispenseRequest dispenseRequest = new MedicationOrder.DispenseRequest();
        dispenseRequest.setNumberOfRepeatsAllowed(3);



        prescription.setDispenseRequest(dispenseRequest);



        return prescription;
    }
}
