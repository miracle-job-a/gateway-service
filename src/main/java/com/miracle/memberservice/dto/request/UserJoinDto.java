package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserJoinDto {
    private final String email;
    private final String name;
    private final String password;
    private final String phone;
    private final LocalDate birth;
    private final String address;
}
