package com.miracle.memberservice.service;

import com.miracle.memberservice.jwt.domain.AccessToken;

public interface TokenService {

    /**
     * Create access-token
     *
     * @param clientId   Client ID
     * @param memberType Client type in (user, company, admin)
     * @param email      Client email
     * @return Return access-token
     * @author chocola
     */
    AccessToken createToken(Long clientId, String memberType, String email);
}
