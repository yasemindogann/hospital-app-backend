package com.lenora.repository;

import com.lenora.entity.concretes.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUserId(Long id);
    Optional<Doctor> findByUserId(Long id);

}
