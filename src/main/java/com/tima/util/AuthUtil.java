package com.tima.util;

import com.tima.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof String)) {
            return (User) auth.getPrincipal();
        } return null;
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser() == null ? null : getCurrentUser().getEmail();
    }
}
