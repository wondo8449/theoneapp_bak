package com.app.theone_back.security;

import com.app.theone_back.entity.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final String secretKey;

    @Value("${jwt.access.token.expiration}")
    long tokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    long refreshTokenExpiration;

    private static final String BEARER_PREFIX = "Bearer ";


    public JwtProvider(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String loginId, UserRoleEnum role) {

        return generateToken(loginId, role, tokenExpiration);
    }

    public String createRefreshToken(String loginId) {

        return generateRefreshToken(loginId, refreshTokenExpiration);
    }

    public String generateToken(String loginId, UserRoleEnum role, long expiration) {

        return Jwts.builder()
                .setSubject(loginId)
                .claim("role", role.getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(String loginId, long expiration) {

        return Jwts.builder()
                .setSubject(loginId)
                .claim("type", "refresh")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

    }

    public boolean validateRefreshToken(String token) {

        return validateToken(token);
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authorizationHeader.substring(7);
    }

    public Claims getClaimsFromToken(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

}
