package com.bbva.minibank.infrastructure.repositories.springdatajpa;

import com.bbva.minibank.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserSpringRepository extends JpaRepository<UserEntity, UUID> {
	Optional<UserEntity> findByUsername(String username);
}
