package com.denis.afoh.intelliShop.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

   @Value("${app.jwt.secret}")
    private String secret;
   @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;

   private SecretKey key(){
       byte[] bytes = java.util.Base64.getDecoder().decode(secret);
       return io.jsonwebtoken.security.Keys.hmacShaKeyFor(bytes);
   }

   public String generateToken(UserDetails userDetails){
       long now = System.currentTimeMillis();
       return Jwts.builder()
               .subject(userDetails.getUsername()) // Email
               .issuedAt(new Date(now))
               .expiration(new Date(now + 1000L * 60 * 60 * 24))
               .signWith(key())
               .compact();
   }
   public String extractUsername(String token){
       return extractAllClaims(token).getSubject();
   }
   public boolean isTokenValid(String token, UserDetails userDetails){
       String username = extractUsername(token);
       return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
   }
   public boolean isTokenExpired(String token){
       return extractAllClaims(token).getExpiration().before(new Date()) ;
   }

    private Claims extractAllClaims(String token) {
       return Jwts.parser()
               .verifyWith(key())
               .build()
               .parseSignedClaims(token)
               .getPayload();
    }





}
