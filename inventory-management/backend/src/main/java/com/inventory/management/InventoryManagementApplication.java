package com.inventory.management;

import com.inventory.management.models.ERole;
import com.inventory.management.models.Role;
import com.inventory.management.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableScheduling
public class InventoryManagementApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			try {
				if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
					roleRepository.save(new Role(ERole.ROLE_ADMIN));
				}
				if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
					roleRepository.save(new Role(ERole.ROLE_USER));
				}
			} catch (Exception ex) {
				LOGGER.warn("Skipping role seeding as MongoDB is not available: {}", ex.getMessage());
			}
		};
	}
}
