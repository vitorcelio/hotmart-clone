package com.hotmart.orders.dto.request;

import com.hotmart.orders.documents.AddressOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressOrderRequestDTO {

    @NotBlank(message = "Código Postal (CEP) é obrigatório")
    private String cep;

    @NotBlank(message = "Estado é obrigatório")
    private String state;

    @NotBlank(message = "Cidade é obrigatório")
    private String city;

    @NotBlank(message = "Bairro é obrigatório")
    private String neighborhood;

    @NotBlank(message = "Endereço é obrigatório")
    private String street;

    private String complement;

    public AddressOrder toAddressOrder() {
        return AddressOrder.builder()
                .cep(this.cep)
                .state(this.state)
                .city(this.city)
                .neighborhood(this.neighborhood)
                .street(this.street)
                .complement(this.complement)
                .build();
    }

}
