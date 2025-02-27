package com.hotmart.orders.documents;

import com.hotmart.orders.enums.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String transactionId;
    private LocalDateTime createdAt;
    private SagaStatus status;
    private ProductOrder product;
    private BuyerOrder buyer;
    private AddressOrder address;
    private PaymentOrder payment;

}
