package com.red.ElectronicStore.repositories;

import com.red.ElectronicStore.Enumeration.RoleName;
import com.red.ElectronicStore.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(RoleName roleName);
}
