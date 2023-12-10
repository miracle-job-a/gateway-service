package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CompanyLoginResponseDto{
    private final Long id;
    private final String email;
    private final String bno;

    @Builder
    public CompanyLoginResponseDto(Object id, Object email, Object bno) {
        this.id = Long.parseLong(id.toString());
        this.email = String.valueOf(email);
        this.bno = String.valueOf(bno);
    }
}
