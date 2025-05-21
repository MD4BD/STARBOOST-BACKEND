package com.starboost.starboost_backend_demo;

import com.starboost.starboost_backend_demo.entity.*;
import com.starboost.starboost_backend_demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class StarboostBackendDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarboostBackendDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner seedData(
			RegionRepository regionRepo,
			AgencyRepository agencyRepo,
			UserRepository userRepo,
			PasswordEncoder encoder
	) {
		return args -> {
			// 1) Seed Regions
			if (regionRepo.count() == 0) {
				List<Region> regions = List.of(
						new Region(null, "R1", "North", List.of()),
						new Region(null, "R2", "South", List.of()),
						new Region(null, "R3", "East",  List.of()),
						new Region(null, "R4", "West", List.of()),
						new Region(null, "R5", "Central", List.of()),
						new Region(null, "R6", "Islands", List.of())
				);
				regionRepo.saveAll(regions);
			}

			// 2) Seed Agencies (one per region for demo)
			if (agencyRepo.count() == 0) {
				regionRepo.findAll().forEach(r -> {
					agencyRepo.save(new Agency(
							null,
							r.getCode() + "-A1",
							r.getName() + " HQ Branch",
							r
					));
				});
			}

			// 3) Seed an ADMIN user
			if (userRepo.count() == 0) {
				Region anyRegion = regionRepo.findAll().get(0);
				Agency anyAgency = agencyRepo.findAll().get(0);
				User admin = User.builder()
						.firstName("Super")
						.lastName("Admin")
						.email("admin@starboost.com")
						.phoneNumber("0000000000")
						.gender(Gender.M)
						.dateOfBirth(LocalDate.of(1990,1,1))
						.role(Role.AGENCY_MANAGER)
						.registrationNumber("REG-ADMIN-001")
						.region(anyRegion)
						.agency(anyAgency)
						.password(encoder.encode("adminpass"))
						.build();
				userRepo.save(admin);
			}
		};
	}


}
