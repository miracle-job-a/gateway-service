package com.miracle.memberservice.jwt.service;

import com.miracle.memberservice.jwt.JwtProvider;
import com.miracle.memberservice.jwt.domain.AccessToken;
import com.miracle.memberservice.jwt.domain.RefreshToken;
import com.miracle.memberservice.jwt.repository.JwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtRepository jwtRepository;
    private final JwtProvider jwtProvider;

    public AccessToken createToken(String memberType, String email, Map<String, Object> claims) {
        Objects.requireNonNull(memberType, "Member type is null");
        Objects.requireNonNull(email, "Email is null");

        String subject = memberType + ":" + email;
        Map<String, String> tokens = jwtProvider.createTokens(subject, claims);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        jwtRepository.save(subject, new RefreshToken(refreshToken));
        return new AccessToken(accessToken);
    }

    public boolean validateToken(String token) {
        return jwtProvider.validateToken(token);
    }

    public AccessToken refreshToken(String subject, Map<String, Object> claims) {
        String refreshToken = jwtRepository.get(subject).orElse(new RefreshToken("")).getToken();
        String refreshedAccessToken = jwtProvider.refreshAccessToken(refreshToken, subject, claims);
        return new AccessToken(refreshedAccessToken);
    }
}
