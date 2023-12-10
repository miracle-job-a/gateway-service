package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@Getter
@RequiredArgsConstructor
public class LoginDto {
    private final String email;
    private final String password;
    private final String memberType;
    private final String postId;
    private final String companyId;
    private final String postType;
}
