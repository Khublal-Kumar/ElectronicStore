package com.red.ElectronicStore.services;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Create or update a user using a UserDTO
    UserDTO saveUser(UserDTO userDTO);

    // Get a user by their userId (return UserDTO)
    Optional<UserDTO> getUserById(String userId);

    // Get a user by their email (return UserDTO)
    Optional<UserDTO> getUserByEmail(String email);

    // Get all users (return a list of UserDTOs)
    PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    // Check if a user exists by their email
    boolean userExistsByEmail(String email);

    // Check if a user exists by their userId
    boolean userExistsById(String userId);

    // Delete a user by their userId
    void deleteUserById(String userId);

    // Update user details (pass updated UserDTO)
    UserDTO updateUser(String userId, UserDTO updatedUserDTO);

    void clearAllRolesForUser(String userId);
}
