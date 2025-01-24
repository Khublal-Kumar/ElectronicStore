package com.red.ElectronicStore.controllers;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.UserDTO;
import com.red.ElectronicStore.exceptions.BadApiRequest;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.helper.ApiResponseMessage;
import com.red.ElectronicStore.helper.ImageApiResponse;
import com.red.ElectronicStore.repositories.RoleRepository;
import com.red.ElectronicStore.services.FileService;
import com.red.ElectronicStore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {


    @Value("${user.profile.image.path}")
    private  String imageUploadPath;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public FileService fileService;



    // Create or update user
    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.saveUser(userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Get user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        Optional<UserDTO> user = userService.getUserById(userId);
        return user.map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<UserDTO> user = userService.getUserByEmail(email);
        return user.map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<PageableResponse<UserDTO>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "firstName",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    ) {
        PageableResponse<UserDTO> users = userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    // Check if a user exists by email
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> userExistsByEmail(@PathVariable String email) {
        boolean exists = userService.userExistsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // Check if a user exists by userId
    @GetMapping("/exists/{userId}")
    public ResponseEntity<ApiResponseMessage> userExistsById(@PathVariable String userId) {

        try {
            boolean exists = userService.userExistsById(userId);
            ApiResponseMessage message = ApiResponseMessage.builder().message("User Found..").success(true).status(HttpStatus.OK).build();
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch(UserNotFoundException ex){
            ApiResponseMessage message = ApiResponseMessage.builder().message("Something went wrong!!!").success(false).status(HttpStatus.BAD_REQUEST).build();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

    }

//    // Delete user by userId
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<ApiResponseMessage> deleteUserById(@PathVariable String userId) {
//
//
//        try {
//            userService.deleteUserById(userId);
//            ApiResponseMessage message = ApiResponseMessage.builder().message("Successfully Deleted!!!").success(true).status(HttpStatus.OK).build();
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (UserNotFoundException ex) {
//            ApiResponseMessage message = ApiResponseMessage.builder().message("Something went wrong!!!").success(false).status(HttpStatus.BAD_REQUEST).build();
//
//            return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
//        }
//    }
// Delete user by userId
@DeleteMapping("/{userId}")
public ResponseEntity<ApiResponseMessage> deleteUserById(@PathVariable String userId) {
    try {
        // Fetch the user by ID
        Optional<UserDTO> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        UserDTO userDTO = optionalUser.get();

        // Delete the user's image if it exists
        if (userDTO.getImageName() != null) {
            String imagePath = imageUploadPath + "/" + userDTO.getImageName();
            File imageFile = new File(imagePath);

            if (imageFile.exists() && imageFile.isFile()) {
                if (!imageFile.delete()) {
                    throw new IOException("Failed to delete user image: " + userDTO.getImageName());
                }
            }
        }

        // Delete the user's details from the database
        userService.deleteUserById(userId);

        // Build the success response
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User and associated details successfully deleted!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message, HttpStatus.OK);

    } catch (UserNotFoundException ex) {
        // Handle user not found scenario
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User not found with ID: " + userId)
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);

    } catch (IOException ex) {
        // Handle image file deletion failure
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Failed to delete user's associated image. Please try again.")
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);

    } catch (Exception ex) {
        // Handle other exceptions
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("An error occurred while deleting the user. Please try again.")
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}


    // Update user details
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO updatedUserDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(userId, updatedUserDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageApiResponse> uploadUserImage(
            @RequestParam MultipartFile userImage,
            @PathVariable String userId
            ) throws IOException, BadApiRequest {

        // Validate file
        if (userImage.isEmpty()) {
            throw new BadApiRequest("File is empty. Please upload a valid file.");
        }

        // Upload file and get the file name
        String imageName = fileService.uploadFile(userImage, imageUploadPath);

        // Fetch user by ID
        Optional<UserDTO> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            throw new BadApiRequest("User not found with ID: " + userId);
        }

        // Update user's image name
        UserDTO userDTO = optionalUser.get();
        // Check if user already has an image
        if (userDTO.getImageName() != null) {
            String existingImagePath = imageUploadPath + "/" + userDTO.getImageName();
            File existingImageFile = new File(existingImagePath);

            // Delete the existing image file
            if (existingImageFile.exists() && existingImageFile.isFile()) {
                if (!existingImageFile.delete()) {
                    throw new IOException("Failed to delete existing image: " + userDTO.getImageName());
                }
            }
        }

        userDTO.setImageName(imageName);
        userService.updateUser(userId, userDTO);



        // Build the response
        ImageApiResponse response = ImageApiResponse.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .messgae("Your image uploaded successfully!")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        Optional<UserDTO> userById = userService.getUserById(userId);

        UserDTO userDTO = userById.get();
        InputStream resource = fileService.getResource(imageUploadPath, userDTO.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());


    }
}
