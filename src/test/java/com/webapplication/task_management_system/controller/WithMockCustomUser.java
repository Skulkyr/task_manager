package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.entity.user.Role;
import com.webapplication.task_management_system.entity.user.User;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "test@mail.ru";

    String[] roles() default {"USER"};
}

class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal = new User();
        principal.setEmail(customUser.username());
        principal.setRoles(Arrays.stream(customUser.roles()).map(u -> Role.valueOf(STR."ROLE_\{u}")).toList());

        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(principal, "password", principal.getAuthorities());
        authentication.setAuthenticated(true);
        context.setAuthentication(authentication);

        return context;
    }
}
