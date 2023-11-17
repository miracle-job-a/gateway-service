package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.UserJoinDto;
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
public class UserService {

    /**
     * 유저 회원가입 요청 API
     *
     * @param userJoinDto 회원가입 폼 태그에 있는 정보들
     * @param session     Token값 추출을 위한 세션
     * @return 회원가입 성공하면 메인 페이지 반환, 실패하면 에러메시지와 함께 다시 user-join 반환
     */
    public PageMoveWithMessage userJoin(UserJoinDto userJoinDto, HttpSession session) {

        log.info(userJoinDto.toString());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:60001")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(session);

        ResponseEntity<ApiResponse> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/join").build())
                .bodyValue(userJoinDto)
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
        return new PageMoveWithMessage("guest/user-join", errorMessage);
    }
}
