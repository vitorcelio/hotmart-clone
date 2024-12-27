package com.hotmart.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {

    //@Pattern(regexp = "/^(?=.*[a-zA-Z])(?=.*\\d).{7,}$/", message = "A senha não corresponde ao requisito da diretiva de senha.")
    @NotBlank(message = "A senha é obrigatória")
    private String password;

    //@Pattern(regexp = "/^(?=.*[a-zA-Z])(?=.*\\d).{7,}$/", message = "A senha não corresponde ao requisito da diretiva de senha.")
    @NotBlank(message = "A senha é obrigatória")
    private String passwordRepeat;

}
