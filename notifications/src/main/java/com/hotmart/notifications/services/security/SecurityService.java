package com.hotmart.notifications.services.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public Long getUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        if (context.getAuthentication().getPrincipal() != null && context.getAuthentication().getPrincipal() instanceof Jwt jwt) {
            String userId = jwt.getClaims().get("user_id").toString();

            if (userId == null) {
                return null;
            }

            return Long.parseLong(userId);
        }

        return null;
    }

    public String getEmail() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        if (context.getAuthentication().getPrincipal() != null && context.getAuthentication().getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaims().get("sub").toString();

            if (email == null) {
                return null;
            }

            return email;
        }

        return null;
    }

}
