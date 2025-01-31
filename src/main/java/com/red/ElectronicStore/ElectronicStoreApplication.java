package com.red.ElectronicStore;

import com.red.ElectronicStore.Enumeration.RoleName;
import com.red.ElectronicStore.entities.Role;
import com.red.ElectronicStore.entities.User;
import com.red.ElectronicStore.repositories.RoleRepository;
import com.red.ElectronicStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;  // Non-static

	@Bean
	public CommandLineRunner initializeRoles() {
		return args -> {
			// Create roles only if they don't exist
			if (roleRepository.count() == 0) {
				Role userRole = Role.builder()
						.roleId(1)
						.roleName(RoleName.USER)
						.build();

				Role managerRole = Role.builder()
						.roleId(2)
						.roleName(RoleName.MANAGER)
						.build();

				Role moderatorRole = Role.builder()
						.roleId(3)
						.roleName(RoleName.MODERATOR)
						.build();

				Role adminRole = Role.builder()
						.roleId(4)
						.roleName(RoleName.ADMIN)
						.build();

				roleRepository.save(userRole);
				roleRepository.save(managerRole);
				roleRepository.save(moderatorRole);
				roleRepository.save(adminRole);
				System.out.println("Roles initialized successfully!");
			}
		};
	}
}


