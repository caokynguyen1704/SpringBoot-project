package com.example.webapplication.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ChangePasswordRequest {
    @NotBlank
    private String current_password;
    @NotBlank
    private String new_password;

}