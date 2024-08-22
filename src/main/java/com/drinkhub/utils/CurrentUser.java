package com.drinkhub.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private final HttpServletRequest request;

    public CurrentUser(HttpServletRequest request) {
        this.request = request;
    }

    public Long getUserId() {
        // Retrieve the user ID from the request attributes
        return (Long) request.getAttribute("userId");
    }

    public String getUsername() {
        // Retrieve the username from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }
}