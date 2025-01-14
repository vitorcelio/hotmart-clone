package com.hotmart.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatório")
    private String password;

    @NotBlank(message = "O tipo de usuário é obrigatório")
    private String role;

    private String cpfCnpj;
    private String phone;

}
