package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class StackResponseDto {
    private final Long id;
    private final String name;

    @Builder
    public StackResponseDto(Object id, Object name){
        this.id = Long.parseLong(id.toString());
        this.name = String.valueOf(name);
    }



}
