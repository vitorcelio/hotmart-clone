package com.hotmart.payments.service.payment.impl;

import com.asaas.docs.configuration.AsaasApiConfig;
import com.hotmart.payments.dto.event.OrderEventDTO;
import com.hotmart.payments.dto.response.PaymentResponseDTO;
import com.hotmart.payments.enums.PaymentGateway;
import com.hotmart.payments.enums.PaymentStatus;
import com.hotmart.payments.enums.PaymentType;
import com.hotmart.payments.repositories.CustomerRepository;
import com.hotmart.payments.repositories.PaymentRepository;
import com.hotmart.payments.service.payment.PaymentService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.provider.api-key}")
    private String apiKey;

    private final PaymentRepository repository;
    private final CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        AsaasApiConfig.setSandbox(true);
        AsaasApiConfig.setApiKey(apiKey);
    }

    @Override
    public void processPaymentSaga(@NonNull OrderEventDTO event) {

    }

    @Override
    public void rollbackPaymentSaga(@NonNull OrderEventDTO event) {

    }

    @Override
    public void requestRefund(@NonNull Long id) {

    }

    @Override
    public PaymentResponseDTO findById(@NonNull Long id) {
        return null;
    }

    @Override
    public List<PaymentResponseDTO> findAll(@NonNull Long userId, String orderId, String transactionId,
                                            PaymentType type, PaymentStatus status, PaymentGateway gateway,
                                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        return List.of();
    }
}
