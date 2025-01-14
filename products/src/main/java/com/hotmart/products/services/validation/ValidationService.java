package com.hotmart.products.services.validation;

import com.hotmart.products.dto.event.OrderEventDTO;
import com.hotmart.products.models.Validation;
import lombok.NonNull;

import java.util.Optional;

public interface ValidationService {

    void createValidation(@NonNull OrderEventDTO event, boolean success);

    void changeValidationToFail(@NonNull OrderEventDTO event);

    Optional<Validation> findByOrderIdAndTransactionId(@NonNull String orderId, @NonNull String transactionId);

    boolean existsByOrderIdAndTransactionId(@NonNull String orderId, @NonNull String transactionId);

}
