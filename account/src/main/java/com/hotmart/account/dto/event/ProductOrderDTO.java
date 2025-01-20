package com.hotmart.account.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrderDTO {

    private Long id;
    private Long planId;
    private String name;
    private String type;
    private BigDecimal price;

}
