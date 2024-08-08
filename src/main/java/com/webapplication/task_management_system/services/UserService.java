package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;

    public User getByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> {
            String message = STR."User with email=\{email} is not found!";
            log.warn(message);
            return new UsernameNotFoundException(message);
        });
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(() -> {
            String message = STR."User with email=\{username} is not found!";
            log.warn(message);
            return new UsernameNotFoundException(message);
        });
    }
}
