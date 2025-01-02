package com.hotmart.products.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;

    private String description;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "1.0", message = "Valor não pode ser menor que R$ 1,00")
    @Digits(integer = 19, fraction = 2, message = "O preço deve ter no máximo 19 dígitos inteiros e 2 dígitos decimais")
    private BigDecimal price;

    @NotBlank(message = "A periodicidade é obrigatória")
    private String periodicity;

    @NotBlank(message = "A forma de pagamento é obrigatória")
    private String paymentMethod;

    @NotNull(message = "O parcelamento é obrigatório")
    @Min(value = 1, message = "O número de parcelas deve ser no mínimo 1")
    @Max(value = 12, message = "O número de parcelas pode ser no máximo 12")
    private Integer installment;

    @NotBlank(message = "A forma de cobrança é obrigatória")
    private String billingMethod;

    @NotNull(message = "A recorrência é obrigatória")
    @Min(value = 1, message = "Recorrências não podem ser menor que 1")
    private Integer recurrences;

}
