package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ConditionalSearchPostResponseDto {

    private final Long id;
    private final String name;
    private final String title;
    private final String postType;
    private final String endDate;
    private final Boolean closed;
    private final String workAddress;
    private final Integer career;
    private final String job;

    public ConditionalSearchPostResponseDto() {
        this.id = null;
        this.name = null;
        this.title = null;
        this.postType = null;
        this.endDate = null;
        this.closed = null;
        this.workAddress = null;
        this.career = null;
        this.job = null;
    }

    @Builder
    public ConditionalSearchPostResponseDto(Long id, String name, String title, String postType, String endDate, Boolean closed, String workAddress, Integer career, String job) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.postType = postType;
        this.endDate = endDate;
        this.closed = closed;
        this.workAddress = workAddress;
        this.career = career;
        this.job = job;
    }
}
