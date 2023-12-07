package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ApplicationLetterPostRequestDto {

    private final Long resumeId;
    private final Long coverLetterId;
    private final String postType;
    private final Long postId;
    private final LocalDateTime submitDate;
    private final String applicationStatus;
    private final String userJob;

    public ApplicationLetterPostRequestDto(Long resumeId, Long coverLetterId, String postType, Long postId, String userJob) {
        this.resumeId = resumeId;
        this.coverLetterId = coverLetterId;
        this.postType = postType;
        this.postId = postId;
        this.submitDate = LocalDateTime.now();
        this.applicationStatus = "IN_PROGRESS";
        this.userJob = userJob;
    }
}
