package com.hotmart.products.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OAuth2HeaderConfig {

    @Value("${spring.security.oauth2.client.registration.account-hotmart.client-name}")
    private String registrationId;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager) {
        return builder
                .additionalRequestCustomizers(request -> {
                    OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                            .withClientRegistrationId(registrationId)
                            .principal("ProductsService")
                            .build();

                    OAuth2AuthorizedClient authorize = authorizedClientManager.authorize(oAuth2AuthorizeRequest);

                    if (authorize != null) {
                        OAuth2AccessToken accessToken = authorize.getAccessToken();
                        request.getHeaders().setBearerAuth(accessToken.getTokenValue());
                    }
                })
                .build();
    }

}
