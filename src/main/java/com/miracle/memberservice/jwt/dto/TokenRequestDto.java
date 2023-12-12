package com.miracle.memberservice.jwt.dto;

import lombok.Data;

@Data
public class TokenRequestDto {

    private final Long id;
    private final String email;
    private final String memberType;

    public TokenRequestDto() {
        this.id = null;
        this.email = null;
        this.memberType = null;
    }
}
