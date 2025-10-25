package com.lenora.repository.business;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.user.Doctor;
import com.lenora.entity.concretes.user.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    boolean existsByDoctorAndPatient(Doctor doctor, Patient patient);

}
