package com.hotmart.products.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 1, max = 250, message = "O nome deve ter entre 3 e 250 caracteres")
    private String name;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 200, max = 2000, message = "O nome deve ter 200 caracteres, no mínimo")
    private String description;

    @NotBlank(message = "O tipo do produto é obrigatório")
    private String type;

    @NotNull(message = "Selecione uma categoria")
    private Integer categoryId;

    @NotNull(message = "O prazo para solicitação de reembolso é obrigatório")
    private Integer dayRefundRequest;

    private boolean affiliation;

    private String paymentMethod;

    private BigDecimal price;

    private String image;

    @Valid
    private List<PlanRequestDTO> plans;

}
