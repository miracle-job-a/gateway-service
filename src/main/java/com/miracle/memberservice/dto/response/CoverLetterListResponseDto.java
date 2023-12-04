package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CoverLetterListResponseDto {

    private final Long id;
    private final Long userId;
    private final String title;
    private final String modifiedAt;

    @Builder
    public CoverLetterListResponseDto(Long id, Long userId, String title, String modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.modifiedAt = modifiedAt;
    }
}
