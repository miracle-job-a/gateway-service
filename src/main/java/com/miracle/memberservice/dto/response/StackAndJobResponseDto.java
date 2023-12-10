package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StackAndJobResponseDto {
    private final Long id;
    private final String name;

    @Builder
    public StackAndJobResponseDto(Object id, Object name) {
        this.id = Long.parseLong(id.toString());
        this.name = String.valueOf(name);
    }
}
