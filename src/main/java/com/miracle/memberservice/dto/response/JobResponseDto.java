package com.miracle.memberservice.dto.response;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class JobResponseDto {

    private final Long id;
    private final String name;


    @Builder
    public JobResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
