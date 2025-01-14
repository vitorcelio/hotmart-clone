package com.hotmart.products.services.validation.impl;

import com.hotmart.products.dto.event.OrderEventDTO;
import com.hotmart.products.models.Validation;
import com.hotmart.products.repositories.ValidationRepository;
import com.hotmart.products.services.validation.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final ValidationRepository repository;

    @Override
    public void createValidation(@NonNull OrderEventDTO event, boolean success) {
        Validation validation = Validation.builder()
                .orderId(event.getOrder().getId())
                .transactionId(event.getOrder().getTransactionId())
                .success(success)
                .build();

        repository.save(validation);
    }

    @Override
    public void changeValidationToFail(@NonNull OrderEventDTO event) {
        findByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId())
                .ifPresentOrElse(validation -> {
                    validation.setSuccess(false);
                    repository.save(validation);
                },
                () -> createValidation(event, false));
    }

    @Override
    public Optional<Validation> findByOrderIdAndTransactionId(@NonNull String orderId, @NonNull String transactionId) {
        return repository.findByOrderIdAndTransactionId(orderId, transactionId);
    }

    @Override
    public boolean existsByOrderIdAndTransactionId(@NonNull String orderId, @NonNull String transactionId) {
        return repository.existsByOrderIdAndTransactionId(orderId, transactionId);
    }
}
