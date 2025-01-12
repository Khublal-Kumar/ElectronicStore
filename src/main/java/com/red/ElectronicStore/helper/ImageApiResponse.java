package com.red.ElectronicStore.helper;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageApiResponse {


    private String imageName;
    private String messgae;
    private boolean success;
    private HttpStatus status;
}

