package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ApplicantListResponseDto {

    private final Long applicationLetterId;
    private final Long userId;
    private final String resumeTitle;
    private final String name;
    private final String email;
    private final String address;
    private final String submitDate;

    @Builder
    public ApplicantListResponseDto(Long applicationLetterId, Long userId, String resumeTitle, String name, String email, String address, String submitDate) {
        this.applicationLetterId = applicationLetterId;
        this.userId = userId;
        this.resumeTitle = resumeTitle;
        this.name = name;
        this.email = email;
        this.address = address;
        this.submitDate = submitDate;
    }
}
