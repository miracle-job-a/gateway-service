package com.miracle.memberservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.TokenDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ServiceCall {

    private static final String BASE_URL = "http://";
    private static final String VERSION = "/v1";

    private static WebClient.Builder createWebClientBuilder(String serviceType) {
        return WebClient.builder()
                .baseUrl(BASE_URL + port(serviceType))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private static Mono<ApiResponse> addCommonHeaders(WebClient.RequestHeadersSpec<?> request, HttpSession httpSession, String serviceType) {
        String capitalizeFirstLetter = capitalizeFirstLetter(serviceType);
        MiracleTokenKey key = new MiracleTokenKey(httpSession);
        String token = "Bearer " + createToken();

        return request
                .header(Const.RequestHeader.MIRACLE, key.getHashcode())
                .header(Const.RequestHeader.SESSION_ID, key.getSessionId())
                .header(capitalizeFirstLetter + Const.RequestHeader.HEADER_ID, String.valueOf(httpSession.getAttribute(Const.RequestHeader.ID)))
                .header(capitalizeFirstLetter + Const.RequestHeader.HEADER_EMAIL, (String) httpSession.getAttribute(Const.RequestHeader.EMAIL))
                .header(capitalizeFirstLetter + Const.RequestHeader.HEADER_BNO, (String) httpSession.getAttribute(Const.RequestHeader.BNO))
                .header(Const.RequestHeader.HEADER_AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(handleError());
    }

    private static Mono<ApiResponse> anotherHeaders(WebClient.RequestHeadersSpec<?> request, HttpSession httpSession, String serviceType, String userId) {
        String capitalizeFirstLetter = capitalizeFirstLetter(serviceType);
        MiracleTokenKey key = new MiracleTokenKey(httpSession);
        String token = "Bearer " + createToken();

        return request
                .header(Const.RequestHeader.MIRACLE, key.getHashcode())
                .header(Const.RequestHeader.SESSION_ID, key.getSessionId())
                .header(capitalizeFirstLetter + Const.RequestHeader.HEADER_ID, userId)
                .header(capitalizeFirstLetter + Const.RequestHeader.HEADER_AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(handleError());
    }

    public static ApiResponse get(HttpSession httpSession, String serviceType, String url) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParams(HttpSession httpSession, String serviceType, String url, int year, int month) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("year", year).queryParam("month", month).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getAnother(HttpSession httpSession, String serviceType, String url, Long userId) {
        return anotherHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()), httpSession, serviceType, String.valueOf(userId)).block();
    }

    public static ApiResponse post(HttpSession httpSession, Object dto, String serviceType, String url) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().post()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()).bodyValue(dto), httpSession, serviceType).block();
    }

    public static ApiResponse postParam(HttpSession httpSession, Object dto, String serviceType, String url, int strNum, int endNum) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().post()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).build()).bodyValue(dto), httpSession, serviceType).block();
    }

    public static ApiResponse delete(HttpSession httpSession, String serviceType, String url) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().delete()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()), httpSession, serviceType).block();
    }

    public static ApiResponse put(HttpSession httpSession, Object dto, String serviceType, String url) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().put()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()).bodyValue(dto), httpSession, serviceType).block();
    }

    public static ApiResponse putParam(HttpSession httpSession, String serviceType, String url, String name, String value) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().put()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam(name, value).build()), httpSession, serviceType).block();
    }

    public static ApiResponse putModifyParam(HttpSession httpSession, String serviceType, String url, String stackId, String modifiedName) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().put()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("stackId", stackId).queryParam("stackName", modifiedName).build()), httpSession, serviceType).block();
    }

    public static ApiResponse putModifyJobParam(HttpSession httpSession, String serviceType, String url, String jobId, String modifiedName) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().put()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("jobId", jobId).queryParam("jobName", modifiedName).build()), httpSession, serviceType).block();
    }

    public static ApiResponse putApproveCompany(HttpSession httpSession, String serviceType, String url) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().put()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParam(HttpSession httpSession, String serviceType, String url, String name, String value) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam(name, value).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamStackName(HttpSession httpSession, String serviceType, String url, String stackName) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("stackName", stackName).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamJobName(HttpSession httpSession, String serviceType, String url, String jobName) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("jobName", jobName).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamList(HttpSession httpSession, String serviceType, String url, int strNum, int endNum, String sort) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).queryParam("sort", sort).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamListWithToday(HttpSession httpSession, String serviceType, String url, int strNum, int endNum, boolean today) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).queryParam("today", today).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getUserParamListSort(HttpSession httpSession, String serviceType, String url, int strNum, int endNum, String sort) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("startPage", strNum).queryParam("endPage", endNum).queryParam("pageSize", 9).queryParam("sort", sort).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getUserParamListSearch(HttpSession httpSession, String serviceType, String url, int strNum, int endNum, String word) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("startPage", strNum).queryParam("endPage", endNum).queryParam("pageSize", 9).queryParam("word", word).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamSearch(HttpSession httpSession, String serviceType, String url, int strNum, int endNum, String keyword) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).queryParam("keyword", keyword).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamList(HttpSession httpSession, String serviceType, String url, int strNum, int endNum) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamList(HttpSession httpSession, String serviceType, String url, int strNum, int endNum, boolean today) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).queryParam("today", today).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamListForCount(HttpSession httpSession, String serviceType, String url) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).build()), httpSession, serviceType).block();
    }

    public static ApiResponse getParamListWithTodayFalse(HttpSession httpSession, String serviceType, String url, int strNum, int endNum) {
        return addCommonHeaders(createWebClientBuilder(serviceType).build().get()
                .uri(uriBuilder -> uriBuilder.path(VERSION + url).queryParam("strNum", strNum).queryParam("endNum", endNum).queryParam("today", false).build()), httpSession, serviceType).block();
    }

    private static String port(String memberType) {
        switch (memberType) {
            case Const.RequestHeader.USER:
                /*return "3.36.113.249:60001";*/
                return "localhost:60001";
            case Const.RequestHeader.COMPANY:
                return "13.125.211.61:60002";
            case Const.RequestHeader.ADMIN:
                return "3.36.98.12:60003";
            default:
                return "60000";
        }
    }

    private static String capitalizeFirstLetter(String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    private static Function<Throwable, Mono<ApiResponse>> handleError() {
        return e -> {
            ObjectMapper om = new ObjectMapper();
            WebClientResponseException exception = (WebClientResponseException) e;
            String errorResponseBody = exception.getResponseBodyAsString(StandardCharsets.UTF_8);
            ApiResponse apiResponse;
            try {
                apiResponse = om.readValue(errorResponseBody, ApiResponse.class);
            } catch (JsonProcessingException ex) {
                // 에러 응답을 만들 수 없는 경우 빈 ApiResponse를 반환
                apiResponse = new ApiResponse();
            }
            return Mono.just(apiResponse);
        };
    }

    private static String createToken() {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Boolean> map = new HashMap<>();
        map.put("gateway", true);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://13.125.220.223:60200/v1/jwt/create", map, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TokenDto tokenDto = objectMapper.readValue(stringResponseEntity.getBody(), TokenDto.class);
            return tokenDto.getToken();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}