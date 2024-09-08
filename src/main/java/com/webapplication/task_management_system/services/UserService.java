package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Gets by email.
     *
     * @param email the email
     * @return the by email
     */
    User getByEmail(String email);

    /**
     * Save user user.
     *
     * @param user the user
     * @return the user
     */
    User saveUser(User user);
}
