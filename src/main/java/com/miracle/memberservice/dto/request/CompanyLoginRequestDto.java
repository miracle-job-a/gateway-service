package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class CompanyLoginRequestDto {
    @Setter
    private String email;
    private final String password;

    public CompanyLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
