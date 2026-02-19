package com.luis.taskapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
  private final JwtProperties props;
  private final Key key;

  public JwtService(JwtProperties props) {
    this.props = props;
    this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String subject) {
    Instant now = Instant.now();
    Instant exp = now.plus(props.expirationMinutes(), ChronoUnit.MINUTES);
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(exp))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String validateAndGetSubject(String token) throws JwtException {
    Jws<Claims> claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
    return claims.getBody().getSubject();
  }
}
