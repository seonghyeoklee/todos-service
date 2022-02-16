package com.passionfactory.user.dto;

import com.passionfactory.user.entity.Role;
import com.passionfactory.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotNull(message = "name cannot be null")
    @Size(min = 2, message = "name not be less than 2 characters")
    private String name;

    @NotNull(message = "age cannot be null")
    @Min(value = 0)
    @Max(value = 150)
    private Integer age;

    @NotNull(message = "password cannot be null")
    @Size(min = 8, message = "password not be less than 8 characters")
    private String password;

    @NotNull(message = "role cannot be null")
    private Role role;

    public User toEntity() {
        return User.builder()
                .name(name)
                .age(age)
                .password(password)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
