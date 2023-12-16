package com.miracle.memberservice.dto.request;

import lombok.Data;

@Data
public class UserTokenRequestDto extends CommonTokenRequestDto {

    private final String name;

    public UserTokenRequestDto(Long id, String email, String memberType, String name) {
        super(id, email, memberType);
        this.name = name;
    }
}
