package com.lenora.service.user;

import com.lenora.entity.concretes.user.Patient;
import com.lenora.payload.mapper.user.PatientMapper;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.user.PatientRequest;
import com.lenora.payload.response.user.PatientResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.repository.business.ExaminationRepository;
import com.lenora.repository.user.PatientRepository;
import com.lenora.service.helper.MethodHelper;
import com.lenora.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ExaminationRepository examinationRepository;
    private final PatientMapper patientMapper;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;


    // !!! 1) savePatient (Yeni hasta oluşturma)
    @Transactional
    public ResponseMessage<PatientResponse> savePatient(PatientRequest patientRequest) {

        Patient patient = patientMapper.patientRequestToPatient(patientRequest);

        Patient savedPatient = patientRepository.save(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_CREATED_SUCCESSFULY)
                .object(patientMapper.patientToPatientResponse(savedPatient))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }


    // !!! 2) getAllPatientWithPageable (Yeni hasta oluşturma)
    public ResponseMessage<Page<PatientResponse>> getAllPatientWithPageable(int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);

        // Veritabanından sayfalı hasta listesi al
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        // Entity → DTO dönüşümü (stream değil, map fonksiyonu ile)
        Page<PatientResponse> patientResponsePage = patientPage.map(patientMapper::patientToPatientResponse);

        // ResponseMessage döndür
        return ResponseMessage.<Page<PatientResponse>>builder()
                .message(SuccessMessages.PATIENT_LISTED_WITH_PAGEABLE_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(patientResponsePage)
                .build();
    }

    // !!! 3) getPatientByIdWithRequestParam (İstenilen id'li hastayı getir)
    public ResponseMessage<PatientResponse> getPatientByIdWithRequestParam(Long id) {

        Patient patient = methodHelper.getByIdPatient(id);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(patientMapper.patientToPatientResponse(patient))
                .build();
    }

    // !!! 4) updatePatientById (id ile hasta getirme)
    @Transactional
    public ResponseMessage<PatientResponse> updatePatientById(Long id, PatientRequest patientRequest) {
        Patient patient = methodHelper.getByIdPatient(id);
        patientMapper.updatePatientFromRequest(patientRequest, patient);
        Patient updatedPatient = patientRepository.save(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_UPDATED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(patientMapper.patientToPatientResponse(updatedPatient))
                .build();
    }

/*
    // !!! 5) deletePatientById (id ile hasta silme)
    @Transactional
    public ResponseMessage<PatientResponse> deletePatientById(Long id) {

        Patient patient = methodHelper.getByIdPatient(id);
        patientRepository.delete(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_DELETED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }
*/
}
