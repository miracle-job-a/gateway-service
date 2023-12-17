package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.token.AccessToken;

import java.util.Map;

public interface TokenService {

    /**
     * Create access-token
     *
     * @param memberType Client type in (user, company, admin)
     * @param email      Client email
     * @param claims     Claims to add
     * @return Return access-token
     * @author chocola
     */
    AccessToken createToken(String memberType, String email, Map<String, Object> claims);

    /**
     * Validate token
     *
     * @param token Token to validate
     * @return 토큰이 인증된 서버에서 발급되었으며 만료되지 않았으면 true, 그렇지 않으면 false를 반환한다.
     * @author chocola
     */
    boolean validateToken(String token);

    /**
     * Parse token
     *
     * @param token Token to parse
     * @return Return claims
     * @author chocola
     */
    Map<String, String> parseToken(String token);
}
