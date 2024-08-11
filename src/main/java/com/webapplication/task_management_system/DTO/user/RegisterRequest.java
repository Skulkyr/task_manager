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
public class RegisterRequest {
    @NotNull(message = "First name can not be empty")
    @Size(min = 2, max = 30, message = "The name cannot be shorter than 2 characters!")
    private String firstName;

    @NotNull(message = "Last name can not be empty")
    @Size(min = 2, max = 30, message = "The last name cannot be shorter than 2 characters!")
    private String lastName;

    @NotBlank(message = "Enter Your Email!")
    @Email(message = "Incorrect email address!")
    private String email;

    @NotNull(message = "Password can not be empty")
    @Size(min = 8, max = 40, message = "The password must contain at least 8 characters!")
    @Pattern(
            regexp = "^(?=.*[a-zA-Zа-яА-Я])(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d).+$",
            message = "The password must contain at least one uppercase, one lowercase letter and one digit!")
    private String password;
}
