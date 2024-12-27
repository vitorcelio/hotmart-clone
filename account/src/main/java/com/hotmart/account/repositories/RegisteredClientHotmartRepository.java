package com.hotmart.account.repositories;

import com.hotmart.account.models.RegisteredClientHotmart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredClientHotmartRepository extends JpaRepository<RegisteredClientHotmart, Integer> {

    Optional<RegisteredClientHotmart> findByRegisteredId(String registeredId);

}
