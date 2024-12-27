package com.hotmart.auth.repositories;

import com.hotmart.auth.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    @Query("SELECT r FROM RoleEntity r, UserRole ur WHERE r.id = ur.roleId and ur.userId = :userId")
    List<RoleEntity> findByUserId(@Param("userId") Long userId);

}
