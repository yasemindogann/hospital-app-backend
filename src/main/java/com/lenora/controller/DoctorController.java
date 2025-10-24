package com.lenora.controller;

import com.lenora.entity.concretes.User;
import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // !!! 1) saveDoctor (Yeni doktor oluşturma)
    @PostMapping
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@Valid @RequestBody DoctorRequest doctorRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.saveDoctor(doctorRequest));
    }

    // !!! 2) getAllDoctorsWithList (Bütün doktorları getir)
    @GetMapping
    public ResponseEntity<ResponseMessage<List<DoctorResponse>>> getAllDoctorsWithList(){
        return ResponseEntity.ok(doctorService.getAllDoctorsWithList());
    }

    // !!! 3) getDoctorById (İstenilen id'li doktoru getir)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // !!! 4) updateDoctorById (Doktor güncelleme)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> updateDoctorById(@PathVariable Long id, @Valid @RequestBody DoctorRequest doctorRequest){
        return ResponseEntity.ok(doctorService.updateDoctorById(id, doctorRequest));
    }

    // !!! 4) deleteDoctorById (Doktor silme)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> deleteDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.deleteDoctorById(id));
    }


}
