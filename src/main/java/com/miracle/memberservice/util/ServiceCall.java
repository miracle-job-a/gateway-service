package com.miracle.memberservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.memberservice.dto.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

public class ServiceCall {

    /**
     * get으로 보내줄 데이터와 세션 전달하여 APIResponse받아내는 메서드, url에 모든 정보가 전달됨.
     *
     * @param httpSession 현재 세션
     * @param url         서비스에 어떤 요청을 할 것인지
     * @return Webclient에 넘겨주고 받을 반응 데이터
     */
    public static ApiResponse get(HttpSession httpSession, String service, String url) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port(service))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(httpSession);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/" + url).build())
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> {
                    ObjectMapper om = new ObjectMapper();
                    WebClientResponseException exception = (WebClientResponseException) e;
                    String errorResponseBody = exception.getResponseBodyAsString(StandardCharsets.UTF_8);
                    ApiResponse apiResponse = null;
                    try {
                        apiResponse = om.readValue(errorResponseBody, ApiResponse.class);
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                    return Mono.just(apiResponse);
                }).block();
    }

    /**
     * post로 보내줄 데이터와 세션 전달하여 APIResponse받아내는 메서드
     *
     * @param httpSession 현재 세션
     * @param dto         넘겨줄 값
     * @param service     어떤 서비스에 넘겨줄지
     * @param url         서비스에 어떤 요청을 할 것인지
     * @return Webclient에 넘겨주고 받을 반응 데이터
     */
    public static ApiResponse post(HttpSession httpSession, Object dto, String service, String url) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port(service))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MiracleTokenKey key = new MiracleTokenKey(httpSession);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/" + url).build())
                .bodyValue(dto)
                .header("miracle", key.getHashcode())
                .header("sessionId", key.getSessionId())
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> {
                    ObjectMapper om = new ObjectMapper();
                    WebClientResponseException exception = (WebClientResponseException) e;
                    String errorResponseBody = exception.getResponseBodyAsString(StandardCharsets.UTF_8);
                    ApiResponse apiResponse = null;
                    try {
                        apiResponse = om.readValue(errorResponseBody, ApiResponse.class);
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                    return Mono.just(apiResponse);
                }).block();
    }

    private static int port(String memberType) {
        if (memberType.equals("user")) {
            return 60001;
        } else if (memberType.equals("company")) {
            return 60002;
        } else {
            return 60003;
        }
    }
}
