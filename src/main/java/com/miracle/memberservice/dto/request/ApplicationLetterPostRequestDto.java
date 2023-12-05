package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ApplicationLetterPostRequestDto {

    private final Long resumeId;
    private final Long coverLetterId;
    private final String postType;
    private final Long postId;
    private final LocalDateTime submitDate;
    private final String applicationStatus;
    private final String userJob;
}
