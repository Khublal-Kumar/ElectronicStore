package com.red.ElectronicStore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private UserDTO user;
    private String jwtToken;

}
