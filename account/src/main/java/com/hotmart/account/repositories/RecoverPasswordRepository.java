package com.hotmart.account.repositories;

import com.hotmart.account.models.RecoverPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecoverPasswordRepository extends JpaRepository<RecoverPassword, Long> {
    Optional<RecoverPassword> findByPswdrst(String pswdrst);
}
