package com.webapplication.task_management_system.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User entity")
public class AuthenticationRequest {

    @NotBlank(message = "Enter Your Email!")
    @Email(message = "Incorrect email address!")
    private String email;

    @Size(min = 8, max = 40, message = "The password must contain at least 8 characters!")
    @NotNull(message = "Password can not be empty")
    @Pattern(
            regexp = "^(?=.*[a-zA-Zа-яА-Я])(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d).+$",
            message = "The password must contain at least one uppercase, one lowercase letter and one digit!")
    private String password;

}
