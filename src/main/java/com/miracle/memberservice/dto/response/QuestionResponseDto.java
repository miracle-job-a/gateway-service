package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class QuestionResponseDto {
    private final Long id;
    private final String question;
}