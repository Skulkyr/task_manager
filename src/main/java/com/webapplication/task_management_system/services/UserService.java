package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    User getByEmail(String email);

    User saveUser(User user);
}
