package com.miracle.memberservice.service;

import com.miracle.memberservice.jwt.domain.AccessToken;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public AccessToken createToken(Long clientId, String memberType, String email) {
        return null;
    }
}
