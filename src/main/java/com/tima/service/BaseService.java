package com.tima.service;

import com.tima.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService {

    protected User fetchCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected int fetchCurrentUserId() {
        return fetchCurrentUser().getId();
    }

    protected String fetchCurrentUserEmail() {
        return fetchCurrentUser().getEmail();
    }
}
