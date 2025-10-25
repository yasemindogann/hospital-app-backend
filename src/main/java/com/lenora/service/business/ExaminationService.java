package com.lenora.service.business;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.user.Doctor;
import com.lenora.entity.concretes.user.Patient;
import com.lenora.exception.ConflictException;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.mapper.business.ExaminationMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.business.ExaminationRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.business.ExaminationResponse;
import com.lenora.repository.business.ExaminationRepository;
import com.lenora.repository.user.DoctorRepository;
import com.lenora.repository.user.PatientRepository;
import com.lenora.service.helper.MethodHelper;
import com.lenora.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;

@Service
@RequiredArgsConstructor
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final ExaminationMapper examinationMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;


    // !!! 1) saveExamination (Yeni muayene oluşturma)
    @Transactional
    public ResponseMessage<ExaminationResponse> saveExamination(ExaminationRequest examinationRequest) {

        //Kayıt yaparken seçtiğim doktorId'ye gerçeken DB'de var mı
        Doctor doctor = methodHelper.getByIdDoctor(examinationRequest.getDoctorId());
        //Kayıt yaparken seçtiğim hastaId'ye gerçeken DB'de var mı
        Patient patient = methodHelper.getByIdPatient(examinationRequest.getPatientId());

        // Aynı doktor–hasta çifti varsa hata ver
        //Mesela 1 id'li doktor 3 id'li hasta zaten DB'de varsa
        if (examinationRepository.existsByDoctorAndPatient(doctor, patient)) {
            throw new ConflictException(ErrorMessages.EXAMINATION_ALREADY_EXISTS);
        }

        Examination examination = examinationMapper.examinationRequestToExamination(examinationRequest, doctor, patient);
        Examination savedExamination = examinationRepository.save(examination);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_CREATED_SUCCESSFULY)
                .httpStatus(HttpStatus.CREATED)
                .object(examinationMapper.examinationToExaminationResponse(savedExamination))
                .build();
    }

    // !!! 2) getAllExaminationsWithPageable (Bütün Examinationları Pageable yapıda getir)
    public ResponseMessage<Page<ExaminationResponse>> getAllExaminationsWithPageable(int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        Page<Examination> examinationPage = examinationRepository.findAll(pageable);
        Page<ExaminationResponse> examinationResponsePage = examinationPage.map(examinationMapper::examinationToExaminationResponse);

        return ResponseMessage.<Page<ExaminationResponse>>builder()
                .message(SuccessMessages.EXAMINATION_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(examinationResponsePage)
                .build();
    }

    // !!! 3) getExaminationById (İstenilen id'li Examinationı getir)
    public ResponseMessage<ExaminationResponse> getExaminationById(Long id) {

        Examination examination = methodHelper.getByIdExamination(id);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_FOUNDED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(examinationMapper.examinationToExaminationResponse(examination))
                .build();
    }

    // !!! 4) updateExaminationById (Examination güncelleme)
    @Transactional
    public ResponseMessage<ExaminationResponse> updateExaminationById(Long id, ExaminationRequest examinationRequest) {

        Examination examination = methodHelper.getByIdExamination(id);

        Doctor doctor = methodHelper.getByIdDoctor(examinationRequest.getDoctorId());
        Patient patient = methodHelper.getByIdPatient(examinationRequest.getPatientId());

        // Aynı doktor–hasta ilişkisiyle başka bir muayene varsa engelle
        if (examinationRepository.existsByDoctorAndPatient(doctor, patient)
                && !(examination.getDoctor().equals(doctor) && examination.getPatient().equals(patient))) {
            throw new ConflictException(ErrorMessages.EXAMINATION_ALREADY_EXISTS);
        }

        examinationMapper.updateExaminationFromRequest(examinationRequest, examination, doctor, patient);

        Examination updatedExamination = examinationRepository.save(examination);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_UPDATED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(examinationMapper.examinationToExaminationResponse(updatedExamination))
                .build();
    }

    // !!! 5) deleteExamination (Examination silme)
    @Transactional
    public ResponseMessage<ExaminationResponse> deleteExamination(Long id) {

        Examination examination = methodHelper.getByIdExamination(id);
        examinationRepository.delete(examination);

        return ResponseMessage.<ExaminationResponse>builder()
                .message(SuccessMessages.EXAMINATION_DELETED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }

}
