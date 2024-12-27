package com.hotmart.auth.config;

import com.hotmart.auth.models.UserEntity;
import com.hotmart.auth.repositories.RoleRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@EnableWebSecurity
@Configuration
public class AuthSecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                ).csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer);

        return http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login") {
                    @Override
                    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
                        String originalParams = request.getQueryString();
                        return super.determineUrlToUseForThisRequest(request, response, exception)
                                + (originalParams != null ? "?" + originalParams : "");
                    }
                }))
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtEncodingContextCustomizer(RoleRepository roleRepository) {
        return (context -> {
            Authentication authentication = context.getPrincipal();
            if (authentication.getPrincipal() instanceof UserEntity userEntity) {

                var userRoles = roleRepository.findByUserId(userEntity.getId());

                Set<String> authorities = new HashSet<>();
                if (!ObjectUtils.isEmpty(userRoles)) {
                    for (var role : userRoles) {
                        authorities.add(role.getAuthority());
                    }
                }

                context.getClaims().claim("user_id", userEntity.getId().toString());
                context.getClaims().claim("user_fullname", userEntity.getName());

                if (!ObjectUtils.isEmpty(authorities)) {
                    context.getClaims().claim("authorities", authorities);
                }
            }
        });
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(BCryptPasswordEncoder encoder, JdbcTemplate jdbcTemplate) {

        RegisteredClient hotmartClient = RegisteredClient
                .withId("1")
                .clientId("hotmartClient")
                .clientSecret(encoder.encode("CLIENT_vM8KB274xmrkW"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("user_read") // acesso a informações do usuário
                .scope("purchase_read") // leitura das informações de compras (histórico e transações)
                .scope("subscription_read") // acesso a informações de assinatura (status e períodos de cobranças)
                .scope("product_read") // acesso detalhado do produto
                .scope("order_read") // acesso a informações de pedidos realizados
                .scope("affiliation_read") // acesso a informações sobre status das afiliações e afiliados
                .scope("sales_read") // leitura dos relatórios de vendas e métricas financeiras
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(5))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        RegisteredClient hotmartApp = RegisteredClient
                .withId("2")
                .clientId("hotmartApp")
                .clientSecret(encoder.encode("APP_2BqKH11e5rkX"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:3000/authorized")
                .redirectUri("https://oidcdebugger.com/debug")
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scope("user_read") // acesso a informações do usuário
                .scope("password_write") // permite modificar a senha
                .scope("profile_read") // acesso completo ao perfil do usuário
                .scope("user_write") // permite modificar informações de perfil do usuário, como email, nome e outros dados pessoais
                .scope("purchase_read") // acesso ao histórico de compras
                .scope("purchase_write") // permite modificar ou registrar novas compras, como ajustes ou cancelamentos
                .scope("subscription_read") // acesso às assinaturas do usuário, status e detalhes
                .scope("subscription_write") // permite alterar ou cancelar assinaturas vinculadas
                .scope("affiliation_read") // acesso ao status e informações sobre afiliações
                .scope("affiliation_write") // permite alterar afiliações, aprovar ou rejeitar
                .scope("notification_read") // leitura das notificações recebidas pelo usuário, como alertas de transações
                .scope("notification_manage") // permite gerenciar notificações, incluindo ativação ou desativação de alertas
                .scope("commission_read") // permite acessar comissões que o usuário ou afiliado recebeu
                .scope("commission_write") // permite ajustar comissões, como pagamentos antecipados ou correções
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(5))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        registeredClientRepository.save(hotmartClient);
        registeredClientRepository.save(hotmartApp);

        return registeredClientRepository;
    }

    @Bean
    public OAuth2AuthorizationConsentService oauth2AuthorizationConsentService(JdbcOperations jdbcOperations,
                                                                               RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations,
                                                                 RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(AuthProperties authProperties) {
        return AuthorizationServerSettings.builder()
                .issuer(authProperties.getProviderUri())
                .build();
    }

    @Bean
    public JWKSet jwkSet(AuthProperties authProperties) throws Exception {
        AuthProperties.JksProperties jksProperties = authProperties.getJks();

        final String path = jksProperties.getPath();
        final InputStream inputStream = new ClassPathResource(path).getInputStream();

        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, jksProperties.getStorepass().toCharArray());

        RSAKey rsaKey = RSAKey.load(
                keyStore,
                jksProperties.getAlias(),
                jksProperties.getKeypass().toCharArray());

        return new JWKSet(rsaKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return (((jwkSelector, securityContext) -> jwkSelector.select(jwkSet)));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}
