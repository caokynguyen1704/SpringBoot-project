package com.example.webapplication.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

 @Data
public class SignupRequest {
    @NotBlank
    private String username;
 
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
