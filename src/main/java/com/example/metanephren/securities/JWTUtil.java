package com.example.metanephren.securities;

import com.example.metanephren.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

  @Value("${springbootwebfluxjjwt.jjwt.secret}") private String secret;

  @Value("${springbootwebfluxjjwt.jjwt.expiration}") private String expTime;

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }


  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    claims.put("role", user.getRoles());
    return doGenerateToken(claims, user.getUsername());
  }

  public Boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  private Boolean isTokenExpired(String token) {
    final Date exp = getExpDateFromToken(token);
    return exp.before(new Date());
  }

  private String doGenerateToken(Map<String, Object> claims, String username) {
    Long expTimeLong = Long.parseLong(expTime);
    final Date createdDate = new Date();
    final Date expDate = new Date(createdDate.getTime() + expTimeLong * 1000);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(createdDate)
        .setExpiration(expDate)
        .signWith(key)
        .compact();
  }

}
