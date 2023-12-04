package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminLoginResponseDto {
    private final Long id;
    private final String email;

    @Builder
    public AdminLoginResponseDto(Object id, Object email, Object name) {
        this.id = Long.parseLong(id.toString());
        this.email = String.valueOf(email);
    }
}
