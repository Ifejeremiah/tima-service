package com.tima.service;

import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService {

    protected int fetchCurrentUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Integer.parseInt(userId);
    }
}
