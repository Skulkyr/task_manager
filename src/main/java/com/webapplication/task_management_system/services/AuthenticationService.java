package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.user.AuthenticationRequest;
import com.webapplication.task_management_system.DTO.user.AuthenticationResponse;
import com.webapplication.task_management_system.DTO.user.RegisterRequest;
import com.webapplication.task_management_system.entity.user.Role;
import com.webapplication.task_management_system.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;
    private final AuthenticationManager authManager;


    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.ROLE_USER))
                .build();

        User userFromDB = userService.saveUser(user);
        String token = tokenService.generateToken(userFromDB);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        User user = userService.getByEmail(request.getEmail());
        String token = tokenService.generateToken(user);

        return new AuthenticationResponse(token);
    }

}
