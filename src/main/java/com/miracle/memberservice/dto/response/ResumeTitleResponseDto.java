package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResumeTitleResponseDto {

    private final Long id;
    private final String title;

    @Builder
    public ResumeTitleResponseDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
