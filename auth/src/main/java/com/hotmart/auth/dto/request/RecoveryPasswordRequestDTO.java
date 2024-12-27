package com.hotmart.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoveryPasswordRequestDTO {

    @Email(message = "Digite um endereço de email válido")
    @NotBlank(message = "O email é obrigatório")
    private String email;

}
