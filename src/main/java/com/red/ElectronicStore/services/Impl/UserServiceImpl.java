package com.red.ElectronicStore.services.Impl;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.UserDTO;
import com.red.ElectronicStore.entities.User;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.helper.PageResponseHandler;
import com.red.ElectronicStore.repositories.UserRepository;
import com.red.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        // Convert UserDTO to User entity
//        User user = new User();
//
//        String randomId = UUID.randomUUID().toString();
//        user.setUserId(randomId);
//        user.setFirstName(userDTO.getFirstName());
//        user.setLastName(userDTO.getLastName());
//        user.setEmail(userDTO.getEmail());
//        user.setPassword(userDTO.getPassword());
//        user.setPhoneNumber(userDTO.getPhoneNumber());
//        user.setAddress(userDTO.getAddress());
//        user.setDateOfBirth(userDTO.getDateOfBirth());
//        user.setCreatedAt(userDTO.getCreatedAt());
//        user.setUpdatedAt(userDTO.getUpdatedAt());
//        user.setImageName(userDTO.getImageName());
//
//        // Save the user in the database
//        User savedUser = userRepository.save(user);
//
//
//
//        // Convert saved User entity back to UserDTO
//        return mapToDTO(savedUser);

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
       return convertToDto(savedUser);

    }

    @Override
    public Optional<UserDTO> getUserById(String userId) {
        // Find user by userId
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            return Optional.of(convertToDto(userOptional.get()));
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            return Optional.of(convertToDto(userOptional.get()));
        } else {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
    }

    @Override
    public PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Get all users and convert them to DTOs

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<User> page=  userRepository.findAll(pageable);

        PageableResponse<UserDTO> userDTOPageableResponse = PageResponseHandler.getPageableResponse(page, UserDTO.class);


        return userDTOPageableResponse;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean userExistsById(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public void deleteUserById(String userId) {
        // Check if user exists before deleting
        if (userRepository.existsByUserId(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found. Cannot delete.");
        }
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO updatedUserDTO) {
        // Find existing user
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update user details
            user.setFirstName(updatedUserDTO.getFirstName());
            user.setLastName(updatedUserDTO.getLastName());
            user.setEmail(updatedUserDTO.getEmail());
            user.setPassword(updatedUserDTO.getPassword());
            user.setPhoneNumber(updatedUserDTO.getPhoneNumber());
            user.setAddress(updatedUserDTO.getAddress());
            user.setDateOfBirth(updatedUserDTO.getDateOfBirth());
            user.setUpdatedAt(updatedUserDTO.getUpdatedAt());
            user.setImageName(updatedUserDTO.getImageName());

            // Save updated user
            User updatedUser = userRepository.save(user);

            return convertToDto(updatedUser);
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found. Cannot update.");
        }
    }

    // Helper method to convert User entity to UserDTO
//    private UserDTO mapToDTO(User user) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUserId(user.getUserId());
//        userDTO.setFirstName(user.getFirstName());
//        userDTO.setLastName(user.getLastName());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setPhoneNumber(user.getPhoneNumber());
//        userDTO.setAddress(user.getAddress());
//        userDTO.setDateOfBirth(user.getDateOfBirth());
//        userDTO.setCreatedAt(user.getCreatedAt());
//        userDTO.setUpdatedAt(user.getUpdatedAt());
//        userDTO.setImageName(user.getImageName());
//        return userDTO;
//    }

    public UserDTO convertToDto(User user) {
        return mapper.map(user,UserDTO.class);
    }

    public User convertToEntity(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }
}