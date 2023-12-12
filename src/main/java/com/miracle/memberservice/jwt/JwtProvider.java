package com.miracle.memberservice.jwt;

import com.miracle.memberservice.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtProvider {

    private final Key key;
    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;

    public JwtProvider(String secretKey, long accessTokenValidityInSeconds, long refreshTokenValidityInSeconds) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }

    public Map<String, String> createTokens(String subject) {
        String accessToken = createAccessToken(subject);
        String refreshToken = createRefreshToken(subject);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public String createAccessToken(String subject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date now = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date expirationDate = Date.from(localDateTime.plusSeconds(accessTokenValidityInSeconds).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(String subject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date now = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date expirationDate = Date.from(localDateTime.plusSeconds(refreshTokenValidityInSeconds).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Date expirationDate = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            Date now = new Date();

            return now.before(expirationDate);
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String subject = extractSubject(refreshToken);
            return createRefreshToken(subject);
        }

        throw new InvalidTokenException();
    }

    private String extractSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
