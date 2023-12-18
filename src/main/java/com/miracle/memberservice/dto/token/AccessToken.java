package com.miracle.memberservice.dto.token;

import lombok.Data;

@Data
public class AccessToken extends AbstractToken {

    public AccessToken() {
    }

    public AccessToken(String token) {
        super(token);
    }
}
