package com.miracle.memberservice.service;

import com.miracle.memberservice.jwt.JwtProvider;
import com.miracle.memberservice.jwt.dto.AccessTokenDto;
import com.miracle.memberservice.jwt.dto.RefreshTokenDto;
import com.miracle.memberservice.repository.JwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtRepository jwtRepository;
    private final JwtProvider jwtProvider;

    public AccessTokenDto createToken(String email, String password, String memberType) {
        String subject = memberType + ":" + email + ":" + password;
        Map<String, String> tokens = jwtProvider.createTokens(subject);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        jwtRepository.save(subject, new RefreshTokenDto(refreshToken));
        return new AccessTokenDto(accessToken);
    }

    public AccessTokenDto refreshToken(String subject) {
        String refreshToken = jwtRepository.get(subject).orElse(new RefreshTokenDto("")).getToken();
        String refreshedAccessToken = jwtProvider.refreshAccessToken(refreshToken);
        return new AccessTokenDto(refreshedAccessToken);
    }
}
