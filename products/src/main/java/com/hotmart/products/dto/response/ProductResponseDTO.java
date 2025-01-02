package com.hotmart.products.dto.response;

import com.hotmart.products.enums.PaymentMethod;
import com.hotmart.products.enums.ProductType;
import com.hotmart.products.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private ProductType type;
    private Long userId;
    private CategoryResponseDTO category;
    private Integer dayRefundRequest;
    private PaymentMethod paymentMethod;
    private BigDecimal price;
    private String image;
    private List<PlanResponseDTO> plans;

    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.type = product.getType();
        this.userId = product.getUserId();
        this.category = new CategoryResponseDTO(product.getCategory());
        this.dayRefundRequest = product.getDayRefundRequest();
        this.paymentMethod = product.getPaymentMethod();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.plans = product.getPlans();
    }

    public static List<ProductResponseDTO> convert(List<Product> products) {
        return products.stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }
}
