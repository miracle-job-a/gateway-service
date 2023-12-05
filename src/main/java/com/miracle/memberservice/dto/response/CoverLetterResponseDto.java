package com.miracle.memberservice.dto.response;

import com.miracle.memberservice.dto.request.QnaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CoverLetterResponseDto {

    private final Long id;
    private final String title;
    private final String modifiedAt;
    private final List<QnaDto> qnaList;

    @Builder
    public CoverLetterResponseDto(Long id, String title, String modifiedAt, List<QnaDto> qnaList) {
        this.id = id;
        this.title = title;
        this.modifiedAt = modifiedAt;
        this.qnaList = qnaList;
    }
}
