package com.miracle.memberservice.dto.request;

import lombok.Data;

@Data
public class CommonTokenRequestDto {

    private final Long id;
    private final String email;
    private final String memberType;
}
