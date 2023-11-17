package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.CompanyBnoDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@Slf4j
public class CompanyService {

    public String bnoCerti(CompanyBnoDto bnoDto) {
        return null;
    }

    private static final String PRIVATE_KEY = "TkwkdsladkdlrhdnjfrmqdhodlfjgrpaksgdlwnjTdjdy";

    public PageMoveWithMessage companyJoin(UserJoinDto userJoinDto, String sessionId) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60002")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String key = sessionId + PRIVATE_KEY;
        int h = key.hashCode();

        ResponseEntity<String> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/join").build())
                .bodyValue(userJoinDto)
                .header("miracle", String.valueOf(h))
                .header("sessionId", sessionId)
                .retrieve()
                .toEntity(String.class).block();
        log.info(userJoinDto.toString());
        assert response != null;
        log.info(response.toString());

        String body = response.getBody();

        String httpStatus = Objects.requireNonNull(body).split(",")[0].split(":")[1];
        if ("200".equals(httpStatus)) return new PageMoveWithMessage("index", null);

        body = body.substring(body.indexOf(":") + 2, body.lastIndexOf("\""));
        log.error(body);

        return new PageMoveWithMessage("guest/user-join", body);
    }
}
