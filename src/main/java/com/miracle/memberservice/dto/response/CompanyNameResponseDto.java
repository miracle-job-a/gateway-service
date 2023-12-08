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

    @Builder
    public CompanyNameResponseDto(Long postId, Long companyId, String companyName) {
        this.postId = postId;
        this.companyId = companyId;
        this.companyName = companyName;
    }
}
