package com.hotmart.notifications.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class NotificationsSecurityConfig {

    @Value("${spring.security.oauth2.client.registration.account-hotmart.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.account-hotmart.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.account-hotmart.authorization-grant-type}")
    private String authorizationGrantType;
    @Value("${spring.security.oauth2.client.registration.account-hotmart.scope}")
    private String scope;
    @Value("${spring.security.oauth2.client.registration.account-hotmart.client-name}")
    private String clientName;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/api/v1/emitter/subscribe/{userId}").permitAll()
                        .anyRequest().authenticated())
                .csrf().disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());


        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> userAuthorities = jwt.getClaimAsStringList("authorities");

            if (ObjectUtils.isEmpty(userAuthorities)) {
                userAuthorities = Collections.emptyList();
            }

            JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();

            Collection<GrantedAuthority> scopeAuthorities = scopesConverter.convert(jwt);

            if (!ObjectUtils.isEmpty(scopeAuthorities)) {
                List<SimpleGrantedAuthority> scopes =
                        userAuthorities.stream().map(SimpleGrantedAuthority::new).toList();
                scopeAuthorities.addAll(scopes);
            }

            return scopeAuthorities;
        });

        return converter;
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        // Mesma configuração que temos no nosso application.yml
        ClientRegistration client = ClientRegistration
                .withRegistrationId(clientName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .scope(scope)
                .clientName(clientName)
                .tokenUri(issuerUri + "/oauth2/token").build();

        return new InMemoryClientRegistrationRepository(client);
    }


    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

}
