package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.user.AuthenticationRequest;
import com.webapplication.task_management_system.DTO.user.AuthenticationResponse;
import com.webapplication.task_management_system.DTO.user.RegisterRequest;


/**
 * The interface Authentication service.
 */
public interface AuthenticationService {
    /**
     * Register authentication response.
     *
     * @param request the register request DTO
     * @return the authentication response DTO
     */
    AuthenticationResponse register(RegisterRequest request);

    /**
     * Authentication response.
     *
     * @param request the AuthenticationRequest DTO
     * @return the authentication response DTO
     */
    AuthenticationResponse authentication(AuthenticationRequest request);

}
