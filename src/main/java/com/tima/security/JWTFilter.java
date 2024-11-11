package com.tima.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tima.model.User;
import com.tima.service.UserService;
import com.tima.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    JwtUtil jwtUtil;
    UserService userService;

    public JWTFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            String jwt = authHeader.substring(7);
            if (StringUtils.isBlank(jwt) || StringUtils.isEmpty(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    String email = jwtUtil.parseToken(jwt);
                    User user = userService.findByEmail(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword(), null);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException error) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, error.getMessage());
                }
            }
        }
    }
}
