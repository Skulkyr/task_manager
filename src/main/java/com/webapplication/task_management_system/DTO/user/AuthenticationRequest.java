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

    @NotBlank
    @Email
    private String email;

    @Size(min = 8, message = "Пароль должен содержать не менее 8 символов!")
    @Pattern(
            regexp = "^(?=.*[a-zA-Zа-яА-Я])(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d).+$",
            message = "Пароль должен содержать хотя бы одну заглавную, одну строчную букву и одну цифру!")
    private String password;

}
