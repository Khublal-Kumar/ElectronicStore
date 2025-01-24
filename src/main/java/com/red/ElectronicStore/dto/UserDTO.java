package com.red.ElectronicStore.dto;


import com.red.ElectronicStore.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {

    private String userId;

    @Size(min = 2,max = 25,message = "Invalid Length!!!")
    @NotBlank(message = "Enter your first name")
    private String firstName;


    @Size(min = 2,max = 25,message = "Invalid Length!!!")
    @NotBlank(message = "Enter your last name")
    private String lastName;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @NotBlank(message = "E-mail must be required")
    private String email;

    @Size(min = 2,max = 500,message = "Invalid Length!!!")
    private String upassword;

    private String encryptedPassword;

    @Size(max=10,message = "Phone no. must be 10 digit Number")
    private String phoneNumber;

    @Size(min = 5, max = 100,message = "Address must be in Range 5  to 100")
    private String address;
    private String dateOfBirth;
    private String createdAt;
    private String updatedAt;
    private String imageName;
    private Set<Role> roles;
}
