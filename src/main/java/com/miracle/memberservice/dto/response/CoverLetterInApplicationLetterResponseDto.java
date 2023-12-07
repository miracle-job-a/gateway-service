package com.miracle.memberservice.dto.response;

import com.miracle.memberservice.dto.request.QnaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CoverLetterInApplicationLetterResponseDto {

    private final String coverLetterTitle;
    private final List<QnaDto> qnaList;

    @Builder
    public CoverLetterInApplicationLetterResponseDto(String coverLetterTitle, List<QnaDto> qnaList) {
        this.coverLetterTitle = coverLetterTitle;
        this.qnaList = List.copyOf(qnaList);
    }
}
