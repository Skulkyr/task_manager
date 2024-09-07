package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.user.AuthenticationRequest;
import com.webapplication.task_management_system.DTO.user.AuthenticationResponse;
import com.webapplication.task_management_system.DTO.user.RegisterRequest;
import com.webapplication.task_management_system.services.AuthenticationService;
import com.webapplication.task_management_system.utils.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "Authentication controller", description = "Manages user authorization and registration")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final ValidUtils validUtils;
    @Operation(summary = "User registration", responses =
            {@ApiResponse(description = "JWT token", responseCode = "201", content =
                    {@Content(mediaType = "JSON", examples = {@ExampleObject(value = "eyJhbGciOiJIUzUxMiJ9.eyJmaXJzdE5hbWUiOiJKYW5lIiwibGFzdE5hbWUiOiJTbWl0aCIsInN1YiI6ImphbmUuc21pdGhAZW1haWwuY29tIiwiaWF0IjoxNzI1NTI2MjMzLCJleHAiOjE3MjU1MzM0MzN9.r-m6Gn1z_5JbQMy_5-WDmWOifO7pMColrRCokDZI-c2mrvwUoPbRKtnHEuaSTZS6vXbhp_LUw7gF1LE5ghftFw")})})})
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
                                      BindingResult bindingResult) {

        validUtils.checkErrors(bindingResult);

        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Authorization", responses =
            {@ApiResponse(description = "JWT token", responseCode = "200", content =
                    {@Content(mediaType = "JSON", examples = {@ExampleObject(value = "eyJhbGciOiJIUzUxMiJ9.eyJmaXJzdE5hbWUiOiJKYW5lIiwibGFzdE5hbWUiOiJTbWl0aCIsInN1YiI6ImphbmUuc21pdGhAZW1haWwuY29tIiwiaWF0IjoxNzI1NTI2MjMzLCJleHAiOjE3MjU1MzM0MzN9.r-m6Gn1z_5JbQMy_5-WDmWOifO7pMColrRCokDZI-c2mrvwUoPbRKtnHEuaSTZS6vXbhp_LUw7gF1LE5ghftFw")})})})
    @PostMapping("/authenticate")
    public ResponseEntity<?> authentication(@Valid @RequestBody AuthenticationRequest request,
                                            BindingResult bindingResult) {

        validUtils.checkErrors(bindingResult);

        AuthenticationResponse response = authenticationService.authentication(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
