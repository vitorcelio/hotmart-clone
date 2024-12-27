package com.hotmart.auth.repositories;

import com.hotmart.auth.models.RecoverPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecoverPasswordRepository extends JpaRepository<RecoverPasswordEntity, Long> {
    Optional<RecoverPasswordEntity> findByPswdrst(String pswdrst);
}
