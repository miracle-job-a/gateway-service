package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@ToString
public class ResumeListResponseDto {

    private final Long id;
    private final String title;
    private final ArrayList<Integer> jobIdSet;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final String modifiedAt;
    private final Boolean open;
    private final String photo;

    @Builder
    public ResumeListResponseDto(Long id, String title, ArrayList<Integer> jobIdSet, String modifiedAt, Boolean open, String photo) {
        this.id = id;
        this.title = title;
        this.jobIdSet = jobIdSet;
        this.modifiedAt = modifiedAt;
        this.open = open;
        this.photo = photo;
    }
}
