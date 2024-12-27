package com.hotmart.account.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotmart.account.models.Role;
import com.hotmart.account.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.cpfCnpj = user.getCpfCnpj();
        this.website = user.getWebsite();
        this.description = user.getDescription();
        this.birthDate = user.getBirthDate();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        Set<Role> rolesUser = user.getRoles();
        if (!ObjectUtils.isEmpty(rolesUser)) {
            this.roles = rolesUser.stream().map(Role::getName).toList();
        }
    }

    public static List<UserResponseDTO> convert(List<User> users) {
        return users.stream().map(UserResponseDTO::new).toList();
    }

}
