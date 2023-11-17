package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class CompanyJoinDto {
    private final String email;
    private final String name;
    private final String photo;
    private final String password;
    private final String bno;
    private final String ceoName;
    private final String sector;
    private final String address;
    private final String introduction;
    private final int employeeNum;
}
