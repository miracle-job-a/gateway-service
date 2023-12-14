package com.miracle.memberservice.jwt.dto;

import lombok.Data;

@Data
public class TokenRequestDto {

    private final Long id;
    private final String email;
    private final String memberType;
    private final String bno;
    private final String token;

    public TokenRequestDto() {
        this.id = null;
        this.email = null;
        this.memberType = null;
        this.bno = null;
        this.token = null;
    }
}
