package com.passionfactory;

import com.passionfactory.user.entity.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthenticationSecurityContextFactory.class)
public @interface WithMockAuthentication {
    String name();

    Role role();
}
