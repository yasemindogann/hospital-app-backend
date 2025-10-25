package com.lenora.payload.mapper.business;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.business.Prescription;
import com.lenora.payload.request.business.PrescriptionRequest;
import com.lenora.payload.response.business.PrescriptionResponse;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    // 🔹 Request -> Entity
    public Prescription prescriptionRequestToPrescription(PrescriptionRequest prescriptionRequest, Examination examination){
        return Prescription.builder()
                .examination(examination)
                .medicineName(prescriptionRequest.getMedicineName())
                .dosage(prescriptionRequest.getDosage())
                .description(prescriptionRequest.getDescription())
                .active(true)
                .build();
    }

    // 🔹 Entity -> Response
    public PrescriptionResponse prescriptionToPrescriptionResponse (Prescription prescription){
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .examinationId(prescription.getExamination().getId())
                .doctorName(prescription.getExamination().getDoctor().getFullName())
                .patientName(prescription.getExamination().getPatient().getFullName())
                .medicineName(prescription.getMedicineName())
                .dosage(prescription.getDosage())
                .description(prescription.getDescription())
                .build();
    }

    // (updatePrescriptionById için)
    public void updateEntityFromRequest(Prescription prescription, PrescriptionRequest prescriptionRequest, Examination examination){
        prescription.setExamination(examination);
        prescription.setMedicineName(prescriptionRequest.getMedicineName());
        prescription.setDosage(prescriptionRequest.getDosage());
        prescription.setDescription(prescriptionRequest.getDescription());
    }

}
