package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.CompanyCheckBnoRequestDto;
import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.util.MiracleTokenKey;
import com.miracle.memberservice.util.PageMoveWithMessage;
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


    public PageMoveWithMessage companyJoin(CompanyJoinDto companyJoinDto, HttpSession session) {

        log.info(companyJoinDto.toString());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60002")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(session);

        ResponseEntity<ApiResponse> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/company/signup").build())
                .bodyValue(companyJoinDto)
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .toEntity(ApiResponse.class).block();

        ApiResponse body = response.getBody();

        log.info(body.toString());

        int httpStatus = body.getHttpStatus();
        if (httpStatus == 200) return new PageMoveWithMessage("index", null);

        String errorMessage = body.getMessage();
        log.error(errorMessage);
        return new PageMoveWithMessage("guest/company-join", errorMessage);

    }


}
