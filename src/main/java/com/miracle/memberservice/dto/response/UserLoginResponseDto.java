package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private final Long id;
    private final String email;
    private final String name;

    @Builder
    public UserLoginResponseDto(Object id, Object email, Object name) {
        this.id = Long.parseLong(id.toString());
        this.email = String.valueOf(email);
        this.name = String.valueOf(name);
    }
}
