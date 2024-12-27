package com.hotmart.account.repositories;

import com.hotmart.account.models.DataAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DataAccessRepository extends JpaRepository<DataAccess, UUID> {


}
