package com.hotmart.auth.repositories;

import com.hotmart.auth.models.DataAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DataAccessRepository extends JpaRepository<DataAccessEntity, UUID> {


}
