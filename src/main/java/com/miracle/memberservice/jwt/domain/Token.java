package com.miracle.memberservice.jwt.domain;

import lombok.Getter;

@Getter
public abstract class Token {

    private final String token;

    public Token(String token) {
        this.token = token;
    }
}
