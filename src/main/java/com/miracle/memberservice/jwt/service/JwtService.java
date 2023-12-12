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

    public AccessToken createToken(Long id, String memberType, String email) {
        Objects.requireNonNull(id, "Id is null");
        Objects.requireNonNull(memberType, "Member type is null");
        Objects.requireNonNull(email, "Email is null");

        String subject = memberType + ":" + email;
        Map<String, String> tokens = jwtProvider.createTokens(id, subject);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        jwtRepository.save(subject, new RefreshToken(refreshToken));
        return new AccessToken(accessToken);
    }

    public boolean validateToken(Long id, String memberType, String email, String token) {
        Objects.requireNonNull(id, "Id is null");
        Objects.requireNonNull(memberType, "Member type is null");
        Objects.requireNonNull(email, "Email is null");

        String subject = memberType + ":" + email;
        return jwtProvider.validateToken(id, subject, token);
    }

    public AccessToken refreshToken(Long id, String subject) {
        String refreshToken = jwtRepository.get(subject).orElse(new RefreshToken("")).getToken();
        String refreshedAccessToken = jwtProvider.refreshAccessToken(id, subject, refreshToken);
        return new AccessToken(refreshedAccessToken);
    }
}
