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
    @Size(min = 2, max = 20, message = "The name cannot be shorter than 2 characters!")
    @Schema(example = "Alexey")
    private String firstName;

    @NotNull(message = "Last name can not be empty")
    @Size(min = 2, max = 20, message = "The last name cannot be shorter than 2 characters!")
    @Schema(example = "Pogonin")
    private String lastName;

    @NotBlank(message = "Enter Your Email!")
    @Email(message = "Incorrect email address!")
    @Schema(description = "The address is being checked for validity", example = "skulkyr20@gmail.com")
    private String email;

    @NotNull(message = "Password can not be empty")
    @Size(min = 8, max = 40, message = "The password must contain at least 8 characters!")
    @Pattern(
            regexp = "^(?=.*[a-zA-Zа-яА-Я])(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d).+$",
            message = "The password must contain at least one uppercase, one lowercase letter and one digit!")
    @Schema(description = "The password must contain at least one uppercase, one lowercase letter and one digit!",
            example = "123456Qq")
    private String password;
}
