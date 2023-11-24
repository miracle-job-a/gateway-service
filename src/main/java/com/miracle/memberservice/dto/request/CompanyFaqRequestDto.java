package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.servlet.http.HttpSession;

@ToString
@Getter
@RequiredArgsConstructor
public class CompanyFaqRequestDto {

    private final Long companyId;
    private final String question;
    private final String answer;

}
