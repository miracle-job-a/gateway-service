package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyCheckBnoRequestDto {


    private final String bno;

    public CompanyCheckBnoRequestDto(String bno) {
        this.bno = bno;
    }
}
