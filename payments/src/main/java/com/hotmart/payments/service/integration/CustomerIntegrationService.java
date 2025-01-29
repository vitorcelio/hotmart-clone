package com.hotmart.payments.service.integration;

import com.asaas.docs.dto.response.CustomerResponseDTO;
import com.hotmart.payments.dto.event.OrderEventDTO;
import com.hotmart.payments.models.Customer;
import lombok.NonNull;

public interface CustomerIntegrationService {

    CustomerResponseDTO createCustomer(@NonNull Customer customer, @NonNull OrderEventDTO event);

    CustomerResponseDTO updateCustomer(@NonNull Customer customer);

    CustomerResponseDTO getCustomer(@NonNull String integrationId);

    void deleteCustomer(@NonNull String integrationId);

}
