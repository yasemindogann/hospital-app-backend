package com.lenora.repository.business;

import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.business.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    void deleteAllByExamination(Examination examination);

}
