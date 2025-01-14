package com.hotmart.orders.services.order;

import com.hotmart.orders.documents.Order;
import com.hotmart.orders.dto.request.OrderRequestDTO;
import lombok.NonNull;

public interface OrderService {

    Order save(@NonNull OrderRequestDTO request);

}
