package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyPageResponseDto {
    private final Integer companyId;
    private final Boolean approveStatus;
    private final String name;
    private final String ceoName;
    private final String photo;
    private final Integer employeeNum;
    private final String address;
    private final String introduction;
    private final String sector;
    private final Boolean bnoStatus;
    private final Integer countOpen;

    @Builder
    public CompanyPageResponseDto(Integer companyId, Boolean approveStatus, String name, String ceoName, String photo, Integer employeeNum, String address, String introduction, String sector, Boolean bnoStatus, Integer countOpen) {
        this.companyId = companyId;
        this.approveStatus = approveStatus;
        this.name = name;
        this.ceoName = ceoName;
        this.photo = photo;
        this.employeeNum = employeeNum;
        this.address = address;
        this.introduction = introduction;
        this.sector = sector;
        this.bnoStatus = bnoStatus;
        this.countOpen = countOpen;
    }

}

