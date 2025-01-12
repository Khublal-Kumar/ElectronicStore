package com.red.ElectronicStore.helper;

import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseMessage {

    private String message;
    private boolean success;
    private HttpStatus status;
}
