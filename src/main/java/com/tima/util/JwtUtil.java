package com.tima.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${JWT.ACCESS.EXPIRATION}")
    private long ACCESS_EXPIRATION;
    @Value("${JWT.REFRESH.EXPIRATION}")
    private long REFRESH_EXPIRATION;
    @Value("${JWT.SECRET}")
    private String SECRET;

    public String generateAccessToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String generateRefreshToken() {
        return JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(LocalDate.now().plusWeeks(REFRESH_EXPIRATION).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getSubject();
    }
}
