package com.hotmart.account.services.registeredClient.impl;

import com.hotmart.account.config.exception.ValidationException;
import com.hotmart.account.dto.request.RegisteredClientRequestDTO;
import com.hotmart.account.dto.response.RegisteredClientResponseDTO;
import com.hotmart.account.models.RegisteredClientHotmart;
import com.hotmart.account.repositories.RegisteredClientHotmartRepository;
import com.hotmart.account.services.registeredClient.RegisteredClientService;
import com.hotmart.account.utils.AccountUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.hotmart.account.utils.AccountUtils.*;
import static com.hotmart.account.utils.AccountUtils.convertToAuthorizationGrantType;


@Slf4j
@RequiredArgsConstructor
@Service
public class RegisteredClientServiceImpl implements RegisteredClientService {

    private final JdbcRegisteredClientRepository registeredClientRepository;
    private final RegisteredClientHotmartRepository registeredClientHotmartRepository;

    @Override
    public void save(@NonNull RegisteredClientRequestDTO request) {
        try {
            var id = UUID.randomUUID().toString();
            createOrUpdateRegisteredClient(request, id);
            createRegisteredClientHotmart(request, id);
        } catch (Exception e) {
            log.error("Erro ao salvar registered client", e);
            throw new ValidationException("Erro ao salvar registered client");
        }
    }

    @Override
    public void update(@NonNull Integer id, @NonNull RegisteredClientRequestDTO request) {
        RegisteredClientHotmart registeredClientHotmart =
                registeredClientHotmartRepository.findById(id).orElseThrow(() -> new ValidationException("Registered Client not found"));

        try {
            createOrUpdateRegisteredClient(request, registeredClientHotmart.getRegisteredId());
            updateRegisteredClientHotmart(request, id);
        } catch (Exception e) {
            log.error("Erro ao atualizar registered client");
            throw new ValidationException("Erro ao atualizar registered client");
        }
    }

    @Override
    public RegisteredClient findById(@NonNull Integer id) {
        RegisteredClientHotmart registeredClientHotmart =
                registeredClientHotmartRepository.findById(id).orElseThrow(() -> new ValidationException("Registered Client not found"));

        return registeredClientRepository.findById(registeredClientHotmart.getRegisteredId());
    }

    @Override
    public List<RegisteredClientResponseDTO> findAll() {
        return RegisteredClientResponseDTO.convert(registeredClientHotmartRepository.findAll());
    }

    private void createOrUpdateRegisteredClient(@NonNull RegisteredClientRequestDTO request, @NonNull String registeredId) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        RegisteredClient.Builder client = RegisteredClient
                .withId(registeredId)
                .clientId(request.getClientId())
                .clientSecret(encoder.encode(request.getClientSecret()))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(5))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build());

        var clientAuth = new HashSet<>(convertToClientAuthenticationMethod(request.getClientAuthenticationMethod()));
        clientAuth.forEach(client::clientAuthenticationMethod);

        var grantTypes = new HashSet<>(convertToAuthorizationGrantType(request.getAuthorizationGrantType()));
        grantTypes.forEach(client::authorizationGrantType);

        var redirectUris = new HashSet<>(request.getRedirectUri());
        redirectUris.forEach(client::redirectUri);

        var scopes = new HashSet<>(request.getScopes());
        scopes.forEach(client::scope);

        registeredClientRepository.save(client.build());
    }

    private void createRegisteredClientHotmart(@NonNull RegisteredClientRequestDTO request, @NonNull String id) {
        RegisteredClientHotmart registeredClientHotmart = RegisteredClientHotmart.builder()
                .registeredId(id)
                .registeredClientId(request.getClientId())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        registeredClientHotmartRepository.save(registeredClientHotmart);
    }

    private void updateRegisteredClientHotmart(@NonNull RegisteredClientRequestDTO request, @NonNull Integer id) {
        RegisteredClientHotmart registeredClientHotmart = registeredClientHotmartRepository.findById(id).orElseThrow(() -> new ValidationException("Registered Client not found"));
        registeredClientHotmart.setRegisteredClientId(request.getClientId());
        registeredClientHotmart.setName(request.getName());
        registeredClientHotmart.setDescription(request.getDescription());

        registeredClientHotmartRepository.save(registeredClientHotmart);
    }
}
