package com.miracle.memberservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class PostRequestDto {
    private final Long companyId;
    private final String postType;
    private final String title;
    private final int career;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'00:00:00")
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
    private final List<String> questionList;
    private final List<Integer> jobIdSet;
    private final List<Integer> stackIdSet;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'00:00:00")
    private final LocalDateTime testStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'00:00:00")
    private final LocalDateTime testEndDate;

    public PostRequestDto(Long companyId, String postType, String title, int career, LocalDateTime endDate, String mainTask, String workCondition, String qualification, String tool, String benefit, String process, String notice, String specialSkill, String workAddress, List<String> questionList, List<Integer> jobIdSet, List<Integer> stackIdSet, LocalDateTime testStartDate, LocalDateTime testEndDate) {
        this.companyId = companyId;
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
        this.questionList = questionList;
        this.jobIdSet = jobIdSet;
        this.stackIdSet = stackIdSet;
        this.testStartDate = testStartDate;
        this.testEndDate = testEndDate;
    }

    @Builder
    public PostRequestDto() {
        this.companyId = null;
        this.postType = null;
        this.title = null;
        this.career = 0;
        this.endDate = null;
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
