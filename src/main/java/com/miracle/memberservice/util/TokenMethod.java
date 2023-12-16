package com.miracle.memberservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.memberservice.dto.response.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class TokenMethod {
    public static String validateToken(TokenDto tokenDto) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> stringResponseEntity = restTemplate.postForEntity("http://13.125.220.223:60200/v1/jwt/validate-gateway", tokenDto, Boolean.class);
        if (Boolean.FALSE.equals(stringResponseEntity.getBody())) {
            tokenDto.setToken(createToken().getToken());
        }
        return tokenDto.getToken();
    }

    public static TokenDto createToken() {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Boolean> map = new HashMap<>();
        map.put("gateway", true);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://13.125.220.223:60200/v1/jwt/create", map, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(stringResponseEntity.getBody(), TokenDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
