package com.api.backend.utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class RefreshTokenUtils {
  @Value("${refresh.token.secret}")
  private String REFRESH_TOKEN_SECRET;

  @Value("${refresh.token.expiration}")
  private int REFRESH_TOKEN_EXP;

  public RefreshTokenUtils() {
  }

  public String generateToken(String username) throws IllegalArgumentException {
    Instant now = Instant.now();
    Instant expiryDate = now.plusSeconds(REFRESH_TOKEN_EXP);

    return Jwts
        .builder()
        .setSubject(username)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiryDate))
        .signWith(getSignInKey())
        .compact();
  }

  public String extractUsername(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getSubject();
  }

  public boolean isTokenExpired(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getExpiration().before(new Date());
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private SecretKey getSignInKey() {
    return Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
  }

  public boolean validateJwtToken(String refresh_token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(refresh_token);
      return true;
    } catch (MalformedJwtException e) {
      System.out.println("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.out.println("JWT token is expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.out.println("JWT token is unsupported: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty: " + e.getMessage());
    }

    return false;
  }
}
