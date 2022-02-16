package com.passionfactory.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.passionfactory.config.auth.PrincipalDetails;
import com.passionfactory.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

import static com.passionfactory.config.jwt.JwtConstant.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final Environment env;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginRequestDto loginRequestDto;
		try {
			loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					loginRequestDto.getName(),
					loginRequestDto.getPassword()
			);

			return authenticationManager.authenticate(authenticationToken);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authResult) {
		PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

		String token = JWT.create()
				.withSubject(principal.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(getProperty(TOKEN_EXPIRATION_TIME.getKey()))))
				.withClaim(getProperty(TOKEN_CLAIM.getKey()), principal.getUser().getName())
				.sign(Algorithm.HMAC512(getProperty(TOKEN_SECRET.getKey())));

		response.addHeader(HttpHeaders.AUTHORIZATION, getProperty(TOKEN_PREFIX.getKey()) + " " + token);
	}

	private String getProperty(String key) {
		return Objects.requireNonNull(env.getProperty(key));
	}

}
