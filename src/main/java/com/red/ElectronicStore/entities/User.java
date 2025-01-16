package com.red.ElectronicStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    private String userId;  // The unique ID of the user, stored as a String

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;  // User's first name, with a maximum length of 100 characters

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;  // User's last name, with a maximum length of 100 characters

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;  // User's email, with a maximum length of 150 characters

    @Column(name = "password", nullable = false)
    private String password;  // User's Password, with a maximum length of 150 characters

    @Column(name = "phone_number", length = 15, nullable = true)
    private String phoneNumber;  // User's phone number, with a maximum length of 15 characters

    @Column(name = "address", length = 255, nullable = true)
    private String address;  // User's address, with a maximum length of 255 characters

    @Column(name = "date_of_birth", nullable = true)
    private String dateOfBirth;  // User's date of birth

    @Column(name = "created_at", nullable = false)
    private String createdAt;  // Timestamp of when the user was created

    @Column(name = "updated_at", nullable = true)
    private String updatedAt;  // Timestamp of when the user was last updated

    @Column(name = "img_name", nullable = true)
    private String imageName;  // Timestamp of when the user was last updated



}

