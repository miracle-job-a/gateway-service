package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class PostResponseDto {
    private final String postType;
    private final String title;
    private final Integer career;
    private final LocalDate endDate;
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
    private final LocalDate testStartDate;
    private final LocalDate testEndDate;

    @Builder
    public PostResponseDto(String postType, String title, Integer career, LocalDate endDate, String mainTask, String workCondition, String qualification, String tool, String benefit, String process, String notice, String specialSkill, String workAddress, Boolean closed, List<QuestionResponseDto> questionList, List<Integer> jobIdSet, List<Integer> stackIdSet) {
        this.postType = postType;
        this.title = title;
        this.career = career;
        this.endDate = endDate;
        this.mainTask = mainTask;
        this.workCondition = workCondition;
        this.qualification = qualification;
        this.tool = tool;
        this.benefit = benefit;
        this.process = process;
        this.notice = notice;
        this.specialSkill = specialSkill;
        this.workAddress = workAddress;
        this.closed = closed;
        this.questionList = questionList;
        this.jobIdSet = jobIdSet;
        this.stackIdSet = stackIdSet;
        this.testStartDate = null;
        this.testEndDate = null;
    }

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
