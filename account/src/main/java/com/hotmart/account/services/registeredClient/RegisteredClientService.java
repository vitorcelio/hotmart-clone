package com.hotmart.account.services.registeredClient;

import com.hotmart.account.dto.request.RegisteredClientRequestDTO;
import com.hotmart.account.dto.response.RegisteredClientResponseDTO;
import lombok.NonNull;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;

public interface RegisteredClientService {

    void save(@NonNull RegisteredClientRequestDTO request);

    void update(@NonNull Integer id, @NonNull RegisteredClientRequestDTO request);

    RegisteredClient findById(@NonNull Integer id);

    List<RegisteredClientResponseDTO> findAll();

}
