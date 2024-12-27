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
public class UserUpdatePasswordRequestDTO {

    @NotBlank(message = "Senha é obrigatório")
    private String password;

    @NotBlank(message = "Nova senha é obrigatório")
    private String newPassword;

}
