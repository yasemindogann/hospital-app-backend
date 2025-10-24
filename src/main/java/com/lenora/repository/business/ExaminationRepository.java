package com.lenora.repository.business;

import com.lenora.entity.concretes.business.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
}
