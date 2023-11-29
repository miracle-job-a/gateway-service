package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class JobResponseDto {
    private final Long id;
    private final String name;

    @Builder
    public JobResponseDto(Object id, Object name){
        this.id = Long.parseLong(id.toString());
        this.name = String.valueOf(name);
    }



}
