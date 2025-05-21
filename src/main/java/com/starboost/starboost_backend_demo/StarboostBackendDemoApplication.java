package com.starboost.starboost_backend_demo;

import com.starboost.starboost_backend_demo.entity.Gender;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class StarboostBackendDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarboostBackendDemoApplication.class, args);
	}

	/**
	 * On startup, seed *only* the ADMIN user if none exists.
	 * We no longer auto-create Regions or Agencies here,
	 * because you have your real data in the database already.
	 */
	@Bean
	CommandLineRunner seedAdmin(UserRepository userRepo,
								PasswordEncoder encoder) {
		return args -> {
			// Only seed if the users table is completely empty:
			if (userRepo.count() == 0) {
				User admin = User.builder()
						.firstName("Super")
						.lastName("Admin")
						.email("admin@starboost.com")
						.phoneNumber("0000000000")
						.gender(Gender.M)
						.dateOfBirth(LocalDate.of(1990, 1, 1))

						// ← GIVE THEM TRUE ADMIN RIGHTS
						.role(Role.ADMIN)

						// ← NO AGENCY or REGION for a global ADMIN
						.agency(null)
						.region(null)

						.registrationNumber("REG-ADMIN-001")
						.password(encoder.encode("adminpass"))
						.build();

				userRepo.save(admin);
				System.out.println("✅ Seeded ADMIN user: " + admin.getEmail());
			}
		};
	}
}
