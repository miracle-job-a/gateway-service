package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Getter
@ToString
public class PostCreateRequestDto {
    private final Long postId;
    private final String postType;
    private final String title;
    private final int career;
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
    private final List<QuestionCreateRequestDto> questionList;
    private final Set<Long> jobIdSet;
    private final Set<Long> stackIdSet;
    private final LocalDateTime testStartDate;
    private final LocalDateTime testEndDate;

    public PostCreateRequestDto(Long postId, String postType, String title, int career, String endDate, String mainTask, String workCondition, String qualification, String tool, String benefit, String process, String notice, String specialSkill, String workAddress, List<QuestionCreateRequestDto> questionList, Set<Long> jobIdSet, Set<Long> stackIdSet, String testStartDate, String testEndDate) {
        this.postId = postId;
        this.postType = postType;
        this.title = title;
        this.career = career;
        this.endDate = removeMinute(endDate);
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
        this.testStartDate = removeMinute(testStartDate);
        this.testEndDate = removeMinute(testEndDate);
    }

    private LocalDateTime removeMinute(String time) {
        Function<String, Integer> stringIntegerFunction = Integer::parseInt;

        if(Objects.isNull(time)) return null;

        Integer year = stringIntegerFunction.apply(time.substring(0, 4));
        Integer month = stringIntegerFunction.apply(time.substring(5, 7));
        Integer dayOfMonth = stringIntegerFunction.apply(time.substring(8, 10));
        Integer hour = stringIntegerFunction.apply(time.substring(11, 13));

        return LocalDateTime.of(year, month, dayOfMonth, hour, 0);
    }
}
