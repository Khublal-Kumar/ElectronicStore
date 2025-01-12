package com.red.ElectronicStore.repositories;

import com.red.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find a user by their email address
    Optional<User> findByEmail(String email);

    // Find a user by their user ID (this should already be the default method since it's the primary key)
    Optional<User> findByUserId(String userId);

    // Check if a user exists by their email
    boolean existsByEmail(String email);

    // Check if a user exists by their user ID
    boolean existsByUserId(String userId);

    // Find all users with a specific last name
    List<User> findByLastName(String lastName);
}
