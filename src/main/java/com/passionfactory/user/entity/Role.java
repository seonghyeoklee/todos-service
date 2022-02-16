package com.passionfactory.user.entity;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반사용자"),
    GUEST("ROLE_GUEST", "외부사용자");

    private final String role;
    private final String desc;

    Role(String role, String desc) {
        this.role = role;
        this.desc = desc;
    }

}
