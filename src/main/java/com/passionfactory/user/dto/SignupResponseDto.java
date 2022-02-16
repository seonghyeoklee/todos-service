package com.passionfactory.user.dto;

import com.passionfactory.user.entity.Role;
import com.passionfactory.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final Long id;
    private final String name;
    private final Integer age;
    private final Role role;

    @Builder
    public SignupResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
        this.role = user.getRole();
    }
}
