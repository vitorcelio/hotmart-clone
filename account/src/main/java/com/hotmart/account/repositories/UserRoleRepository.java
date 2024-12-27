package com.hotmart.account.repositories;

import com.hotmart.account.models.UserRole;
import com.hotmart.account.models.pk.UserRolePk;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, UserRolePk> {

    Set<UserRole> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from UserRole ur where ur.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
