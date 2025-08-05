package com.tima.service;

import com.tima.dao.TokenDao;
import com.tima.exception.NotFoundException;
import com.tima.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class TokenService {
    @Value("${JWT.REFRESH.EXPIRATION}")
    private long REFRESH_EXPIRATION;
    TokenDao tokenDao;

    public TokenService(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public long create(String email, String refreshToken) {
        try {
            Token token = new Token();
            token.setEmail(email);
            token.setRefreshToken(refreshToken);
            token.setExpiresAt(Date.from(LocalDate.now().plusWeeks(REFRESH_EXPIRATION).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            return tokenDao.create(token);
        } catch (Exception error) {
            log.error("Error creating token", error);
            throw error;
        }
    }

    public Token findByEmailAndToken(String email, String token) {
        try {
            Token existing = tokenDao.findByEmailAndToken(email, token);
            if (existing == null) throw new NotFoundException("Could not find token");
            return existing;
        } catch (Exception error) {
            log.error("Error fetching token by email and token", error);
            throw error;
        }
    }
}
