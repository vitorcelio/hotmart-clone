package com.hotmart.orders.documents;

import com.hotmart.orders.enums.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "events")
public class Event {

    @Id
    private String id;
    private String transactionId;
    private String orderId;
    private Order order;
    private String source;
    private SagaStatus status;
    private List<History> historyList;
    private LocalDateTime createdAt;

}
