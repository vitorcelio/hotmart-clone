package com.hotmart.products.dto.event;

import com.hotmart.products.enums.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventDTO {

    private String id;
    private String transactionId;
    private String orderId;
    private OrderDTO order;
    private String source;
    private SagaStatus status;
    private List<HistoryDTO> historyList;
    private LocalDateTime createdAt;

    public void addHistory(HistoryDTO history) {
        if (ObjectUtils.isEmpty(this.historyList)) {
            this.historyList = new ArrayList<>();
        }

        this.historyList.add(history);
    }

}
