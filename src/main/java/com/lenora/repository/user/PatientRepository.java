package com.lenora.repository.user;

import com.lenora.entity.concretes.user.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
