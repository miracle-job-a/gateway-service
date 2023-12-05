package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ConditionalSearchPostResponseDto {

    private final Long id;
    private final Long companyId;
    private final String name;
    private final String title;
    private final String postType;
    private final String endDate;
    private final Boolean closed;
    private final String workAddress;
    private final Integer career;
    private final String job;
    private final String photo;

    public ConditionalSearchPostResponseDto() {
        this.id = null;
        this.companyId = null;
        this.name = null;
        this.title = null;
        this.postType = null;
        this.endDate = null;
        this.closed = null;
        this.workAddress = null;
        this.career = null;
        this.job = null;
        this.photo = null;
    }

    @Builder
    public ConditionalSearchPostResponseDto(Long id, Long companyId, String name, String title, String postType, String endDate, Boolean closed, String workAddress, Integer career, String job, String photo) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.title = title;
        this.postType = postType;
        this.endDate = endDate;
        this.closed = closed;
        this.workAddress = workAddress;
        this.career = career;
        this.job = job;
        this.photo = photo;
    }
}
