package com.example.TalkToDo.config.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
  private SecretKey secretKey;

  public JWTUtil(@Value("${jwt.secret}") String secret) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String createJWT(Long id, String username) {
    return Jwts.builder()
        .claim("id", id)
        .claim("username", username)
        .issuedAt(new Date(System.currentTimeMillis())) // 생성시기
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 만료시기
        .signWith(secretKey)
        .compact();
  }

  public Long getId(String token) {
    return Jwts.parser()
        .verifyWith(secretKey) // 검증.
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("id", Long.class);
  }

  public String getUsername(String token) { // username 확인
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("username", String.class);
  }

  // 만료 여부 확인
  public Boolean isExpired(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration()
        .before(new Date(System.currentTimeMillis()));
  }
}
