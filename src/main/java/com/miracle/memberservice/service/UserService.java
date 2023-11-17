package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.dto.request.UserLoginDto;
import com.miracle.memberservice.util.HttpResponseMethod;
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
public class UserService {

    public PageMoveWithMessage userJoin(UserJoinDto userJoinDto, HttpSession session) {

        log.info(userJoinDto.toString());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60001")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(session);

        ResponseEntity<String> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/join").build())
                .bodyValue(userJoinDto)
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .toEntity(String.class).block();

        String httpStatus = HttpResponseMethod.makeHttpStatus(response);
        if ("200".equals(httpStatus)) return new PageMoveWithMessage("index", null);

        String errorMessage = HttpResponseMethod.makeErrorMessage(response);
        log.error(errorMessage);

        return new PageMoveWithMessage("guest/user-join", errorMessage);
    }

    public PageMoveWithMessage userLogin(UserLoginDto userLoginDto, HttpSession session) {
        log.info(userLoginDto.toString());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60001")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(session);

        ResponseEntity<String> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/login").build())
                .bodyValue(userLoginDto)
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .toEntity(String.class).block();

        String httpStatus = HttpResponseMethod.makeHttpStatus(response);
        if ("200".equals(httpStatus)) return new PageMoveWithMessage("index", null);

        String errorMessage = HttpResponseMethod.makeErrorMessage(response);
        log.error(errorMessage);

        return new PageMoveWithMessage("guest/user-login", errorMessage);
    }

}
