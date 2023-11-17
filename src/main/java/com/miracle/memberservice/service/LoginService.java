package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.LoginDto;
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
public class LoginService {
    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {
        log.info(loginDto.toString());

        String memberType = loginDto.getMemberType();

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:"+port(memberType))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(session);

        ResponseEntity<ApiResponse> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/"+memberType+"/login").build())
                .bodyValue(loginDto)
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .toEntity(ApiResponse.class).block();

        ApiResponse<Long> body = response.getBody();

        log.info(body.toString());

        int httpStatus = body.getHttpStatus();
        if (httpStatus == 200) return new PageMoveWithMessage("index", null, body.getData());

        String errorMessage = body.getMessage();
        log.error(errorMessage);
        return new PageMoveWithMessage("guest/"+memberType+"-join", errorMessage);
    }

    private int port(String memberType) {
        return memberType.equals("user") ? 60001 : 60002;
    }
}
