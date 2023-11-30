package com.miracle.memberservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@RequiredArgsConstructor
public class PostRequestDto {
    private final String postType;
    private final String title;
    private final int career;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
    private final List<QuestionRequestDto> questionList;
    private final Set<Long> jobIdSet;
    private final Set<Long> stackIdSet;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime testStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime testEndDate;
}
