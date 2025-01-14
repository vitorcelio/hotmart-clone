package com.hotmart.products.services.buyer;

import com.hotmart.products.dto.event.OrderEventDTO;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.models.Buyer;
import lombok.NonNull;

import java.util.List;

public interface BuyerService {

    void createBuyerSaga(@NonNull OrderEventDTO event);

    void rollbackBuyerSaga(@NonNull OrderEventDTO event);

    Buyer findByUserId(@NonNull Long id);

    List<UserResponseDTO> findAll(@NonNull Long productId, String name, String email);

    void deleteAllBuyersByProductId(@NonNull Long productId);

}
