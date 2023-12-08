package com.miracle.memberservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyInfoRequestDto {
    private final String name;
    private final String ceoName;
    private final int employeeNum;
    private final String sector;
    private final String photo;
    private final String introduction;
    private final String address;
    private final String pwd;

    @Builder
    public CompanyInfoRequestDto(String name, String ceoName, int employeeNum, String sector, String photo, String introduction, String address, String pwd) {
        this.name = name;
        this.ceoName = ceoName;
        this.employeeNum = employeeNum;
        this.sector = sector;
        this.photo = photo;
        this.introduction = introduction;
        this.address = address;
        this.pwd = pwd;
    }
}
