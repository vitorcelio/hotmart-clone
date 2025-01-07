package com.hotmart.products.utils;

import com.hotmart.products.config.exception.ForbiddenException;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class ProductUtils {

    public static Long getUserId() {
        try {

            SecurityContext context = SecurityContextHolder.getContext();
            if (context == null) {
                throw new ForbiddenException("Token não encontrado");
            }

            if (context.getAuthentication().getPrincipal() != null && context.getAuthentication().getPrincipal() instanceof Jwt jwt) {
                String userId = jwt.getClaims().get("user_id").toString();

                if (userId == null) {
                    throw new ForbiddenException("Usuário não identificado");
                }

                return Long.parseLong(userId);
            }
        } catch (Exception e) {
            throw new ForbiddenException("Usuário não autenticado");
        }

        return null;
    }

    public static String getEmail() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context == null) {
                throw new ForbiddenException("Token não encontrado");
            }

            if (context.getAuthentication().getPrincipal() != null && context.getAuthentication().getPrincipal() instanceof Jwt jwt) {
                String email = jwt.getClaims().get("sub").toString();

                if (email == null) {
                    throw new ForbiddenException("Usuário não identificado");
                }

                return email;
            }
        } catch (Exception e) {
            throw new ForbiddenException("Usuário não autenticado");
        }

        return null;
    }

    public static void validationUser(@NonNull Long userId) {
        if (!ProductUtils.getUserId().equals(userId)) {
            throw new ForbiddenException("Usuário não tem permissão");
        }
    }

}
