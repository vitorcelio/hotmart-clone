package com.hotmart.account.dto.event;

import com.hotmart.account.enums.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    @Id
    private String id;
    private String transactionId;
    private LocalDateTime createdAt;
    private SagaStatus status;
    private ProductOrderDTO product;
    private BuyerOrderDTO buyer;
    private AddressOrderDTO address;
    private PaymentOrderDTO payment;

}
