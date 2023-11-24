package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyFaqResponseDto {
    private final Long id;
    private final String question;
    private final String answer;

    @Builder
    public CompanyFaqResponseDto(Object id, Object question, Object answer) {
        this.id = Long.parseLong(id.toString());
        this.question = String.valueOf(question);
        this.answer = String.valueOf(answer);
    }
}
