package com.hotmart.payments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerIntegrationResponseDTO {

    private String customerId;
    private String subaccountId;

}
