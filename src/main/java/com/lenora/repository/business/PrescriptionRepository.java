package com.lenora.repository.business;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.business.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    // Soft delete destekli sorgular
    Optional<Prescription> findByIdAndActiveTrue(Long id);
    Page<Prescription> findByActiveTrue(Pageable pageable);
    Optional<Prescription> findByExaminationAndActiveTrue(Examination examination);
}
