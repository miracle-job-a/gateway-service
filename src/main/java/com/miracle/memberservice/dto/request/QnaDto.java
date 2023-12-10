package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class QnaDto {
    private final String question;
    private final String answer;
}
