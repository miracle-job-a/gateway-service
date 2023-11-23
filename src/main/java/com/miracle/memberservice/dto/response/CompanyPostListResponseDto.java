package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class CompanyPostListResponseDto {

    private final Long id;
    private final String postType;
    private final String title;
    private final String endDate;
    private final String modifiedAt;

    @Builder
    public CompanyPostListResponseDto(Object id, Object postType, Object title, Object endDate, Object modifiedAt) {
        this.id = Long.parseLong(id.toString());
        this.postType = String.valueOf(postType);
        this.title = String.valueOf(title);
        this.endDate = String.valueOf(endDate);
        this.modifiedAt = String.valueOf(modifiedAt);
    }
}
