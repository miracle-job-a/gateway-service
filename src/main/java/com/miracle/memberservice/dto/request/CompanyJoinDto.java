package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Getter
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

    public CompanyJoinDto(String email, String name, String password, String bno, String ceoName, String sector, String address, String detailAddress, String introduction, int employeeNum) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.photo = bno;
        this.bno = bno;
        this.ceoName = ceoName;
        this.sector = sector;
        this.introduction = introduction;
        this.employeeNum = employeeNum;
        this.address = address + " " + detailAddress;
    }
}
