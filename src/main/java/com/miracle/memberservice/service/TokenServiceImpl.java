package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.CommonTokenRequestDto;
import com.miracle.memberservice.dto.request.CompanyTokenRequestDto;
import com.miracle.memberservice.dto.request.UserTokenRequestDto;
import com.miracle.memberservice.dto.token.AccessToken;
import com.miracle.memberservice.util.Const;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AccessToken createToken(String memberType, String email, Map<String, Object> claims) {
        CommonTokenRequestDto dto;
        if (Const.RequestHeader.COMPANY.equals(memberType)) {
            dto = new CompanyTokenRequestDto((Long) claims.get("id"), email, memberType, claims.get("bno").toString());
        } else if (Const.RequestHeader.USER.equals(memberType)){
            dto = new UserTokenRequestDto((Long) claims.get("id"), email, memberType, claims.get("name").toString());
        } else {
            dto = new CommonTokenRequestDto((Long) claims.get("id"), email, memberType);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CommonTokenRequestDto> httpEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<AccessToken> responseEntity = restTemplate.postForEntity(
                "http://13.125.220.223:60200/v1/jwt/create",
                httpEntity,
                AccessToken.class);

        return responseEntity.getStatusCode().is2xxSuccessful() ? responseEntity.getBody() : null;
    }

    @Override
    public boolean validateToken(String token) {
        AccessToken accessToken = new AccessToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AccessToken> httpEntity = new HttpEntity<>(accessToken, headers);
        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(
                "http://13.125.220.223:60200/v1/jwt/validate",
                httpEntity,
                Boolean.class);

        boolean success = responseEntity.getStatusCode().is2xxSuccessful();
        boolean result = Boolean.TRUE.equals(responseEntity.getBody());
        return success && result;
    }

    @Override
    public Map<String, String> parseToken(String token) {
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                "http://13.125.220.223:60200/v1/jwt/parse?token=" + token,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        return responseEntity.getStatusCode().is2xxSuccessful() ? responseEntity.getBody() : null;
    }
}
