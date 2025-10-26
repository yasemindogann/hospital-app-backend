package com.lenora.controller.user;

import com.lenora.payload.request.user.DoctorRequest;
import com.lenora.payload.response.user.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.user.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // !!! 1) saveDoctor (Yeni doktor oluşturma)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@Valid @RequestBody DoctorRequest doctorRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.saveDoctor(doctorRequest));
    }

    // !!! 2) getAllDoctorsWithList (Bütün doktorları getir)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<ResponseMessage<List<DoctorResponse>>> getAllDoctorsWithList(){
        return ResponseEntity.ok(doctorService.getAllDoctorsWithList());
    }

    // !!! 3) getDoctorById (İstenilen id'li doktoru getir)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // !!! 4) updateDoctorById (Doktor güncelleme)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> updateDoctorById(@PathVariable Long id, @Valid @RequestBody DoctorRequest doctorRequest){
        return ResponseEntity.ok(doctorService.updateDoctorById(id, doctorRequest));
    }

    // !!! 5) deleteDoctor (Doktor silme)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> deleteDoctor(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }

}
