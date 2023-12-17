package com.miracle.memberservice.dto.request;

import lombok.Data;

@Data
public class CompanyTokenRequestDto extends CommonTokenRequestDto {

    private final String bno;

    public CompanyTokenRequestDto(Long id, String email, String memberType, String bno) {
        super(id, email, memberType);
        this.bno = bno;
    }
}
