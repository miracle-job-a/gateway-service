package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class InterviewRequestDto {

    private final Long applicationLetterId;
    private final List<QnaDto> qnaList;
}
