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

    /**
     * Validate token
     *
     * @param token Token to validate
     * @return 토큰이 인증된 서버에서 발급되었으며 만료되지 않았으면 true, 그렇지 않으면 false를 반환한다.
     * @author chocola
     */
    boolean validateToken(String token);
}
