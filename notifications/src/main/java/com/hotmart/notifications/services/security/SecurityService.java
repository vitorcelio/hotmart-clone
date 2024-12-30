package com.hotmart.notifications.services.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityService {

    public Long getUserId() {
        try {

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
        } catch (Exception e) {
            log.error("Erro ao busca ID do usuário dentro do context");
            return null;
        }

        return null;
    }

    public String getEmail() {
        try {
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
        } catch (Exception e) {
            log.error("Erro ao busca EMAIL do usuário dentro do context");
            return null;
        }

        return null;
    }

}
