package com.lenora;

import com.lenora.entity.concretes.user.User;
import com.lenora.entity.enums.Role;
import com.lenora.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class HospitalAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// --- Admin oluştur ---
			if (userRepository.findByUserName("Admin").isEmpty()) {
				User admin = new User();
				admin.setUserName("Admin");
				admin.setPassword(passwordEncoder.encode("Admin123!"));
				admin.setEmail("admin@example.com");
				admin.setRole(Role.ADMIN);
				admin.setActive(true);
				admin.setCreatedDateTime(LocalDateTime.now());
				userRepository.save(admin);
			}

			// --- Doctor oluştur ---
			if (userRepository.findByUserName("Doctor").isEmpty()) {
				User doctor = new User();
				doctor.setUserName("Doctor");
				doctor.setPassword(passwordEncoder.encode("Doctor123!"));
				doctor.setEmail("doctor@example.com");
				doctor.setRole(Role.DOCTOR);
				doctor.setActive(true);
				doctor.setCreatedDateTime(LocalDateTime.now());
				userRepository.save(doctor);
			}

			// --- Secretary oluştur ---
			if (userRepository.findByUserName("Secretary").isEmpty()) {
				User secretary = new User();
				secretary.setUserName("Secretary");
				secretary.setPassword(passwordEncoder.encode("Secretary123!"));
				secretary.setEmail("secretary@example.com");
				secretary.setRole(Role.SECRETARY);
				secretary.setActive(true);
				secretary.setCreatedDateTime(LocalDateTime.now());
				userRepository.save(secretary);
			}
		};
	}

}
