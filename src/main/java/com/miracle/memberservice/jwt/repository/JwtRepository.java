package com.miracle.memberservice.jwt.repository;

import com.miracle.memberservice.jwt.dto.RefreshTokenDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class JwtRepository {

    private final Map<String, RefreshTokenDto> refreshTokens = new HashMap<>();

    public void save(String subject, RefreshTokenDto refreshToken) {
        refreshTokens.put(subject, refreshToken);
    }

    public Optional<RefreshTokenDto> get(String subject) {
        return Optional.ofNullable(refreshTokens.get(subject));
    }
}
