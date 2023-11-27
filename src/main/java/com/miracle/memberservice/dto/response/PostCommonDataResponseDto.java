package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@ToString
public class PostCommonDataResponseDto {
    private final String name;
    private final String ceoName;
    private final String photo;
    private final int employeeNum;
    private final String address;
    private final String introduction;
    private final List<CompanyFaqResponseDto> faqList;

    @Builder
    public PostCommonDataResponseDto(Object name, Object ceoName, Object photo, Object employeeNum, Object address, Object introduction, List<CompanyFaqResponseDto> faqList) {
        this.name = String.valueOf(name);
        this.ceoName = String.valueOf(ceoName);
        this.photo = String.valueOf(photo);
        this.employeeNum = (int) employeeNum;
        this.address = String.valueOf(address);
        this.introduction = String.valueOf(introduction);
        this.faqList = faqList;
    }

    public PostCommonDataResponseDto() {
        this.name = null;
        this.ceoName = null;
        this.photo = null;
        this.employeeNum = 0;
        this.address = null;
        this.introduction = null;
        this.faqList = null;
    }
}