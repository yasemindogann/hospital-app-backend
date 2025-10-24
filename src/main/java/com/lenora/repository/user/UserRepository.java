package com.lenora.repository.user;

import com.lenora.entity.concretes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserNameIgnoreCase(String userName);

}
