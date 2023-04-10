package com.example.webapplication.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String new_password;
}
