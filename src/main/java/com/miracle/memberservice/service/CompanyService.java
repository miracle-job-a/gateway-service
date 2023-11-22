package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.CompanyCheckBnoRequestDto;
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

    public ResponseEntity<String> bnoCertify(CompanyCheckBnoRequestDto bno, HttpSession session) {
        log.info(bno.toString());

        log.info("first");
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60002")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        log.info("second");
        MiracleTokenKey key = new MiracleTokenKey(session);
        log.info("third");
        ApiResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/company/bno").build())
                .bodyValue(bno)
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .bodyToMono(ApiResponse.class).block();

        log.info("four");
        System.out.println("response object" + response.toString());
        //return response;
        int httpStatus = response.getHttpStatus();
        String errorMessage = response.getMessage();
        return ResponseEntity.status(httpStatus).body(errorMessage);
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
