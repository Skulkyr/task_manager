package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.AuthenticationRequest;
import com.webapplication.task_management_system.DTO.AuthenticationResponse;
import com.webapplication.task_management_system.DTO.RegisterRequest;
import com.webapplication.task_management_system.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("\n")));
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body("Its ok");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authentication(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
