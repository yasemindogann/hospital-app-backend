package com.lenora.service.business;

import com.lenora.payload.request.business.PrescriptionRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.business.PrescriptionResponse;
import com.lenora.repository.business.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    // !!! 1) saveExamination (Yeni muayene oluşturma)
    public ResponseMessage<PrescriptionResponse> savePrescription(PrescriptionRequest prescriptionRequest) {
        return null;
    }

    // !!! 2) getAllPrescriptionWithPageable (Bütün EPrescriptionı Pageable yapıda getir)
    public ResponseMessage<Page<PrescriptionResponse>> getAllPrescriptionWithPageable(int page, int size, String sort, String type) {
        return  null;
    }

    // !!! 3) getPrescriptionById (İstenilen id'li Prescriptionı getir)
    public ResponseMessage<PrescriptionResponse> getPrescriptionById(Long id) {
        return null;
    }

    // !!! 4) updatePrescriptionById (Reçete güncelleme)
    public ResponseMessage<PrescriptionResponse> updatePrescriptionById(Long id, PrescriptionRequest prescriptionRequest) {
        return null;
    }

    // !!! 5) deletePrescription (Reçete silme)
    public ResponseMessage<PrescriptionResponse> deletePrescription(Long id) {
        return null;
    }
}
