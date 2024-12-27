package com.hotmart.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class OAuth2ParameterPreservationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Verifica se a requisição contém os parâmetros esperados
        String queryString = request.getQueryString();
        if (request.getRequestURI().equals("/login") && (queryString != null && queryString.contains("redirect"))) {
            HttpSession session = request.getSession();
            session.setAttribute("oauth2Params", queryString);

        }

        filterChain.doFilter(request, response);
    }
}
