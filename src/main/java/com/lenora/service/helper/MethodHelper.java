package com.lenora.service.helper;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.business.Prescription;
import com.lenora.entity.concretes.user.Doctor;
import com.lenora.entity.concretes.user.Patient;
import com.lenora.entity.concretes.user.User;
import com.lenora.entity.enums.Role;
import com.lenora.exception.ConflictException;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.request.business.ExaminationRequest;
import com.lenora.repository.business.ExaminationRepository;
import com.lenora.repository.business.PrescriptionRepository;
import com.lenora.repository.user.DoctorRepository;
import com.lenora.repository.user.PatientRepository;
import com.lenora.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ExaminationRepository examinationRepository;
    private final PrescriptionRepository prescriptionRepository;

    // userName ile unique kontrolü — Bu userName zaten var mı?
    public boolean checkUserNameExists(String userName) {
        return userRepository.existsByUserNameIgnoreCase(userName);
    }

    // User ID ile getir — yoksa hata fırlat
    public User getByIdUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

    // Doctor ID ile getir — yoksa hata fırlat
    public Doctor getByIdDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.DOCTOR_NOT_FOUND, id)));
    }

    // User rolü DOCTOR değilse hata fırlat
    public void validateUserIsDoctor(User user) {
        if (user.getRole() != Role.DOCTOR) {
            throw new ConflictException(String.format(ErrorMessages.USER_ROLE_NOT_DOCTOR, user.getId()));
        }
    }

    // Doktor kaydı oluştururken user uygun mu (rolü doktor ve daha önce kayıtlı değil)
    public void validateDoctorCreation(User user) {
        validateUserIsDoctor(user);
        if (doctorRepository.existsByUserId(user.getId())) {
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_REGISTERED_AS_DOCTOR, user.getId()));
        }
    }

    // Patient ID ile getir — yoksa hata fırlat
    public Patient getByIdPatient(Long id){
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND, id)));
    }

    // Examination ID ile getir — yoksa hata fırlat
    public Examination getByIdExamination(Long id){
        return examinationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.EXAMINATION_NOT_FOUND, id)));
    }

    public boolean isDuplicateDoctorPatient(Examination existing, Doctor doctor, Patient patient) {
        return examinationRepository.existsByDoctorAndPatient(doctor, patient)
                && !(existing.getDoctor().equals(doctor) && existing.getPatient().equals(patient));
    }

    //// Prescription ID ile getir — yoksa hata fırlat
    public Prescription getByIdPrescription(Long id){
        return prescriptionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.PRESCRIPION_NOT_FOUND, id)));
    }

}
