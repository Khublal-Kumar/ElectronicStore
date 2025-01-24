package com.red.ElectronicStore;

import com.red.ElectronicStore.Enumeration.RoleName;
import com.red.ElectronicStore.entities.Role;
import com.red.ElectronicStore.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);



    }

}


