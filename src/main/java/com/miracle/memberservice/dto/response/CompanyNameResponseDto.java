package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyNameResponseDto {
    private final Long postId;
    private final Long companyId;
    private final String companyName;
    private final String title;

    @Builder
    public CompanyNameResponseDto(Long postId, Long companyId, String companyName, String title) {
        this.postId = postId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.title = title;
    }
}
