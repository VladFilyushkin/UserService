package com.innowise.userservice.service.impl;

import com.innowise.userservice.exception.ExpiredTokenException;
import com.innowise.userservice.exception.InvalidTokenException;
import com.innowise.userservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String secret;

  public Claims extractAllClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new ExpiredTokenException();
    } catch (JwtException e) {
      throw new InvalidTokenException();
    }
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
  }
}

