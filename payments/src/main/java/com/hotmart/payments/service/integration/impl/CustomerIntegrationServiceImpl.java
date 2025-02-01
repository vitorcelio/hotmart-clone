package com.hotmart.payments.service.integration.impl;

import com.asaas.docs.configuration.AsaasApiConfig;
import com.asaas.docs.dto.request.CustomerRequestDTO;
import com.asaas.docs.dto.request.SubaccountRequestDTO;
import com.asaas.docs.dto.response.CustomerResponseDTO;
import com.asaas.docs.dto.response.SubaccountResponseDTO;
import com.asaas.docs.enums.CompanyType;
import com.asaas.docs.service.customer.impl.CustomerServiceImpl;
import com.asaas.docs.service.subaccount.impl.SubaccountServiceImpl;
import com.hotmart.payments.config.exception.IntegrationException;
import com.hotmart.payments.dto.event.AddressOrderDTO;
import com.hotmart.payments.dto.event.BuyerOrderDTO;
import com.hotmart.payments.dto.event.OrderEventDTO;
import com.hotmart.payments.dto.response.CustomerIntegrationResponseDTO;
import com.hotmart.payments.models.Customer;
import com.hotmart.payments.repositories.CustomerRepository;
import com.hotmart.payments.service.integration.CustomerIntegrationService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerIntegrationServiceImpl implements CustomerIntegrationService {

    @Value("${payment.provider.api-key}")
    private String apiKey;

    private final CustomerServiceImpl integrationService;
    private final CustomerRepository repository;
    private final SubaccountServiceImpl subaccountIntegrationService;

    @PostConstruct
    public void init() {
        AsaasApiConfig.setSandbox(true);
        AsaasApiConfig.setApiKey(apiKey);
    }

    @Override
    public CustomerIntegrationResponseDTO createCustomer(@NonNull Customer customer, @NonNull OrderEventDTO event) {
        var response = new CustomerIntegrationResponseDTO();

        try {
            CustomerResponseDTO customerResponse = createCustomer(event, customer.getId());
            customer.setCustomerIntegrationId(customerResponse.getId());
            repository.save(customer);
            response.setCustomerId(customerResponse.getId());
        } catch (IntegrationException e) {
            log.error("Erro na integração do cliente (pagamento): {}", e.getMessage());
            throw new IntegrationException("Erro na integração do cliente (pagamento)", e);
        } catch (Exception e) {
            log.error("Erro interno ao cadastrar client: {}", e.getMessage());
            throw new RuntimeException("Erro interno ao cadastrar client");
        }

        try {
            SubaccountResponseDTO subaccountResponse = createSubAccount(event);
            customer.setSubaccountIntegrationId(subaccountResponse.getId());
            customer.setWalletId(subaccountResponse.getWalletId());
            customer.setApiKey(subaccountResponse.getApiKey());
            repository.save(customer);
            response.setSubaccountId(subaccountResponse.getId());
        } catch (IntegrationException e) {
            log.error("Erro na integração da conta (pagamento): {}", e.getMessage());
            throw new IntegrationException("Erro na integração da conta (pagamento)", e);
        } catch (Exception e) {
            log.error("Erro interno ao cadastrar conta: {}", e.getMessage());
            throw new RuntimeException("Erro interno ao cadastrar conta");
        }

        return response;
    }

    private CustomerResponseDTO createCustomer(@NonNull OrderEventDTO event, @NonNull Long customerId) throws IntegrationException {
        BuyerOrderDTO buyer = event.getOrder().getBuyer();
        AddressOrderDTO address = event.getOrder().getAddress();

        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .name(buyer.getName())
                .email(buyer.getEmail())
                .company("Hotmart Clone")
                .cpfCnpj(buyer.getCpfCnpj())
                .phone(buyer.getPhone())
                .mobilePhone(buyer.getPhone())
                .postalCode(address.getCep())
                .province(address.getNeighborhood())
                .address(address.getStreet())
                .complement(address.getComplement())
                .notificationDisabled(true)
                .externalReference(customerId.toString())
                .build();

        CustomerResponseDTO response = integrationService.createCustomer(request);
        log.info("Cliente {} criado com sucesso", response.getName());

        return response;
    }

    private SubaccountResponseDTO createSubAccount(@NonNull OrderEventDTO event) throws IntegrationException {
        BuyerOrderDTO buyer = event.getOrder().getBuyer();
        AddressOrderDTO address = event.getOrder().getAddress();

        SubaccountRequestDTO request = SubaccountRequestDTO.builder()
                .name(buyer.getName())
                .email(buyer.getEmail())
                .cpfCnpj(buyer.getCpfCnpj())
                .phone(buyer.getPhone())
                .mobilePhone(buyer.getPhone())
                .incomeValue(new BigDecimal("1000"))
                .postalCode(address.getCep())
                .province(address.getNeighborhood())
                .address(address.getStreet())
                .complement(address.getComplement())
                .build();

        SubaccountResponseDTO response = subaccountIntegrationService.createSubaccount(request);
        log.info("Subaccount {} criada com sucesso", response.getName());

        return response;
    }

    @Override
    public CustomerResponseDTO updateCustomer(@NonNull Customer customer) {
        return null;
    }

    @Override
    public CustomerResponseDTO getCustomer(@NonNull String integrationId) {
        return null;
    }

    @Override
    public void deleteCustomer(@NonNull String integrationId) {

    }

}
