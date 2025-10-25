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

    // User
    public User getByIdUser(Long id) {
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

    // Doctor
    public Doctor getByIdDoctor(Long id) {
        return doctorRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.DOCTOR_NOT_FOUND, id)));
    }

    // Patient
    public Patient getByIdPatient(Long id) {
        return patientRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND, id)));
    }

    // Examination
    public Examination getByIdExamination(Long id) {
        return examinationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.EXAMINATION_NOT_FOUND, id)));
    }

    // Prescription
    public Prescription getByIdPrescription(Long id) {
        return prescriptionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.PRESCRIPTION_NOT_FOUND, id)));
    }

    // userName ile unique kontrolü — Bu userName zaten var mı?
    public boolean checkUserNameExists(String userName) {
        return userRepository.existsByUserNameIgnoreCase(userName);
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

    public boolean isDuplicateDoctorPatient(Examination existing, Doctor doctor, Patient patient) {
        return examinationRepository.existsByDoctorAndPatient(doctor, patient)
                && !(existing.getDoctor().equals(doctor) && existing.getPatient().equals(patient));
    }

    // Generic pasif hale getirme metodu (soft delete)
    public void deactivateEntity(Object entity) {
        if (entity instanceof User user) {
            user.setActive(false);
            userRepository.save(user);
        } else if (entity instanceof Doctor doctor) {
            doctor.setActive(false);
            doctorRepository.save(doctor);
        } else if (entity instanceof Patient patient) {
            patient.setActive(false);
            patientRepository.save(patient);
        } else if (entity instanceof Examination examination) {
            examination.setActive(false);
            examinationRepository.save(examination);
        } else if (entity instanceof Prescription prescription) {
            prescription.setActive(false);
            prescriptionRepository.save(prescription);
        } else {
            throw new IllegalArgumentException("Unsupported entity type for deactivation: " + entity.getClass());
        }
    }

}
