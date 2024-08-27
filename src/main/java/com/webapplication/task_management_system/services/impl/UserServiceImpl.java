package com.webapplication.task_management_system.services.impl;

import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.repository.UserRepository;
import com.webapplication.task_management_system.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    @Transactional
    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> {
            String message = STR."User with email=\{email} is not found!";
            log.warn(message);
            return new UsernameNotFoundException(message);
        });
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(() -> {
            String message = STR."User with email=\{username} is not found!";
            log.warn(message);
            return new UsernameNotFoundException(message);
        });
    }
}
