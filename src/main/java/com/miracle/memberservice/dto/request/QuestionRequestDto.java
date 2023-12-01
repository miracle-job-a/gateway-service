package com.miracle.memberservice.dto.request;

import lombok.*;

@Getter
@ToString
public class QuestionRequestDto {
    private final Long id;
    private final String question;

    @Builder
    public QuestionRequestDto(Long id, String question) {
        this.id = id;
        this.question = question;
    }
}