package com.bbva.minibank.application.repository;

import com.bbva.minibank.infrastructure.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
	Optional<UserEntity> findByUsername(String username);
	
	void save(UserEntity userEntity);
	
	UserEntity findUserById(UUID userId);
}
