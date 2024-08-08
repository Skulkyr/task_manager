package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.user.AuthenticationRequest;
import com.webapplication.task_management_system.DTO.user.AuthenticationResponse;
import com.webapplication.task_management_system.DTO.user.RegisterRequest;
import com.webapplication.task_management_system.services.AuthenticationService;
import com.webapplication.task_management_system.utils.ValidUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final ValidUtils validUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {

        validUtils.checkErrors(bindingResult);

        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authentication(
            @Valid @RequestBody AuthenticationRequest request, BindingResult bindingResult) {

        validUtils.checkErrors(bindingResult);

        AuthenticationResponse response = authenticationService.authentication(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
