package com.passionfactory.config.jwt;

import lombok.Getter;

@Getter
public enum JwtConstant {
    TOKEN_CLAIM("token.claim", "jwt claim"),
    TOKEN_PREFIX("token.prefix", "토큰 고정값"),
    TOKEN_SECRET("token.secret", "토큰 시크릿값"),
    TOKEN_EXPIRATION_TIME("token.expiration_time", "토큰 만료시간");

    private final String key;
    private final String desc;

    JwtConstant(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
