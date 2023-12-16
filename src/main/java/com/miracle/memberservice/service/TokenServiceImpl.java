package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.token.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public AccessToken createToken(String memberType, String email, Map<String, Object> claims) {
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
