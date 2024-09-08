package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.function.Function;


/**
 * The interface Jwt token service.
 */
public interface JwtTokenService {

    /**
     * Extract email string.
     *
     * @param token the token
     * @return the string
     * @throws ExpiredJwtException the expired jwt exception
     */
    String extractEmail(String token) throws ExpiredJwtException;

    /**
     * Extract claim t.
     *
     * @param <T>            the type parameter
     * @param token          the token
     * @param claimsResolver the claims resolver
     * @return the t
     * @throws ExpiredJwtException the expired jwt exception
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException;

    /**
     * Generate token string.
     *
     * @param user the user
     * @return the string
     */
    String generateToken(User user);

}
