package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@ToString
@Getter
@EqualsAndHashCode
public class MainPagePostsResponseDto {

    private final Long id;
    private final Long companyId;
    private final String postType;
    private final String title;
    private final String name;
    private final String photo;
    private final String endDate;
    private final String workAddress;
    private final String jobIdSet;
    private final int career;

    @Builder
    public MainPagePostsResponseDto(Long id, Long companyId, String postType, String title, String name, String photo, String endDate, String workAddress, String jobIdSet, int career) {
        this.id = id;
        this.companyId = companyId;
        this.postType = postType;
        this.title = title;
        this.name = name;
        this.photo = photo;
        this.endDate = endDate;
        this.workAddress = workAddress;
        this.jobIdSet = jobIdSet;
        this.career = career;
    }
}
