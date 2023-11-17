package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.util.HttpResponseMethod;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class UserService {

    private static final String PRIVATE_KEY = "TkwkdsladkdlrhdnjfrmqdhodlfjgrpaksgdlwnjTdjdy";

    public PageMoveWithMessage userJoin(UserJoinDto userJoinDto, String sessionId) {

        log.info(userJoinDto.toString());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60001")
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

        assert response != null;
        String httpStatus = HttpResponseMethod.makeHttpStatus(response);
        if ("200".equals(httpStatus)) return new PageMoveWithMessage("index", null);

        String errorMessage = HttpResponseMethod.makeErrorMessage(response);
        log.error(errorMessage);

        return new PageMoveWithMessage("guest/user-join", errorMessage);
    }

}
