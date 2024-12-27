package com.hotmart.account.dto.response;

import com.hotmart.account.models.RegisteredClientHotmart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredClientResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String registeredClientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RegisteredClientResponseDTO(RegisteredClientHotmart registeredClientHotmart) {
        this.id = registeredClientHotmart.getId();
        this.name = registeredClientHotmart.getName();
        this.description = registeredClientHotmart.getDescription();
        this.registeredClientId = registeredClientHotmart.getRegisteredClientId();
        this.createdAt = registeredClientHotmart.getCreatedAt();
        this.updatedAt = registeredClientHotmart.getUpdatedAt();
    }

    public static List<RegisteredClientResponseDTO> convert(List<RegisteredClientHotmart> list) {
        return list.stream().map(RegisteredClientResponseDTO::new).toList();
    }

}
