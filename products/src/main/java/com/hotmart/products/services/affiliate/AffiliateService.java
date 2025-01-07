package com.hotmart.products.services.affiliate;

import com.hotmart.products.dto.request.UserRequestDTO;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.models.Affiliate;
import lombok.NonNull;

import java.util.List;

public interface AffiliateService {

    Affiliate save(@NonNull UserRequestDTO request);

    Affiliate update(@NonNull UserRequestDTO request);

    void requestAffiliation(@NonNull Long productId);

    void removeAffiliation(@NonNull Long productId);

    List<UserResponseDTO> findAll(@NonNull Long productId, String name, String email);

    void deleteAllAffiliatesByProductId(@NonNull Long productId);

    List<UserResponseDTO> affiliatesRequestByProductId(@NonNull Long productId, String name, String email);

    void permitAffiliation(@NonNull Long productId, @NonNull Long userId);

}
