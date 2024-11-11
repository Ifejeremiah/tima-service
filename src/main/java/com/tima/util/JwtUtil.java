package com.tima.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${JWT.EXPIRATION.MS}")
    private long EXPIRATION;
    @Value("${JWT.SECRET}")
    private String SECRET;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getSubject();
    }
}
