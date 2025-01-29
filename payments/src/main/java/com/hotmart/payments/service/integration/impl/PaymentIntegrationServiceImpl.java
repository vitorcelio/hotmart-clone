package com.hotmart.payments.service.integration.impl;

import com.asaas.docs.configuration.AsaasApiConfig;
import com.asaas.docs.dto.request.CreditCardRequestDTO;
import com.asaas.docs.dto.request.PaymentRefundRequestDTO;
import com.asaas.docs.dto.request.PaymentRequestDTO;
import com.asaas.docs.dto.response.DeleteResponseDTO;
import com.asaas.docs.dto.response.PaymentResponseDTO;
import com.asaas.docs.enums.BillingType;
import com.asaas.docs.service.payment.impl.PaymentServiceImpl;
import com.hotmart.payments.config.exception.IntegrationException;
import com.hotmart.payments.dto.event.OrderEventDTO;
import com.hotmart.payments.dto.event.PaymentOrderDTO;
import com.hotmart.payments.enums.PaymentType;
import com.hotmart.payments.models.Payment;
import com.hotmart.payments.repositories.PaymentRepository;
import com.hotmart.payments.service.integration.PaymentIntegrationService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentIntegrationServiceImpl implements PaymentIntegrationService {

    @Value("${payment.provider.api-key}")
    private String apiKey;

    private final PaymentServiceImpl integrationService;
    private final PaymentRepository repository;

    @PostConstruct
    public void init() {
        AsaasApiConfig.setSandbox(true);
        AsaasApiConfig.setApiKey(apiKey);
    }

    @Override
    public PaymentResponseDTO createPayment(@NonNull Payment payment, @NonNull String customerId, @NonNull OrderEventDTO event) {
        try {
            PaymentResponseDTO response = createPayment(event, customerId, payment.getId());
            payment.setIntegrationId(response.getId());
            repository.save(payment);
            return response;
        } catch (IntegrationException e) {
            log.error("Erro na integração do pagamento: {}", e.getMessage());
            throw new IntegrationException("Erro na integração do pagamento", e);
        } catch (Exception e) {
            log.error("Erro interno ao criar pagamento: {}", e.getMessage());
            throw new RuntimeException("Erro interno ao criar pagamento", e);
        }
    }

    private PaymentResponseDTO createPayment(@NonNull OrderEventDTO event, @NonNull String customerId, @NonNull Long paymentId) throws IntegrationException {
        log.info("Processando pagamento para o cliente {}", customerId);

        PaymentOrderDTO paymentEvent = event.getOrder().getPayment();
        PaymentRequestDTO.PaymentRequestDTOBuilder request = PaymentRequestDTO.builder()
                .customer(customerId)
                .billingType(BillingType.valueOf(paymentEvent.getType().name()))
                .value(paymentEvent.getPrice())
                .dueDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .description("Hotmart Clone | " + event.getOrder().getProduct().getType())
                .externalReference(paymentId.toString());

        if (PaymentType.CREDIT_CARD.equals(paymentEvent.getType())) {
            var creditCard = CreditCardRequestDTO.builder()
                    .holderName(paymentEvent.getCardholderName())
                    .expiryYear(paymentEvent.getYearExpiry())
                    .expiryMonth(paymentEvent.getMonthExpiry())
                    .ccv(paymentEvent.getCvv())
                    .build();

            request.creditCard(creditCard)
                    .installmentCount(paymentEvent.getInstallment())
                    .totalValue(paymentEvent.getTotalPrice());
        }

        PaymentResponseDTO response = integrationService.createPayment(request.build());
        log.info("Pagamento processado com sucesso para o cliente {}", customerId);

        return response;
    }

    @Override
    public PaymentResponseDTO getPayment(String integrationId) {
        try {
            return integrationService.getPayment(integrationId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o pagamento", e);
        }
    }

    @Override
    public DeleteResponseDTO deletePayment(String integrationId) {
        try {
            return integrationService.deletePayment(integrationId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir o pagamento", e);
        }
    }

    @Override
    public PaymentResponseDTO refundPayment(String integrationId) {
        PaymentRefundRequestDTO request = PaymentRefundRequestDTO.builder()
                //.value(new BigDecimal("25.00"))
                .description("Estorno do pagamento")
                .build();

        try {
            return integrationService.refundPayment(integrationId, request);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao estornar o pagamento", e);
        }
    }

}
