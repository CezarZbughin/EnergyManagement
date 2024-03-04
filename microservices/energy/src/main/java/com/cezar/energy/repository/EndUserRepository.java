package com.cezar.energy.repository;


import com.cezar.energy.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    Optional<EndUser> findByUsername(String username);
    Optional<EndUser> findByUsernameAndPassword(String username, String password);
}
