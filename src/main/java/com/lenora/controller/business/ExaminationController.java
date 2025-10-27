package com.lenora.controller.business;

import com.lenora.payload.request.business.ExaminationRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.business.ExaminationResponse;
import com.lenora.service.business.ExaminationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examinations")
@RequiredArgsConstructor
public class ExaminationController {

    private final ExaminationService examinationService;

    // !!! 1) saveExamination (Yeni muayene oluşturma)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> saveExamination(@Valid @RequestBody ExaminationRequest examinationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examinationService.saveExamination(examinationRequest));
    }

    // !!! 2) getAllExaminationsWithPageable (Bütün muayeneleri Pageable yapıda getir)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<ResponseMessage<Page<ExaminationResponse>>> getAllExaminationsWithPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type
    ){
        return ResponseEntity.ok(examinationService.getAllExaminationsWithPageable(page, size, sort, type));
    }

    // !!! 3) getExaminationById (İstenilen id'li muayeneyi getir)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> getExaminationById(@PathVariable Long id){
        return ResponseEntity.ok(examinationService.getExaminationById(id));
    }

    // !!! 4) updateExaminationById (Muayene güncelleme)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> updateExaminationById(@PathVariable Long id, @Valid @RequestBody ExaminationRequest examinationRequest){
        return ResponseEntity.ok(examinationService.updateExaminationById(id, examinationRequest));
    }

    // !!! 5) deleteExamination (Muayene silme)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> deleteExamination(@PathVariable Long id){
        return ResponseEntity.ok(examinationService.deleteExamination(id));
    }
}
