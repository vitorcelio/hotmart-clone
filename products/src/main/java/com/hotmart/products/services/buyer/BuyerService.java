package com.hotmart.products.services.buyer;

import com.hotmart.products.dto.request.UserRequestDTO;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.models.Buyer;
import lombok.NonNull;

import java.util.List;

public interface BuyerService {

    void requestToBuyer(@NonNull Long productId, Long planId, @NonNull String email);

    void removeBuyer(@NonNull Long productId, @NonNull Long userId);

    Buyer save(@NonNull UserRequestDTO request);

    Buyer update(@NonNull UserRequestDTO request);

    List<UserResponseDTO> findAll(@NonNull Long productId, String name, String email);

    void deleteAllBuyersByProductId(@NonNull Long productId);

}
