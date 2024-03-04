package com.cezar.energydevices.repository;

import com.cezar.energydevices.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    Optional<EndUser> findByUsername(String username);
}
