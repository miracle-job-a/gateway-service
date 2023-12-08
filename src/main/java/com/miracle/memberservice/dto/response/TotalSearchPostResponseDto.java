package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TotalSearchPostResponseDto {

    private final Long id;
    private final String title;
    private final String endDate;
    private final String workAddress;
    private final Integer career;
    private final String job;

    @Builder
    public TotalSearchPostResponseDto(Long id, String title, String endDate, String workAddress, Integer career, String job) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.workAddress = workAddress;
        this.career = career;
        this.job = job;
    }
}
