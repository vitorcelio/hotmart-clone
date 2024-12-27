package com.hotmart.auth.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class UserRolePk {

    @Id
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "roles_id", nullable = false)
    private Integer roleId;

}
