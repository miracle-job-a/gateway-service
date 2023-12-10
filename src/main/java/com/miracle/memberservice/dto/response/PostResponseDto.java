package com.miracle.memberservice.dto.response;

import com.miracle.memberservice.dto.request.QuestionRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class PostResponseDto {
    private final String postType;
    private final String title;
    private final Integer career;
    private final LocalDateTime endDate;
    private final String mainTask;
    private final String workCondition;
    private final String qualification;
    private final String tool;
    private final String benefit;
    private final String process;
    private final String notice;
    private final String specialSkill;
    private final String workAddress;
    private final Boolean closed;
    private final List<QuestionResponseDto> questionList;
    private final List<Integer> jobIdSet;
    private final List<Integer> stackIdSet;
    private final LocalDateTime testStartDate;
    private final LocalDateTime testEndDate;

    public PostResponseDto() {
        this.closed = null;
        this.postType = null;
        this.title = null;
        this.endDate = null;
        this.career = null;
        this.mainTask = null;
        this.workCondition = null;
        this.qualification = null;
        this.tool = null;
        this.benefit = null;
        this.process = null;
        this.notice = null;
        this.specialSkill = null;
        this.workAddress = null;
        this.questionList = null;
        this.jobIdSet = null;
        this.stackIdSet = null;
        this.testStartDate = null;
        this.testEndDate = null;
    }
}
