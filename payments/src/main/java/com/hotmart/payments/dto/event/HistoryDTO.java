package com.hotmart.payments.dto.event;

import com.hotmart.payments.enums.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryDTO {

    private String source;
    private SagaStatus status;
    private String message;
    private LocalDateTime createdAt;

}
