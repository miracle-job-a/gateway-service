package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class ManagePostsResponseDto {

    private final Long id;
    private final String postType;
    private final String title;
    private final String createdAt;
    private final String endDate;
    private final Boolean closed;

    @Builder
    public ManagePostsResponseDto(Long id, String postType, String title, String createdAt, String endDate, Boolean closed) {
        this.id = id;
        this.postType = postType;
        this.title = title;
        this.createdAt = createdAt;
        this.endDate = endDate;
        this.closed = closed;
    }

}
