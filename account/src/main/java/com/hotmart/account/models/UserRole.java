package com.hotmart.account.models;

import com.hotmart.account.models.pk.UserRolePk;
import jakarta.persistence.*;

@Entity
@Table(name = "users_roles")
@IdClass(UserRolePk.class)
public class UserRole {

    @Id
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "roles_id", nullable = false)
    private Integer roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roles_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

}
