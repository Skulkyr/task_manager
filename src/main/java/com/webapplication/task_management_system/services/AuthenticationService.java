package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.AuthenticationRequest;
import com.webapplication.task_management_system.DTO.AuthenticationResponse;
import com.webapplication.task_management_system.DTO.RegisterRequest;
import com.webapplication.task_management_system.entity.user.Role;
import com.webapplication.task_management_system.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        String token = tokenService.generateToken(claims, userFromDB);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        User user = userService.getByEmail(request.getEmail());

        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        String token = tokenService.generateToken(claims, user);
        return new AuthenticationResponse(token);
    }
}
