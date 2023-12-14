package com.miracle.memberservice.service;

import com.miracle.memberservice.jwt.domain.AccessToken;
import com.miracle.memberservice.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;

    @Override
    public AccessToken createToken(String memberType, String email, Map<String, Object> claims) {
        return jwtService.createToken(memberType, email, claims);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
