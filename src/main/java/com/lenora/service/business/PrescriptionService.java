package com.lenora.service.business;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.business.Prescription;
import com.lenora.exception.ConflictException;
import com.lenora.payload.mapper.business.PrescriptionMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.business.PrescriptionRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.business.PrescriptionResponse;
import com.lenora.repository.business.PrescriptionRepository;
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
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MethodHelper methodHelper;
    private final PrescriptionMapper prescriptionMapper;
    private final PageableHelper pageableHelper;

    // !!! 1) savePrescription (Yeni reçete oluşturma)
    @Transactional
    public ResponseMessage<PrescriptionResponse> savePrescription(PrescriptionRequest prescriptionRequest) {

        //id'sine kaydetmek istediği muayeneyi varsa getir yoksa hata mesajı
        Examination examination = methodHelper.getByIdExamination(prescriptionRequest.getExaminationId());

        Prescription prescription = prescriptionMapper.prescriptionRequestToPrescription(prescriptionRequest, examination);

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_CREATED_SUCCESSFULY)
                .httpStatus(HttpStatus.CREATED)
                .object(prescriptionMapper.prescriptionToPrescriptionResponse(savedPrescription))
                .build();
    }

    // !!! 2) getAllPrescriptionWithPageable (Sadece aktif Prescriptionları Pageable yapıda getir)
    @Transactional(readOnly = true)
    public ResponseMessage<Page<PrescriptionResponse>> getAllPrescriptionWithPageable(int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        // Sadece aktif prescription kayıtlarını getir
        Page<Prescription> prescriptionPage = prescriptionRepository.findByActiveTrue(pageable);

        Page<PrescriptionResponse> prescriptionResponsePage = prescriptionPage.map(prescriptionMapper::prescriptionToPrescriptionResponse);

        return ResponseMessage.<Page<PrescriptionResponse>>builder()
                .message(SuccessMessages.PRESCRIPTION_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(prescriptionResponsePage)
                .build();
    }

    // !!! 3) getPrescriptionById (İstenilen id'li Prescriptionı getir)
    public ResponseMessage<PrescriptionResponse> getPrescriptionById(Long id) {
        Prescription prescription = methodHelper.getByIdPrescription(id);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_FOUNDED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(prescriptionMapper.prescriptionToPrescriptionResponse(prescription))
                .build();
    }

    // !!! 4) updatePrescriptionById (Reçete güncelleme)
    @Transactional
    public ResponseMessage<PrescriptionResponse> updatePrescriptionById(Long id, PrescriptionRequest prescriptionRequest) {

        Prescription prescription = methodHelper.getByIdPrescription(id);
        Examination examination = methodHelper.getByIdExamination(prescriptionRequest.getExaminationId());

        prescriptionMapper.updateEntityFromRequest(prescription, prescriptionRequest,examination);

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_UPDATED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(prescriptionMapper.prescriptionToPrescriptionResponse(savedPrescription))
                .build();
    }

    @Transactional
    public ResponseMessage<PrescriptionResponse> deletePrescription(Long id) {

        Prescription prescription = methodHelper.getByIdPrescription(id);
        methodHelper.deactivateEntity(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_DELETED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .object(prescriptionMapper.prescriptionToPrescriptionResponse(prescription))
                .build();
    }


/*
    // !!! 5) deletePrescription (Reçete silme)
    @Transactional
    public ResponseMessage<PrescriptionResponse> deletePrescription(Long id) {

        Prescription prescription = methodHelper.getByIdPrescription(id);
        prescriptionRepository.delete(prescription);

        return ResponseMessage.<PrescriptionResponse>builder()
                .message(SuccessMessages.PRESCRIPTION_DELETED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }
 */
}
