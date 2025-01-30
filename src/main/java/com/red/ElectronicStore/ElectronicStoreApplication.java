package com.red.ElectronicStore;

import com.red.ElectronicStore.Enumeration.RoleName;
import com.red.ElectronicStore.entities.Role;
import com.red.ElectronicStore.entities.User;
import com.red.ElectronicStore.repositories.RoleRepository;
import com.red.ElectronicStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication {



	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);

		// Create PasswordEncoder instance
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// Generate a random UUID string
		String salt = UUID.randomUUID().toString();

		// Password to encode
		String rawPassword = "Round@101";

		// Encode password with UUID
		String encodedPassword = passwordEncoder.encode(rawPassword );

		// Print values
		System.out.println("UUID Salt: " + salt);
		System.out.println("Encoded Password: " + encodedPassword);
	}




}


