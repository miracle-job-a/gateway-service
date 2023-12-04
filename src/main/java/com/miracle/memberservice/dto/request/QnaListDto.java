package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
@Getter
@ToString
@RequiredArgsConstructor
public class QnaListDto {
    private final List<String> question;
    private final List<String> answer;
}
