package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApplicationLetterListResponseDto {

    private final Long applicationLetterId;
    private final Long postId;
    private final Long interviewId;
    private final String postType;
    private final String submitDate;
    private final String applicationStatus;
    private final String job;

    @Builder
    public ApplicationLetterListResponseDto(Long applicationLetterId, Long postId, Long interviewId, String postType, String submitDate, String applicationStatus, String job) {
        this.applicationLetterId = applicationLetterId;
        this.postId = postId;
        this.interviewId = interviewId;
        this.postType = postType;
        this.submitDate = submitDate;
        this.applicationStatus = applicationStatus;
        this.job = job;
    }
}
