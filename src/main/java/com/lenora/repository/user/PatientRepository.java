package com.lenora.repository.user;

import com.lenora.entity.concretes.user.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Soft delete destekli sorgular
    Optional<Patient> findByIdAndActiveTrue(Long id);
    Page<Patient> findByActiveTrue(Pageable pageable);
}
