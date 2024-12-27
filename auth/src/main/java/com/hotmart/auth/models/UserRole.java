package com.hotmart.auth.models;

import com.hotmart.auth.models.pk.UserRolePk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_roles")
@IdClass(UserRolePk.class)
public class UserRole {

    @Id
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false, updatable = false, insertable = false)
    private UserEntity user;

    @Id
    @Column(name = "roles_id", nullable = false)
    private Integer roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id", nullable = false, updatable = false, insertable = false)
    private RoleEntity role;

}
