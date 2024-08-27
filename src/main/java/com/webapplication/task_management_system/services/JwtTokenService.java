package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

import java.util.function.Function;


public interface JwtTokenService {

    String extractEmail(String token) throws ExpiredJwtException;

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException;

    String generateToken(User user);

}
