package com.hotmart.orders.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrder {

    private Long id;
    private Long planId;
    private String name;
    private String type;

}
