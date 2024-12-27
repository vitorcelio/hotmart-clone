package com.hotmart.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.hotmart.auth.utils.AuthUtils.OAUTH_AUTHORIZE_BUYER;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String redirectUrl = redirectUrl(request);

        response.sendRedirect(redirectUrl);
    }

    private String redirectUrl(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();

            String oauth2Params = session.getAttribute("oauth2Params").toString();
            session.removeAttribute("oauth2Params");

            return String.format("/oauth2/authorize?%s", oauth2Params);
        } catch (Exception e) {
            return OAUTH_AUTHORIZE_BUYER;
        }
    }

}
