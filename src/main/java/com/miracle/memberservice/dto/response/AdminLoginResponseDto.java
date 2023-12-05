package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminLoginResponseDto {
    private final Long id;
    private final String email;

    @Builder
    public AdminLoginResponseDto(Object id, Object email) {
        this.id = (Long) id;
        this.email = String.valueOf(email);
    }
}
