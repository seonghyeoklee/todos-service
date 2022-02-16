package com.passionfactory.user.controller;

import com.passionfactory.user.dto.SignupRequestDto;
import com.passionfactory.user.dto.SignupResponseDto;
import com.passionfactory.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signup(signupRequestDto));
    }
}
