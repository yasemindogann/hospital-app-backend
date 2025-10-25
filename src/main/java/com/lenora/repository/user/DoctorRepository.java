package com.lenora.repository.user;

import com.lenora.entity.concretes.user.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUserId(Long id);
    Optional<Doctor> findByUserId(Long id);

    // Soft delete destekli sorgular
    Optional<Doctor> findByIdAndActiveTrue(Long id);
    List<Doctor> findAllByActiveTrue();
    Optional<Doctor> findByUserIdAndActiveTrue(Long userId);
}
