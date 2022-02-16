package com.passionfactory.security;

import com.passionfactory.config.jwt.JwtAuthenticationFilter;
import com.passionfactory.config.jwt.JwtAuthorizationFilter;
import com.passionfactory.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.Filter;

import static com.passionfactory.user.entity.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment env;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/signup").permitAll();

        http.authorizeRequests()
                .antMatchers("/todos/**")
                .hasAnyAuthority(USER.getRole())
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(getAuthorizationFilter());
    }

    private Filter getAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), env);
    }

    private Filter getAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), userRepository, env);
    }

}
