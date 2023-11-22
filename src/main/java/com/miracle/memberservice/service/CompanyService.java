package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.CompanyCheckBnoRequestDto;
import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.CompanyLoginResponseDto;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@Service
@Slf4j
public class CompanyService {

    public ResponseEntity<String> bnoCertify(CompanyCheckBnoRequestDto bno, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, bno, "company", "/bno");

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    public PageMoveWithMessage join(CompanyJoinDto companyJoinDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, companyJoinDto, "company", "/join");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/company-join", response.getMessage());

        return new PageMoveWithMessage("index");
    }

    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, loginDto, loginDto.getMemberType(), "/login");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/company-login", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        CompanyLoginResponseDto dto = CompanyLoginResponseDto.builder()
                .id(data.get("id"))
                .email(data.get("email"))
                .bno(data.get("bno"))
                .build();

        return new PageMoveWithMessage("index", dto);
    }


}
