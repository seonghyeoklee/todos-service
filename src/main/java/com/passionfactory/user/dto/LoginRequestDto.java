package com.passionfactory.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class LoginRequestDto {

	@NotNull(message = "name cannot be null")
	@Size(min = 1, message = "name not be less than 1 characters")
	private String name;

	@NotNull(message = "password cannot be null")
	@Size(min = 8, message = "password not be less than 8 characters")
	private String password;

}
