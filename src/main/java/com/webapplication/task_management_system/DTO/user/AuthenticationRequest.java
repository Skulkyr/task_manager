package com.webapplication.task_management_system.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Enter Your Email!")
    @Email(message = "Incorrect email address!")
    private String email;

    @Size(min = 8, message = "The password must contain at least 8 characters!")
    @Pattern(
            regexp = "^(?=.*[a-zA-Zа-яА-Я])(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d).+$",
            message = "The password must contain at least one uppercase, one lowercase letter and one digit!")
    private String password;

}
