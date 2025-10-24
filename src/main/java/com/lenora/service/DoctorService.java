package com.lenora.service;

import com.lenora.entity.concretes.Doctor;
import com.lenora.entity.concretes.User;
import com.lenora.exception.ConflictException;
import com.lenora.payload.mapper.DoctorMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.repository.DoctorRepository;
import com.lenora.service.helper.HelperMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserService userService;
    private final HelperMethods helperMethods;

    // !!! 1) saveDoctor (Yeni doktor oluşturma)
    @Transactional
    public ResponseMessage<DoctorResponse> saveDoctor(DoctorRequest doctorRequest) {

        // İlgili user var mı?
        User user = helperMethods.getByIdUser(doctorRequest.getUserId());

        // Kullanıcının rolü DOCTOR mu ve zaten doktor olarak kayıtlı mı?
        helperMethods.validateDoctorCreation(user);

        // Yeni doktor nesnesini oluştur
        Doctor doctor = doctorMapper.doctorRequestToDoctor(doctorRequest, user);

        // DB'ye kaydet
        Doctor savedDoctor = doctorRepository.save(doctor);

        // Response oluştur
        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_CREATED_SUCCESSFULY)
                .httpStatus(HttpStatus.CREATED)
                .object(doctorMapper.doctorToDoctorResponse(savedDoctor))
                .build();
    }

    // !!! 2) getAllDoctorsWithList (Bütün doktorları getir)
    public ResponseMessage<List<DoctorResponse>> getAllDoctorsWithList() {

        List<Doctor> doctors = doctorRepository.findAll();

        List<DoctorResponse> doctorList = doctors.stream()
                .map(doctorMapper::doctorToDoctorResponse)
                .toList();

        return ResponseMessage.<List<DoctorResponse>>builder()
                .message(SuccessMessages.DOCTOR_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(doctorList)
                .build();
    }

    // !!! 3) getDoctorById (İstenilen id'li doktor'u getir)
    public ResponseMessage<DoctorResponse> getDoctorById(Long id) {
        Doctor doctor = helperMethods.getByIdDoctor(id);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_FOUNDED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(doctorMapper.doctorToDoctorResponse(doctor))
                .build();
    }

    // !!! 4) updateDoctorById (Doktor güncelleme)
    @Transactional
    public ResponseMessage<DoctorResponse> updateDoctorById(Long id, DoctorRequest doctorRequest) {

        // Güncellenecek doktor var mı?
        Doctor doctor = helperMethods.getByIdDoctor(id);

        // Yeni user bilgisi (update edilecek user değişmiş olabilir)
        User user = helperMethods.getByIdUser(doctorRequest.getUserId());

        // Kullanıcı DOCTOR rolünde mi?
        helperMethods.validateUserIsDoctor(user);

        // Aynı user başka bir doktora ait mi?
        if (doctorRepository.existsByUserId(user.getId()) &&
                !doctor.getUser().getId().equals(user.getId())) {
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_REGISTERED_AS_DOCTOR, user.getId()));
        }

        // Doktor nesnesini güncelle
        helperMethods.updateDoctorFromRequest(doctorRequest, doctor, user);

        // DB'ye kaydet
        Doctor updatedDoctor = doctorRepository.save(doctor);

        // Response döndür
        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_UPDATED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(doctorMapper.doctorToDoctorResponse(updatedDoctor))
                .build();
    }

    // !!! 4) deleteDoctorById
    @Transactional
    public ResponseMessage<DoctorResponse> deleteDoctorById(Long id){

        helperMethods.getByIdDoctor(id);
        doctorRepository.deleteById(id);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_DELETED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }


}

