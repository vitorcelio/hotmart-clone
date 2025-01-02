package com.hotmart.products.utils;

import com.hotmart.products.config.exception.ValidationException;
import com.hotmart.products.dto.request.ProductRequestDTO;
import com.hotmart.products.models.Product;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class ProductUtils {

    public static Long getUserId() {
        try {

            SecurityContext context = SecurityContextHolder.getContext();
            if (context == null) {
                throw new ValidationException("Usuário não autenticado");
            }

            if (context.getAuthentication().getPrincipal() != null && context.getAuthentication().getPrincipal() instanceof Jwt jwt) {
                String userId = jwt.getClaims().get("user_id").toString();

                if (userId == null) {
                    throw new ValidationException("Usuário não autenticado");
                }

                return Long.parseLong(userId);
            }
        } catch (Exception e) {
            throw new ValidationException("Usuário não autenticado");
        }

        return null;
    }

    public static void validationUser(@NonNull Long userId) {
        if (!ProductUtils.getUserId().equals(userId)) {
            throw new ValidationException("Usuário não tem permissão");
        }
    }

}
