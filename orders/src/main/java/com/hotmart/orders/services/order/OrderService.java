package com.hotmart.orders.services.order;

import com.hotmart.orders.dto.request.OrderRequestDTO;
import com.hotmart.orders.dto.response.TransactionResponseDTO;
import lombok.NonNull;

public interface OrderService {

    TransactionResponseDTO save(@NonNull OrderRequestDTO request);

}
