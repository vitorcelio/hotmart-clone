package com.hotmart.products.dto.response;

import com.hotmart.products.models.Affiliate;
import com.hotmart.products.models.Buyer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private Long userId;
    private String email;
    private String name;

    public UserResponseDTO(Buyer buyer) {
        this.id = buyer.getId();
        this.userId = buyer.getUserId();
        this.email = buyer.getEmail();
        this.name = buyer.getName();
    }

    public UserResponseDTO(Affiliate affiliate) {
        this.id = affiliate.getId();
        this.userId = affiliate.getUserId();
        this.email = affiliate.getEmail();
        this.name = affiliate.getName();
    }

    public static List<UserResponseDTO> convertBuyer(List<Buyer> buyers) {
        return buyers.stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }

    public static List<UserResponseDTO> convertAffiliate(List<Affiliate> affiliates) {
        return affiliates.stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }

}
