package com.lenora.repository.user;

import com.lenora.entity.concretes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserNameIgnoreCase(String userName);

    // Soft delete destekli sorgular
    Optional<User> findByIdAndActiveTrue(Long id);
    List<User> findAllByActiveTrue();
    Optional<User> findByUserName(String userName);
}
