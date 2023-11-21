package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.CompanyBnoDto;
import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.CompanyLoginResponseDto;
import com.miracle.memberservice.dto.response.SuccessApiResponse;
import com.miracle.memberservice.util.MiracleTokenKey;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class CompanyService {

    public String bnoCerti(CompanyBnoDto bnoDto) {
        return null;
    }

    public PageMoveWithMessage join(CompanyJoinDto companyJoinDto, HttpSession session) {

        log.info(companyJoinDto.toString());

        ApiResponse response = ServiceCall.post(session, companyJoinDto, "company", "/join");

        log.info(response.toString());

        if(response.getHttpStatus()!=200) return new PageMoveWithMessage("guest/company-join", response.getMessage());

        return new PageMoveWithMessage("index");
    }

    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session){
        log.info(loginDto.toString());

        ApiResponse<String> response = ServiceCall.post(session, loginDto, loginDto.getMemberType(), "/login");

        log.info(response.toString());

        if(response.getHttpStatus()!=200) return new PageMoveWithMessage("guest/company-login", response.getMessage());

        log.info(response.getData().toString());

        //TODO login 구현
        return null;
    }

}
