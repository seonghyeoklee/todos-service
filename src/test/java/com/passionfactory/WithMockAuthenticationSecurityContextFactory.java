package com.passionfactory;

import com.passionfactory.config.auth.PrincipalDetails;
import com.passionfactory.user.entity.User;
import com.passionfactory.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class WithMockAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthentication> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .id(1)
                .name(annotation.name())
                .age(30)
                .password("test1234")
                .role(annotation.role())
                .build();
        userRepository.save(user);

        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails,
                principalDetails.getPassword(),
                principalDetails.getAuthorities()
        );

        context.setAuthentication(authentication);
        return context;
    }

}