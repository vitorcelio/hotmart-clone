package com.hotmart.orders.dto.request;

import com.hotmart.orders.config.validator.CompareItem;
import com.hotmart.orders.config.validator.CpfOrCnpj;
import com.hotmart.orders.documents.BuyerOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CompareItem(compare = "email", other = "emailRepeat")
public class BuyerOrderRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Seu email obrigatório")
    @Email(message = "Por favor, insira um endereço de email válido")
    private String email;

    @NotBlank(message = "Seu email obrigatório")
    @Email(message = "Por favor, insira um endereço de email válido")
    private String emailRepeat;

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @CpfOrCnpj(message = "Campo inválido")
    private String cpfCnpj;

    @NotBlank(message = "Celular é obrigatório")
    private String phone;

    public BuyerOrder toBuyerOrder() {
        return BuyerOrder.builder()
                .name(name)
                .email(email)
                .cpfCnpj(cpfCnpj)
                .phone(phone)
                .build();
    }

}
