package com.hotmart.account.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerOrderDTO {

    private String name;
    private String email;
    private String cpfCnpj;
    private String phone;
    private Long userId;

}
