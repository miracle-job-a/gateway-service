package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor
public class MzPostDto {

    private final Long id;
    private final String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'00:00:00")
    private final LocalDateTime end_date;
    private final String tool;
    private final String workAddress;
    private final String mainTask;
    private final String workCondition;
    private final String qualification;
    private final String benefit;
    private final String special_skill;
    private final String process;
    private final String notice;
    private final int career;


}
