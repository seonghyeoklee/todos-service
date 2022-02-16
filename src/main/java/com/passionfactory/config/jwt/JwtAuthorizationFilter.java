package com.passionfactory.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.passionfactory.config.auth.PrincipalDetails;
import com.passionfactory.user.entity.User;
import com.passionfactory.user.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.passionfactory.config.jwt.JwtConstant.*;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final Environment env;
	private final UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, Environment env) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.env = env;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(getProperty(TOKEN_PREFIX.getKey()))) {
			chain.doFilter(request, response);
			return;
		}

		String token = request
				.getHeader(HttpHeaders.AUTHORIZATION)
				.replace(getProperty(TOKEN_PREFIX.getKey()) + " ", "");

		String name = JWT.require(Algorithm.HMAC512(getProperty(TOKEN_SECRET.getKey())))
				.build()
				.verify(token)
				.getClaim(getProperty(TOKEN_CLAIM.getKey()))
				.asString();

		if (name != null) {
			User user = userRepository.findByName(name)
					.orElseThrow(IllegalArgumentException::new);

			PrincipalDetails principalDetails = new PrincipalDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					principalDetails,
					principalDetails.getPassword(),
					principalDetails.getAuthorities()
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

	private String getProperty(String key) {
		return Objects.requireNonNull(env.getProperty(key));
	}

}
