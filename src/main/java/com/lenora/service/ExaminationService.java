package com.lenora.service;

import com.lenora.entity.concretes.Doctor;
import com.lenora.entity.concretes.Examination;
import com.lenora.entity.concretes.Patient;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.mapper.ExaminationMapper;
import com.lenora.payload.request.ExaminationRequest;
import com.lenora.payload.response.ExaminationResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.repository.DoctorRepository;
import com.lenora.repository.ExaminationRepository;
import com.lenora.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public ResponseMessage<ExaminationResponse> saveExamination(ExaminationRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DOCTOR_NOT_FOUND));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND));

        Examination examination = ExaminationMapper.mapRequestToExamination(request, doctor, patient);
        examinationRepository.save(examination);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .object(ExaminationMapper.mapExaminationToResponse(examination))
                .build();
    }

    public ResponseMessage<ExaminationResponse> updateExamination(Long id, ExaminationRequest request) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EXAMINATION_NOT_FOUND));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DOCTOR_NOT_FOUND));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND));

        examination.setDoctor(doctor);
        examination.setPatient(patient);
        examination.setExaminationDate(request.getExaminationDate());
        examination.setDiagnosis(request.getDiagnosis());

        examinationRepository.save(examination);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(ExaminationMapper.mapExaminationToResponse(examination))
                .build();
    }

    public ResponseMessage<ExaminationResponse> getExaminationById(Long id) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EXAMINATION_NOT_FOUND));

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(ExaminationMapper.mapExaminationToResponse(examination))
                .build();
    }

    public ResponseMessage<List<ExaminationResponse>> getAllExaminations() {
        List<ExaminationResponse> list = examinationRepository.findAll()
                .stream()
                .map(ExaminationMapper::mapExaminationToResponse)
                .collect(Collectors.toList());

        return ResponseMessage.<List<ExaminationResponse>>builder()
                .message(SuccessMessages.EXAMINATION_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(list)
                .build();
    }

    public ResponseMessage<ExaminationResponse> deleteExamination(Long id) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EXAMINATION_NOT_FOUND));

        examinationRepository.delete(examination);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_DELETED)
                .httpStatus(HttpStatus.OK)
                .object(ExaminationMapper.mapExaminationToResponse(examination))
                .build();
    }
}
