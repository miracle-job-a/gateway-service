package com.miracle.memberservice.service;

import com.miracle.memberservice.jwt.domain.AccessToken;
import com.miracle.memberservice.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;

    @Override
    public AccessToken createToken(Long clientId, String memberType, String email) {
        return jwtService.createToken(clientId, memberType, email);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
