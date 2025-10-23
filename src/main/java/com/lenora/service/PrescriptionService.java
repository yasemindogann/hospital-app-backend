package com.lenora.service;

import com.lenora.entity.concretes.Examination;
import com.lenora.entity.concretes.Prescription;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.mapper.PrescriptionMapper;
import com.lenora.payload.request.PrescriptionRequest;
import com.lenora.payload.response.PrescriptionResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.repository.ExaminationRepository;
import com.lenora.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ExaminationRepository examinationRepository;

    public ResponseMessage<PrescriptionResponse> savePrescription(PrescriptionRequest request) {
        Examination examination = examinationRepository.findById(request.getExaminationId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EXAMINATION_NOT_FOUND));

        Prescription prescription = PrescriptionMapper.mapRequestToPrescription(request, examination);
        prescriptionRepository.save(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .object(PrescriptionMapper.mapPrescriptionToResponse(prescription))
                .build();
    }

    public ResponseMessage<PrescriptionResponse> updatePrescription(Long id, PrescriptionRequest request) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRESCRIPTION_NOT_FOUND));

        Examination examination = examinationRepository.findById(request.getExaminationId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EXAMINATION_NOT_FOUND));

        prescription.setExamination(examination);
        prescription.setMedicineName(request.getMedicineName());
        prescription.setDosage(request.getDosage());
        prescription.setInstructions(request.getInstructions());

        prescriptionRepository.save(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(PrescriptionMapper.mapPrescriptionToResponse(prescription))
                .build();
    }

    public ResponseMessage<PrescriptionResponse> getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRESCRIPTION_NOT_FOUND));

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(PrescriptionMapper.mapPrescriptionToResponse(prescription))
                .build();
    }

    public ResponseMessage<List<PrescriptionResponse>> getAllPrescriptions() {
        List<PrescriptionResponse> list = prescriptionRepository.findAll()
                .stream()
                .map(PrescriptionMapper::mapPrescriptionToResponse)
                .collect(Collectors.toList());

        return ResponseMessage.<List<PrescriptionResponse>>builder()
                .message(SuccessMessages.PRESCRIPTION_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(list)
                .build();
    }

    public ResponseMessage<PrescriptionResponse> deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRESCRIPTION_NOT_FOUND));

        prescriptionRepository.delete(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_DELETED)
                .httpStatus(HttpStatus.OK)
                .object(PrescriptionMapper.mapPrescriptionToResponse(prescription))
                .build();
    }
}
