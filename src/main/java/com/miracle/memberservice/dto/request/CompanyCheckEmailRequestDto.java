package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyCheckEmailRequestDto {
    private final String email;

    public CompanyCheckEmailRequestDto(String email) {
        this.email = email;
    }
}
