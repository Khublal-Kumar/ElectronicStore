package com.red.ElectronicStore.services.Impl;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.RoleDTO;
import com.red.ElectronicStore.dto.UserDTO;
import com.red.ElectronicStore.entities.Role;
import com.red.ElectronicStore.entities.User;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.helper.PageResponseHandler;
import com.red.ElectronicStore.repositories.RoleRepository;
import com.red.ElectronicStore.repositories.UserRepository;
import com.red.ElectronicStore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {


        User user = convertToEntity(userDTO);
        String randomId = UUID.randomUUID().toString();
        user.setUserId(randomId);
        user.setEncryptedPassword(passwordEncoder.encode(user.getUpassword()));

        // Handle roles
        Set<Role> roles = new HashSet<>();

//        Set<Role> roles = new HashSet<>();

        // Handle roles - default to ROLE_USER if none specified
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            // Fetch ROLE_USER from database
            Role userRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));
            roles.add(userRole);

        } else {
            // Add specified roles
            for (RoleDTO roleDTO : userDTO.getRoles()) {
                Role role = roleRepository.findByRoleName(roleDTO.getRoleName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " ));
                roles.add(role);
            }
        }

        user.setRoles(roles);
//        System.out.println("*************************");
        log.info("Role is added too user ");

        User savedUser = userRepository.save(user);
        UserDTO userDTO1 = convertToDto(savedUser);

        return userDTO1;

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
//            user.setPassword(updatedUserDTO.getPassword());
            user.setUpassword(updatedUserDTO.getUpassword());
            user.setEncryptedPassword(passwordEncoder.encode(updatedUserDTO.getUpassword()));
            user.setPhoneNumber(updatedUserDTO.getPhoneNumber());
            user.setAddress(updatedUserDTO.getAddress());
            user.setDateOfBirth(updatedUserDTO.getDateOfBirth());
            user.setUpdatedAt(updatedUserDTO.getUpdatedAt());
            user.setImageName(updatedUserDTO.getImageName());

//           user.setRoles(updatedUserDTO.getRoleNames());



            // Handle roles
            Set<Role> roles = new HashSet<>();

            // Handle roles - default to ROLE_USER if none specified
            if (updatedUserDTO.getRoles() == null || updatedUserDTO.getRoles().isEmpty()) {
                // Fetch ROLE_USER from database
                Role userRole = roleRepository.findByRoleName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));
                roles.add(userRole);
            } else {
                // Add specified roles
                for (RoleDTO roleName : updatedUserDTO.getRoles()) {
                    Role role = roleRepository.findByRoleName(roleName.getRoleName())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                    roles.add(role);
                }
            }
            user.setRoles(roles);

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

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }


}
