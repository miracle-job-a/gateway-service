package com.miracle.memberservice.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginResponseDto{
    private final Long id;
    private final String email;
    private final String name;

}
