//package com.project.community.security;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//import javax.crypto.SecretKey;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
////provee métodos para crear y verificar tokens
//
//@Component
//public class JwtUtil {
//    private final String SECRET = "claveSecretaMuySeguraclaveSecretaMuySeguraclaveSecretaMuySegura";
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//    }
//    public String generateToken(UserDetails userDetails) {
//        return Jwts.builder()
//            .subject(userDetails.getUsername())
//            .issuedAt(new Date())
//            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
//            .signWith(getSigningKey())
//            .compact();
//    }
//
//    public String extractUsername(String token) {
//        return Jwts.parser()
//            .verifyWith(getSigningKey())
//            .build()
//            .parseSignedClaims(token)
//            .getPayload()
//            .getSubject();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        return extractUsername(token).equals(userDetails.getUsername()) &&
//               !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        Date expiration = Jwts.parser()
//                .verifyWith(getSigningKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//            .getExpiration();
//        return expiration.before(new Date());
//    }
//
//}