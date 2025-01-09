package com.hotmart.orders.documents;

import com.hotmart.orders.enums.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {

    private String source;
    private SagaStatus status;
    private String message;
    private LocalDateTime createdAt;

}
