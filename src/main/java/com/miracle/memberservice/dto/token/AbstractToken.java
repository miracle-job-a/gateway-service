package com.miracle.memberservice.dto.token;

import lombok.Data;

@Data
public abstract class AbstractToken {

    private final String token;

    protected AbstractToken(String token) {
        this.token = token;
    }
}
