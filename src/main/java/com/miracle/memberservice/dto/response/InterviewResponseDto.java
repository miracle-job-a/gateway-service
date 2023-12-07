package com.miracle.memberservice.dto.response;

import com.miracle.memberservice.dto.request.QnaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class InterviewResponseDto {

    private final List<QnaDto> qnaList;

    @Builder
    public InterviewResponseDto(List<QnaDto> qnaList) {
        this.qnaList = qnaList;
    }
}
