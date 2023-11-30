package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Set;

@Getter
@ToString
public class ResumeListResponseDto {

    private final Long id;
    private final String title;
    private final Set<JobResponseDto> jobIdSet;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final String modifiedAt;
    private final String open;

    @Builder
    public ResumeListResponseDto(Object id, Object title, Set<JobResponseDto> jobIdSet, Object modifiedAt, Object open){
        this.id = Long.parseLong(id.toString());
        this.title = String.valueOf(title);
        this.jobIdSet = jobIdSet;
        this.modifiedAt = String.valueOf(modifiedAt);
        this.open = String.valueOf(open);

    }
}
