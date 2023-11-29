package com.miracle.memberservice.dto.response;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class StackResponseDto {
  
    private final Long id;
    private final String name;


    @Builder
    public StackResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
  
}
