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
public class JwtUtils {
  @Value("${access.token.secret}")
  private String ACCESS_TOKEN_SECRET;

  @Value("${access.token.expiration}")
  private int ACCESS_TOKEN_EXP;

  public String generateToken(String username) throws IllegalArgumentException {
    Instant now = Instant.now();
    Instant expiryDate = now.plusSeconds(ACCESS_TOKEN_EXP);

    return Jwts
        .builder()
        .setSubject(username)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiryDate))
        .signWith(getSignInKey())
        .compact();
  }

  public String extractUsername(String access_token) {
    Claims claims = extractAllClaims(access_token);
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
    return Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
  }

  public boolean validateJwtToken(String authToken) {
    try {
      System.out.println(authToken);
      if (authToken == null || authToken.split("\\.").length != 3) {
        System.out.println("Invalid token structure");
        return false;
      }

      Jwts.parserBuilder()
          .setSigningKey(getSignInKey())
          .build()
          .parseClaimsJws(authToken);
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
