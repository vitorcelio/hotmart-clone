package com.hotmart.orders.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressOrder {

    private String cep;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String complement;

}
