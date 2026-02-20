package com.oceanview.resort.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 Secret key (minimum 32 characters)
    private static final String SECRET_KEY =
            "OCEANVIEW_RESORT_SECRET_KEY_123456";

    // Generate JWT token
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)             // payload
                .setIssuedAt(new Date())           // created time
                .setExpiration(
                        new Date(System.currentTimeMillis() + 60 * 60 * 1000)
                )                                  // 1 hour expiry
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}

