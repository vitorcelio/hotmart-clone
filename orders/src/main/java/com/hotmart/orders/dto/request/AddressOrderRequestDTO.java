package com.hotmart.orders.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressOrderRequestDTO {

    private String cep;
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String street;

}
