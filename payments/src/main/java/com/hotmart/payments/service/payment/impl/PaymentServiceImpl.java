package com.hotmart.payments.service.payment.impl;

import com.hotmart.payments.dto.event.*;
import com.hotmart.payments.dto.response.PaymentResponseDTO;
import com.hotmart.payments.enums.PaymentGateway;
import com.hotmart.payments.enums.PaymentStatus;
import com.hotmart.payments.enums.PaymentType;
import com.hotmart.payments.models.Customer;
import com.hotmart.payments.models.Payment;
import com.hotmart.payments.repositories.CustomerRepository;
import com.hotmart.payments.repositories.PaymentRepository;
import com.hotmart.payments.service.integration.CustomerIntegrationService;
import com.hotmart.payments.service.integration.PaymentIntegrationService;
import com.hotmart.payments.service.payment.PaymentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.hotmart.payments.enums.SagaStatus.ROLLBACK;
import static com.hotmart.payments.enums.SagaStatus.SUCCESS;

@Slf4j
@Service("MyPaymentService")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String CURRENT_SOURCE = "PAYMENT_SERVICE";

    private final PaymentRepository repository;
    private final CustomerRepository customerRepository;
    private final PaymentIntegrationService integrationService;
    private final CustomerIntegrationService customerIntegrationService;

    @Override
    public void processPaymentSaga(@NonNull OrderEventDTO event) {
        try {
            validationPayment(event);
            Customer customer = createCustomer(event);
            var responseCustomer = customerIntegrationService.createCustomer(customer, event);
            Payment payment = createPayment(event);
            integrationService.createPayment(payment, responseCustomer.getCustomerId(), event);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Erro ao processar o pagamento do cliente", e);
            handleFail(event, e.getMessage());
        }

        // envio para saga
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

    private void validationPayment(@NonNull OrderEventDTO event) {
        if(repository.existsByTransactionIdAndOrderId(event.getTransactionId(), event.getOrderId())) {
            log.info("Já existe um pagamento processa para transactionID {} e orderID {} já foi realizado.", event.getTransactionId(), event.getOrderId());
            throw new ValidateException("Já existe uma transação aberta para essa validação.");
        }

        PaymentOrderDTO payment = event.getOrder().getPayment();

        if (payment.getTotalPrice().doubleValue() < 0.1) {
            throw new ValidateException("O valor do produto deve ser maior que 0.1");
        }

        if (payment.getType().equals(PaymentType.CREDIT_CARD)) {
            if (ObjectUtils.isEmpty(payment.getCardholderName()) ||
                ObjectUtils.isEmpty(payment.getYearExpiry()) ||
                ObjectUtils.isEmpty(payment.getMonthExpiry()) ||
                ObjectUtils.isEmpty(payment.getCvv())) {
                throw new ValidateException("As informações de pagamentos estão incompletas.");
            }
        }
    }

    private Payment createPayment(@NonNull OrderEventDTO event) {
        PaymentOrderDTO payment = event.getOrder().getPayment();
        ProductOrderDTO product = event.getOrder().getProduct();

        Payment save = Payment.builder()
                .type(payment.getType())
                .transactionId(event.getTransactionId())
                .orderId(event.getOrderId())
                .amount(payment.getPrice())
                .totalAmount(payment.getTotalPrice())
                .periodicity(product.getPeriodicity())
                .userId(event.getOrder().getBuyer().getUserId())
                .productId(product.getId())
                .planId(product.getPlanId())
                .installments(payment.getInstallment())
                .gateway(PaymentGateway.ASAAS)
                .build();

        event.getOrder().getPayment().setId(save.getId());
        return repository.save(save);
    }

    private Customer createCustomer(@NonNull OrderEventDTO event) {
        BuyerOrderDTO buyer = event.getOrder().getBuyer();

        Customer save = Customer.builder()
                .email(buyer.getEmail())
                .userId(buyer.getUserId())
                .gateway(PaymentGateway.ASAAS)
                .build();


        return customerRepository.save(save);
    }

    private void addHistory(@NonNull OrderEventDTO event, @NonNull String message) {
        var history = HistoryDTO.builder()
                .source(event.getSource())
                .status(event.getStatus())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        event.addHistory(history);
    }

    private void handleFail(@NonNull OrderEventDTO event, @NonNull String message) {
        event.setStatus(ROLLBACK);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Falha ao validar produtos: ".concat(message));
    }

    private void handleSuccess(@NonNull OrderEventDTO event) {
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Produtos validados com sucesso");
    }

}
