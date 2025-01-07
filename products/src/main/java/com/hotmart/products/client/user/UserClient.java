package com.hotmart.products.client.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserClient {

    private final String uriUserClient = "http://localhost:8081/api/v1/user";

    private final RestTemplate restTemplate;

    public Optional<UserClientResponse> findByEmail(final String email) {
        try {
            final String url = String.format("%s/email/%s", uriUserClient, email);
            final UserClientResponse response = restTemplate.getForObject(url, UserClientResponse.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Erro ao buscar usuário", e);
        }

        return null;
    }

}
