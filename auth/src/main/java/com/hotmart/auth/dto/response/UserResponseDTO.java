package com.hotmart.auth.dto.response;

import com.hotmart.auth.models.RoleEntity;
import com.hotmart.auth.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String cpfCnpj;
    private String website;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles;

    public UserResponseDTO(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.cpfCnpj = user.getCpfCnpj();
        //this.website = user.getWebsite();
        //this.description = user.getDescription();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        Set<RoleEntity> rolesUser = user.getRoles();
        if (!ObjectUtils.isEmpty(rolesUser)) {
            this.roles = rolesUser.stream().map(RoleEntity::getName).toList();
        }
    }

    public static List<UserResponseDTO> convert(List<UserEntity> users) {
        return users.stream().map(UserResponseDTO::new).toList();
    }

}
