package com.hotmart.account.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
